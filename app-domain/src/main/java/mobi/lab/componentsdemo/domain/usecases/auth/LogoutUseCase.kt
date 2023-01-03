package mobi.lab.componentsdemo.domain.usecases.auth

import dagger.Reusable
import io.reactivex.rxjava3.core.Completable
import mobi.lab.componentsdemo.domain.usecases.UseCase
import javax.inject.Inject

@Reusable
class LogoutUseCase @Inject constructor(
    private val deleteSessionUseCase: DeleteSessionUseCase
) : UseCase() {
    fun execute(): Completable {
        return Completable.fromCallable { deleteSessionUseCase.execute() }
    }
}
