package mobi.lab.componentsdemo.domain.gateway

import io.reactivex.rxjava3.core.Single
import mobi.lab.componentsdemo.domain.entities.Session

interface AuthGateway {
    fun login(username: String, password: String): Single<Session>
}
