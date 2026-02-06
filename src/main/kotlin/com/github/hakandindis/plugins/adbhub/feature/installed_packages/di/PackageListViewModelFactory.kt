package com.github.hakandindis.plugins.adbhub.feature.installed_packages.di

import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.feature.installed_packages.presentation.PackageListViewModel
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import kotlinx.coroutines.CoroutineScope

@Service(Service.Level.PROJECT)
class PackageListViewModelFactory(
    @Suppress("unused") private val project: Project,
    private val coroutineScope: CoroutineScope
) {

    fun create(executor: AdbCommandExecutor): PackageListViewModel {
        val packageDataSource = PackageModule.createPackageDataSource(executor)
        val packageRepository = PackageModule.createPackageRepository(packageDataSource)
        val getPackagesUseCase = PackageModule.createGetPackagesUseCase(packageRepository)
        val filterPackagesUseCase = PackageModule.createFilterPackagesUseCase()
        return PackageListViewModel(
            getPackagesUseCase = getPackagesUseCase,
            filterPackagesUseCase = filterPackagesUseCase,
            coroutineScope = coroutineScope
        )
    }
}
