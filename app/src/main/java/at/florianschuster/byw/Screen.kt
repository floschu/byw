package at.florianschuster.byw

import android.os.Parcelable
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

sealed class Screen : Parcelable {

    @Parcelize
    object Overview : Screen()

    @Parcelize
    data class Detail(val type: Type) : Screen() {
        enum class Type(
            @StringRes val displayName: Int
        ) {
            Home(R.string.detail_type_home),
            Lockscreen(R.string.detail_type_lockscreen)
        }
    }
}
