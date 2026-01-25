# Recent Deep Links Kalıcılık Raporu

**Amaç:** Execute edilmiş deep link URI’lerini kaydetmek, "Recent Deep Links" alanında listelemek ve kullanıcı tıkladığında TextField’a auto-fill yapmak.

**Kapsam:** IntelliJ Platform eklentisi içinde bu verileri kalıcı olarak saklamak için kullanılabilecek teknolojiler ve önerilen mimari.

---

## 1. Özet ve Öneri

| Yöntem | Kullanım | Recent deep links için uygunluk |
|--------|----------|--------------------------------|
| **PersistentStateComponent + Application Service** | Yapılandırma, listeler, orta boyutlu state | ✅ **Önerilen** |
| **PropertiesComponent** | Birkaç basit key-value | ⚠️ Liste için uygun değil |
| **In-memory (StateFlow / singleton)** | Oturum içi, IDE kapanınca silinir | ❌ Kalıcılık yok |

**Öneri:** `PersistentStateComponent` uygulayan **application-level** bir servis ile state’i XML’e yazmak. URI listesi `List<String>` olarak saklanır; en yeni önce (insert-at-front + son N adet ile sınırlama).

---

## 2. IntelliJ Platform’da Kalıcı Veri Saklama Seçenekleri

### 2.1. PersistentStateComponent (Önerilen)

**Ne işe yarar:** Bileşen/service state’ini XML’e serileştirip IDE kapatılıp açıldığında geri yükler.

**Özellikler:**
- State sınıfı: public alanlar, bean property’ler, koleksiyonlar, map’ler serileştirilir.
- `@State` ile depolama yeri (hangi XML dosyası) tanımlanır.
- **Application-level:** Tüm IDE için tek state (projeden bağımsız).
- **Project-level:** Her proje için ayrı state.

**Recent deep links için:** Uygulama genelinde tek liste daha mantıklı (aynı URI’ler farklı projelerde de kullanılır). Bu yüzden **application-level** tercih edilmeli.

**Kotlin’de iki uygun taban sınıf:**

| Sınıf | Özet | State yapısı |
|-------|------|--------------|
| `SimplePersistentStateComponent<State>` | `BaseState` tabanlı, property delegate’ler | `BaseState` alt sınıfı |
| `SerializablePersistentStateComponent<State>` | 2022.2+, immutable data class, `updateState { }` | `data class` |

`List<String>` gibi basit bir state için ikisi de kullanılabilir; `SerializablePersistentStateComponent` daha güncel ve immutable state için uygundur.

---

### 2.2. PropertiesComponent

**Ne işe yarar:** Az sayıda basit değer (String, int, boolean) key-value olarak saklar.

**Sınırlamalar:**
- Roaming kapalı (Settings Sync / Export ile paylaşılmaz).
- Liste için doğal bir yapı yok; JSON string veya `key0`, `key1` gibi anahtarlara bölmek gerekir.
- Liste sırası, ekleme/çıkarma ve “son N kayıt” gibi mantığı sizin yazmanız gerekir.

**Recent deep links için:** Teknik olarak mümkün ama liste işlemleri ve sınırlama için ek kod gerekir. **Önerilmez.**

---

### 2.3. In-memory (StateFlow, singleton)

**Mevcut örnek:** `CommandLogger` — `MutableStateFlow` ile sadece oturum içi tutuluyor, IDE kapanınca siliniyor.

**Recent deep links için:** Kalıcılık istendiği için **uygun değil**. Sadece “oturum içi son URI’ler” istenirse kullanılabilir.

---

## 3. Depolama Yeri (@State / @Storage)

`@State` ve `@Storage` ile nereye yazılacağı belirlenir.

### 3.1. Application-level (önerilen)

```text
$APP_CONFIG$  →  IDE config dizini (örn. ~/.config/JetBrains/IntelliJIdea2024.x)
```

Örnek:

```kotlin
@State(
    name = "AdbHubRecentDeepLinks",
    storages = [
        Storage(value = "adbhub-recent-deeplinks.xml", roamingType = RoamingType.DISABLED)
    ]
)
```

