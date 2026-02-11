package com.github.hakandindis.plugins.adbhub.feature.package_details.di

import com.github.hakandindis.plugins.adbhub.core.adb.AdbCommandExecutor
import com.github.hakandindis.plugins.adbhub.core.selection.SelectionManager
import com.github.hakandindis.plugins.adbhub.feature.package_details.presentation.PackageDetailsViewModel
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import kotlinx.coroutines.CoroutineScope

@Service(Service.Level.PROJECT)
class PackageDetailsViewModelFactory(
    private val project: Project,
    private val coroutineScope: CoroutineScope
) {

    fun create(executor: AdbCommandExecutor): PackageDetailsViewModel {
        val packageDetailsRepository = PackageDetailsModule.createPackageDetailsRepository(executor)
        val getPackageDetailsUseCase = PackageDetailsModule.createGetPackageDetailsUseCase(packageDetailsRepository)
        val selectionManager = project.service<SelectionManager>()
        return PackageDetailsViewModel(
            getPackageDetailsUseCase = getPackageDetailsUseCase,
            selectionManager = selectionManager,
            coroutineScope = coroutineScope
        )
    }
}
