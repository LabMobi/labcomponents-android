package mobi.lab.componentsdemo.infrastructure.common.local

import android.content.Context
import android.content.SharedPreferences
import mobi.lab.componentsdemo.app.common.isStringEmpty
import mobi.lab.componentsdemo.infrastructure.common.json.Json
import mobi.lab.componentsdemo.infrastructure.common.json.fromJson
import mobi.lab.componentsdemo.infrastructure.common.json.toJson
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SharedPreferenceStorage @Inject constructor(
    context: Context,
    private val json: Json,
) {

    private val prefs: SharedPreferences by lazy { context.getSharedPreferences(KEY_PREFS_NAME, Context.MODE_PRIVATE) }

    internal inline fun <reified T : Any> getObject(key: String, defaultValue: T? = null, type: Type? = null): T? {
        val rawValue = get<String>(key)
        if (isStringEmpty(rawValue)) {
            return defaultValue
        }
        if (type == null) {
            // Can't be null here
            return json.fromJson(rawValue.orEmpty())
        }
        return json.fromJson(rawValue.orEmpty(), type)
    }

    internal fun putObject(key: String, item: Any?) {
        if (item == null) {
            edit { it.remove(key) }
            return
        }
        put(key, json.toJson(item))
    }

    internal fun put(key: String, value: Any?) {
        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> throw UnsupportedOperationException("Unknown preference type")
        }
    }

    internal inline fun <reified T : Any> get(key: String, defaultValue: T? = null): T? {
        return when (T::class) {
            String::class -> prefs.getString(key, defaultValue as? String) as T?
            Int::class -> prefs.getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> prefs.getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> prefs.getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> prefs.getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Unknown preference type")
        }
    }

    internal fun edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = prefs.edit()
        operation(editor)
        editor.apply()
    }

    companion object {
        private const val KEY_PREFS_NAME = "storage"
    }
}
