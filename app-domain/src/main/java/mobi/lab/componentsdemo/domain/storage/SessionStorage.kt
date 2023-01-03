package mobi.lab.componentsdemo.domain.storage

import mobi.lab.componentsdemo.domain.entities.Session

/**
 * SessionStorage does not use async operations because we want to make it clear that the operation will not block.
 * If the storage function accesses a database other that Shared Preferences, consider making the function async
 * to indicate that fact.
 */
interface SessionStorage {
    fun save(session: Session)
    fun load(): Session?
    fun clear()
}
