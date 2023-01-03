package mobi.lab.componentsdemo.infrastructure.auth.local

import mobi.lab.componentsdemo.domain.entities.Session
import mobi.lab.componentsdemo.domain.storage.SessionStorage
import mobi.lab.componentsdemo.infrastructure.common.local.SharedPreferenceStorage
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SessionPreferenceStorage @Inject constructor(
    private val sharedPrefs: SharedPreferenceStorage,
    private val mapper: DbSessionMapper
) : SessionStorage {
    private var cache: Session? = null

    override fun save(session: Session) {
        try {
            saveToPreferences(session)
            setCache(session)
        } catch (error: Exception) {
            Timber.w(error, "save")
        }
    }

    override fun load(): Session? {
        val cached = getCache()
        if (cached != null) {
            Timber.d("load CACHE this=$this")
            return cached
        }
        try {
            Timber.d("load STORAGE this=$this")
            val session = loadFromPreferences()
            setCache(session)
            return session
        } catch (error: Exception) {
            Timber.w(error, "load")
            return null
        }
    }

    private fun loadFromPreferences(): Session? {
        val session = sharedPrefs.getObject<DbSession>(KEY)
        return if (session != null) mapper.toEntity(session) else null
    }

    private fun saveToPreferences(session: Session?) {
        sharedPrefs.putObject(KEY, if (session != null) mapper.toDb(session) else null)
    }

    private fun setCache(session: Session?) {
        synchronized(this) {
            cache = session
        }
    }

    private fun getCache(): Session? {
        synchronized(this) {
            return cache
        }
    }

    override fun clear() {
        cache = null
        sharedPrefs.putObject(KEY, null)
    }

    companion object {
        private const val KEY = "KEY_SESSION"
    }
}