`value` verilip `file` yazılmazsa, application-level servislerde dosya `$APP_CONFIG$` altına gider. Ayrıca açıkça `file = "$APP_CONFIG$/adbhub-recent-deeplinks.xml"` de kullanılabilir.

- `RoamingType.DISABLED`: Settings Sync / Backup and Sync ile gönderilmez (cihaza özel recent listesi mantıklı).

### 3.2. Project-level (alternatif)

Her proje için ayrı recent listesi istenirse:

```kotlin
@Storage(StoragePathMacros.WORKSPACE_FILE)  // .iws benzeri workspace dosyası
// veya
@Storage("adbhub-deeplinks.xml")           // proje dizininde ayrı XML
```

Proje bazlı kullanım raporta **alternatif** olarak bırakıldı; varsayılan öneri application-level.

### 3.3. Diğer makrolar (referans)

- `StoragePathMacros.WORKSPACE_FILE` — project workspace
- `StoragePathMacros.CACHE_FILE` — önbellek; IDE silindiğinde gidebilecek veriler için
- `$APP_CONFIG$` — uygulama ayarları

Recent deep links için `$APP_CONFIG$` (veya eşdeğeri) uygundur.

---

## 4. Servis Seviyesi: Application vs Project

| Seviye | Erişim | Recent list scope |
|--------|--------|-------------------|
| **Application** | `ApplicationManager.getApplication().service<RecentDeepLinksService>()` | Tüm projelerde ortak liste |
| **Project** | `project.service<RecentDeepLinksService>()` | Proje bazlı ayrı listeler |

**Öneri:** Recent deep links için **Application** seviyesi.

Mevcut projede `CoroutineScopeHolder` `@Service(Service.Level.PROJECT)` kullanıyor; yeni servis `Service.Level.APP` ile tanımlanabilir.

---

## 5. Önerilen Mimari: PersistentStateComponent + Application Service

### 5.1. State sınıfı

- `List<String>`: URI’ler, en yeni başta.
- İsteğe bağlı: `lastUsedEpochMillis` gibi ek bilgi; ilk aşamada sadece `List<String>` yeterli.

Serileştirme: `List<String>` IntelliJ’in state API’si ile desteklenir.

### 5.2. Servis arayüzü

- `addAndTruncate(uri: String, maxSize: Int = 20)`:  
  - URI’yi listenin başına ekler,  
  - Varsa eski aynı URI’yi kaldırır (tekrar edenleri önlemek),  
  - `maxSize`’dan uzunsa listeyi keser.
- `getRecentUris(): List<String>`:  
  - Okuma amaçlı; doğrudan state’teki listeyi döndürebilir (veya kopya).

### 5.3. State güncellemesi ve kaydetme

- `SimplePersistentStateComponent` / `SerializablePersistentStateComponent` kullanılıyorsa, state nesnesindeki `List` güncellenince platform `getState()` ile alıp XML’e yazar.  
- `BaseState` + `list` kullanımında, liste değişiminde `incrementModificationCount()` gerekebilir; `SerializablePersistentStateComponent` + `updateState { }` ile bu genelde halledilir.

### 5.4. UI tarafı

- **Recent Deep Links** bölümü: `getRecentUris()` ile doldurulur.
- Öğeye tıklanınca: `onRecentUriSelected(uri: String)` → TextField’a `setText(uri)` (veya `setTextAndPlaceCursorAtEnd`).
- Send’e basıldığında execute’dan **sonra** `RecentDeepLinksService.addAndTruncate(uri)` çağrılır (başarılı execute’lar için kayıt yapılabilir; hata durumunda kaydetmemek de tercih edilebilir).

---

## 6. Alternatif: PropertiesComponent ile (önerilmez)

Tek key’de JSON:

```kotlin
// Kaydetme
val json = Json.encodeToString(uriList)
PropertiesComponent.getInstance().setValue("com.github.hakandindis.adbhub.recentDeepLinks", json, "[]")

// Okuma
val json = PropertiesComponent.getInstance().getValue("com.github.hakandindis.adbhub.recentDeepLinks", "[]")
val list = Json.decodeFromString<List<String>>(json)
```

