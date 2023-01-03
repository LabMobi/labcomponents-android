package mobi.lab.componentsdemo.domain.usecases.auth

import dagger.Reusable
import mobi.lab.componentsdemo.domain.storage.SessionStorage
import mobi.lab.componentsdemo.domain.usecases.UseCase
import javax.inject.Inject

@Reusable
class DeleteSessionUseCase @Inject constructor(private val sessionStorage: SessionStorage) : UseCase() {

    fun execute() {
        sessionStorage.clear()
    }
}
