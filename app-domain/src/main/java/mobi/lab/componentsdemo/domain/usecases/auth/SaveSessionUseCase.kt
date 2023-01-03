package mobi.lab.componentsdemo.domain.usecases.auth

import dagger.Reusable
import mobi.lab.componentsdemo.domain.entities.Session
import mobi.lab.componentsdemo.domain.storage.SessionStorage
import mobi.lab.componentsdemo.domain.usecases.UseCase
import javax.inject.Inject

@Reusable
class SaveSessionUseCase @Inject constructor(private val sessionStorage: SessionStorage) : UseCase() {

    fun execute(session: Session) {
        sessionStorage.save(session)
    }
}