- Ek bağımlılık: `kotlinx.serialization` (eğer projede yoksa).
- Liste sırası, tekrarların kaldırılması, max N kayıt gibi kurallar tamamen sizin kodunuzda.
- Roaming yok.

Bu yüzden **PersistentStateComponent** daha temiz ve bakımı kolay.

---

## 7. Dikkat Edilmesi Gerekenler

1. **Namespace:** `PropertiesComponent` kullanılsa bile key’lere plugin ID ön eki (`com.github.hakandindis.plugins.adbhub.*`) eklenmeli.
2. **State sınıfı:** Varsayılan (parametresiz) constructor olmalı; platform ilk yüklemede bunu kullanır.
3. **Roaming:** Recent listesi makineye özel kalacaksa `RoamingType.DISABLED` yeterli.
4. **Threading:** State okuma/yazma platform tarafından yönetilir; UI (Compose) tarafında `ApplicationManager.getApplication().service<>()` veya `invokeLater` / `EdtExecutor` ile erişmek gerekebilir. Service’in kendisi EDT dışında da kullanılabilir; UI güncellemesi için `StateFlow` veya `Topic` ile dinleyici eklenebilir.
5. **Hassas veri:** URI’ler bazen token veya özel ID içerebilir. Hassas kabul ediliyorsa [Persisting Sensitive Data](https://plugins.jetbrains.com/docs/intellij/persisting-sensitive-data.html) dokümanına göre `SecretField`/ benzeri düşünülebilir; genel kullanımda çoğu deep link için normal state yeterlidir.

---

## 8. Özet Karar Tablosu

| Karar | Seçim |
|-------|--------|
| **Teknoloji** | `PersistentStateComponent` (tercihen `SerializablePersistentStateComponent`) |
| **Servis seviyesi** | `@Service(Service.Level.APP)` — Application |
| **Depolama** | `$APP_CONFIG$` (veya eşdeğeri) altında `adbhub-recent-deeplinks.xml` |
| **State** | `List<String> recentUris` (en yeni önce, örn. max 20) |
| **Roaming** | `RoamingType.DISABLED` |
| **Kaydetme zamanı** | Deep link başarıyla execute edildikten sonra `addAndTruncate(uri)` |

---

## 9. Referanslar

- [Persisting State of Components — IntelliJ Platform SDK](https://plugins.jetbrains.com/docs/intellij/persisting-state-of-components.html)
- [Plugin Services — IntelliJ Platform SDK](https://plugins.jetbrains.com/docs/intellij/plugin-services.html)
- [Settings Guide](https://plugins.jetbrains.com/docs/intellij/settings-guide.html)
- Mevcut proje: `CoroutineScopeHolder` (`@Service(Service.Level.PROJECT)`), `CommandLogger` (in-memory).

---

## 10. Sonraki Adımlar (Uygulama İçin)

1. `RecentDeepLinksState` (içinde `List<String> recentUris`) ve `RecentDeepLinksService : SerializablePersistentStateComponent<RecentDeepLinksState>` (veya `SimplePersistentStateComponent`) oluşturmak.
2. `@State` ve `@Storage` ile `adbhub-recent-deeplinks.xml` tanımlamak; `@Service(Service.Level.APP)` ile application servisi olarak kaydetmek (veya plugin.xml’de `com.intellij.applicationService`).
3. `addAndTruncate(uri)` ve `getRecentUris()` implementasyonu.
4. `PackageActionsViewModel` / `LaunchDeepLink` başarılı olduğunda `RecentDeepLinksService.addAndTruncate(uri)` çağrısı eklemek.
5. `AppActionsTab`’ta "Recent Deep Links" alanı; `getRecentUris()` ile liste, tıklanınca TextField’a `setText(uri)` (auto-fill).

Bu adımlar, mevcut ADB Hub mimarisi ve Compose UI ile uyumlu şekilde uygulanabilir.
