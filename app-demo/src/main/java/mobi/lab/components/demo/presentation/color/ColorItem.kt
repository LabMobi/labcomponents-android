package mobi.lab.components.demo.presentation.color

import androidx.annotation.AttrRes

data class ColorItem(
    val name: String,
    @AttrRes val colorId: Int,
    @AttrRes val surfaceId: Int? = null,
)
