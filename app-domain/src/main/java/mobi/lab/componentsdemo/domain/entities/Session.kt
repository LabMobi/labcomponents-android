package mobi.lab.componentsdemo.domain.entities

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import mobi.lab.componentsdemo.app.common.isStringEmpty

@Parcelize
data class Session(val token: String?) : Parcelable {

    fun isValid(): Boolean {
        return !isStringEmpty(token)
    }
}
