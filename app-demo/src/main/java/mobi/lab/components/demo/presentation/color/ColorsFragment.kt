package mobi.lab.components.demo.presentation.color

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.google.android.material.color.MaterialColors
import mobi.lab.components.R
import mobi.lab.components.demo.databinding.ColorViewColorItemBinding
import mobi.lab.components.demo.databinding.ColorViewColorSectionBinding
import mobi.lab.components.demo.databinding.FragmentColorsBinding
import mobi.lab.components.demo.util.EdgeToEdgeSpec
import mobi.lab.components.demo.util.EdgeToEdgeUtil
import mobi.lab.components.demo.util.FragmentBindingHolder
import mobi.lab.components.demo.util.ViewBindingHolder

class ColorsFragment : Fragment(), ViewBindingHolder<FragmentColorsBinding> by FragmentBindingHolder() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return createBinding(FragmentColorsBinding.inflate(inflater), this) {
            setupColors(this, inflater)
            switchEnabled.setOnCheckedChangeListener { _, isChecked ->
                setViewEnabled(containerColors, isChecked)
            }
            EdgeToEdgeUtil.applyPaddings(containerColors, EdgeToEdgeSpec.AVOID_BAR_AND_CUTOUT_SET_BOTTOM, true)
        }
    }

    private fun setupColors(binding: FragmentColorsBinding, inflater: LayoutInflater) {
        binding.containerColors.removeAllViews()

        getColorSections().map { section ->
            val sectionBinding = ColorViewColorSectionBinding.inflate(inflater)
            sectionBinding.textLabel.text = section.label

            section.rows.map { row ->
                val rowLayout = LinearLayout(context)
                rowLayout.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                row.items.map { item ->
                    val itemBinding = ColorViewColorItemBinding.inflate(inflater)
                    itemBinding.textName.text = item.name

                    val colors = resolveColors(item)

                    // Update text color based on background color
                    itemBinding.textName.setTextColor(colors.text)

                    // Color will add a drawable that we can tint
                    itemBinding.background.setBackgroundColor(colors.background.defaultColor)
                    itemBinding.background.backgroundTintList = colors.background
                    if (colors.foreground != null) {
                        // Color will add a drawable that we can tint
                        itemBinding.foreground.setBackgroundColor(colors.foreground.defaultColor)
                        itemBinding.foreground.backgroundTintList = colors.foreground
                    }
                    // Add the tint
                    itemBinding.root.setOnClickListener {
                        // Do nothing. We need a click listener to add focused and pressed states
                    }
                    rowLayout.addView(itemBinding.root)
                }
                sectionBinding.containerRows.addView(rowLayout)
            }
            binding.containerColors.addView(sectionBinding.root)
        }
    }

    @Suppress("LongMethod")
    private fun getColorSections(): List<ColorSection> {
        return listOf(
            ColorSection(
                label = "Primary",
                rows = listOf(
                    ColorRow(
                        ColorItem("Primary", colorId = R.attr.labColorPrimarySelector),
                        ColorItem("On Primary", colorId = R.attr.labColorOnPrimarySelector, surfaceId = R.attr.labColorPrimarySelector),
                    ),
                    ColorRow(
                        ColorItem("Primary Surface", colorId = R.attr.labColorPrimarySurfaceSelector),
                        ColorItem(
                            "On Primary Surface",
                            colorId = R.attr.labColorOnPrimarySurfaceSelector,
                            surfaceId = R.attr.labColorPrimarySurfaceSelector
                        ),
                    )
                )
            ),
            ColorSection(
                label = "Secondary",
                rows = listOf(
                    ColorRow(
                        ColorItem("Secondary", colorId = R.attr.labColorSecondarySelector),
                        ColorItem("On Secondary", colorId = R.attr.labColorOnSecondarySelector, surfaceId = R.attr.labColorSecondarySelector),
                    ),
                    ColorRow(
                        ColorItem("Secondary Surface", colorId = R.attr.labColorSecondarySurfaceSelector),
                        ColorItem(
                            "On Secondary Surface",
                            colorId = R.attr.labColorOnSecondarySurfaceSelector,
                            surfaceId = R.attr.labColorSecondarySurfaceSelector
                        ),
                    ),
                )
            ),
            ColorSection(
                label = "Error",
                rows = listOf(
                    ColorRow(
                        ColorItem("Error", colorId = R.attr.labColorErrorSelector),
                        ColorItem("On Error", colorId = R.attr.labColorOnErrorSelector, surfaceId = R.attr.labColorErrorSelector),
                    ),
                    ColorRow(
                        ColorItem("Error Surface", colorId = R.attr.labColorErrorSurfaceSelector),
                        ColorItem(
                            "On Error Surface",
                            colorId = R.attr.labColorOnErrorSurfaceSelector,
                            surfaceId = R.attr.labColorErrorSurfaceSelector
                        ),
                    ),
                )
            ),
            ColorSection(
                label = "Success",
                rows = listOf(
                    ColorRow(
                        ColorItem("Success", colorId = R.attr.labColorSuccessSelector),
                        ColorItem(
                            "On Success",
                            colorId = R.attr.labColorOnSuccessSelector,
                            surfaceId = R.attr.labColorSuccessSelector
                        ),
                    ),
                    ColorRow(
                        ColorItem("Success Surface", colorId = R.attr.labColorSuccessSurfaceSelector),
                        ColorItem(
                            "On Success Surface",
                            colorId = R.attr.labColorOnSuccessSurfaceSelector,
                            surfaceId = R.attr.labColorSuccessSurfaceSelector
                        ),
                    )
                )
            ),
            ColorSection(
                label = "Caution",
                rows = listOf(
                    ColorRow(
                        ColorItem("Caution", colorId = R.attr.labColorCautionSelector),
                        ColorItem("On Caution", colorId = R.attr.labColorOnCautionSelector, surfaceId = R.attr.labColorCautionSelector),
                    ),
                    ColorRow(
                        ColorItem("Caution Surface", colorId = R.attr.labColorCautionSurfaceSelector),
                        ColorItem(
                            "On Caution Surface",
                            colorId = R.attr.labColorOnCautionSurfaceSelector,
                            surfaceId = R.attr.labColorCautionSurfaceSelector
                        ),
                    )
                )
            ),
            ColorSection(
                label = "Neutral",
                rows = listOf(
                    ColorRow(
                        ColorItem("Background", colorId = R.attr.labColorBackground),
                        ColorItem("On Background", colorId = R.attr.labColorOnBackgroundSelector, surfaceId = R.attr.labColorBackground),
                    ),
                    ColorRow(
                        ColorItem("Surface", colorId = R.attr.labColorSurfaceSelector),
                        ColorItem("On Surface", colorId = R.attr.labColorOnSurfaceSelector, surfaceId = R.attr.labColorSurfaceSelector),
                    ),
                    ColorRow(ColorItem("Outline", colorId = R.attr.labColorOutlineSelector)),
                    ColorRow(ColorItem("Divider", colorId = R.attr.labColorDividerSelector)),
                )
            ),
            ColorSection(
                label = "Neutral Variant",
                rows = listOf(
                    ColorRow(
                        ColorItem("Surface Variant", colorId = R.attr.labColorSurfaceVariantSelector),
                        ColorItem(
                            "On Surface Variant",
                            colorId = R.attr.labColorOnSurfaceVariantSelector,
                            surfaceId = R.attr.labColorSurfaceVariantSelector
                        ),
                    ),
                    ColorRow(ColorItem("Outline Variant", colorId = R.attr.labColorOutlineVariantSelector)),
                    ColorRow(ColorItem("Divider Variant", colorId = R.attr.labColorDividerVariantSelector)),
                )
            ),
        )
    }

    private fun setViewEnabled(view: View, enabled: Boolean) {
        if (view is ViewGroup) {
            for (child in view.children) {
                setViewEnabled(child, enabled)
            }
        }
        view.isEnabled = enabled
    }

    private fun resolveColors(item: ColorItem): ItemColors {
        val colorId = item.colorId
        val surfaceId = item.surfaceId
        return if (surfaceId == null) {
            // If there is no separate surface, then use the main color for the entire item and don't show a foreground color
            ItemColors(background = getColorStateList(colorId), foreground = null)
        } else {
            // If there is are 2 colors. Use surface as the background and the color value as the foreground
            ItemColors(background = getColorStateList(surfaceId), foreground = getColorStateList(colorId))
        }
    }

    private fun getColorStateList(attrId: Int): ColorStateList {
        val context = requireContext()
        return MaterialColors.getColorStateList(
            context,
            attrId,
            ColorStateList.valueOf(ContextCompat.getColor(context, R.color.lab_internal_todo))
        )
    }

    private data class ItemColors(
        val background: ColorStateList,
        val foreground: ColorStateList?
    ) {
        /**
         * Text color. Based on the background color value
         */
        val text: Int
            @ColorInt
            get() = getTextColor(background.defaultColor)

        private fun getTextColor(@ColorInt backgroundColor: Int): Int {
            // Use white text color if the background color is considered dark.
            return if (MaterialColors.isColorLight(backgroundColor)) Color.BLACK else Color.WHITE
        }
    }
}
