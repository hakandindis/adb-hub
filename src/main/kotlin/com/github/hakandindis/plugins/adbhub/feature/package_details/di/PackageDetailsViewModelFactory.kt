package com.github.hakandindis.plugins.adbhub.feature.package_details.di

import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.PackageDetailsViewModel
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import kotlinx.coroutines.CoroutineScope

@Service(Service.Level.PROJECT)
class PackageDetailsViewModelFactory(
    @Suppress("unused") private val project: Project,
    private val coroutineScope: CoroutineScope
) {

    fun create(executor: AdbCommandExecutor): PackageDetailsViewModel {
        val packageDetailsRepository = PackageDetailsModule.createPackageDetailsRepository(executor)
        val getPackageDetailsUseCase = PackageDetailsModule.createGetPackageDetailsUseCase(packageDetailsRepository)
        return PackageDetailsViewModel(
            getPackageDetailsUseCase = getPackageDetailsUseCase,
            coroutineScope = coroutineScope
        )
    }
}
