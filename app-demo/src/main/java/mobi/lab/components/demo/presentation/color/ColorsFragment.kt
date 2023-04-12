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
import mobi.lab.components.demo.util.FragmentBindingHolder
import mobi.lab.components.demo.util.ViewBindingHolder

class ColorsFragment : Fragment(), ViewBindingHolder<FragmentColorsBinding> by FragmentBindingHolder() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return createBinding(FragmentColorsBinding.inflate(inflater), this) {
            setupColors(this, inflater)
            switchEnabled.setOnCheckedChangeListener { _, isChecked ->
                setViewEnabled(containerColors, isChecked)
            }
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
                    // Colors
                    val backgroundColorStateList = MaterialColors.getColorStateList(
                        itemBinding.root.context,
                        item.attrId,
                        ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.lab_internal_todo))
                    )
                    // Update text color based on background color
                    itemBinding.textName.setTextColor(getTextColor(backgroundColorStateList.defaultColor))
                    // Color will add a drawable that we can tint
                    itemBinding.root.setBackgroundColor(backgroundColorStateList.defaultColor)
                    // Add the tint
                    itemBinding.root.backgroundTintList = backgroundColorStateList
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
                        ColorItem(R.attr.labColorPrimarySelector, "Primary"),
                        ColorItem(R.attr.labColorOnPrimarySelector, "On Primary"),
                    ),
                    ColorRow(
                        ColorItem(R.attr.labColorPrimarySurfaceSelector, "Primary Surface"),
                        ColorItem(R.attr.labColorOnPrimarySurfaceSelector, "On Primary Surface"),
                    )
                )
            ),
            ColorSection(
                label = "Secondary",
                rows = listOf(
                    ColorRow(
                        ColorItem(R.attr.labColorSecondarySelector, "Secondary"),
                        ColorItem(R.attr.labColorOnSecondarySelector, "On Secondary"),
                    ),
                    ColorRow(
                        ColorItem(R.attr.labColorSecondarySurfaceSelector, "Secondary Surface"),
                        ColorItem(R.attr.labColorOnSecondarySurfaceSelector, "On Secondary Surface"),
                    ),
                )
            ),
            ColorSection(
                label = "Error",
                rows = listOf(
                    ColorRow(
                        ColorItem(R.attr.labColorErrorSelector, "Error"),
                        ColorItem(R.attr.labColorOnErrorSelector, "On Error"),
                    ),
                    ColorRow(
                        ColorItem(R.attr.labColorErrorSurfaceSelector, "Error Surface"),
                        ColorItem(R.attr.labColorOnErrorSurfaceSelector, "On Error Surface"),
                    ),
                )
            ),
            ColorSection(
                label = "Success",
                rows = listOf(
                    ColorRow(
                        ColorItem(R.attr.labColorSuccessSelector, "Success"),
                        ColorItem(R.attr.labColorOnSuccessSelector, "On Success"),
                    ),
                    ColorRow(
                        ColorItem(R.attr.labColorSuccessSurfaceSelector, "Success Surface"),
                        ColorItem(R.attr.labColorOnSuccessSurfaceSelector, "On Success Surface"),
                    )
                )
            ),
            ColorSection(
                label = "Caution",
                rows = listOf(
                    ColorRow(
                        ColorItem(R.attr.labColorCautionSelector, "Caution"),
                        ColorItem(R.attr.labColorOnCautionSelector, "On Caution"),
                    ),
                    ColorRow(
                        ColorItem(R.attr.labColorCautionSurfaceSelector, "Caution Surface"),
                        ColorItem(R.attr.labColorOnCautionSurfaceSelector, "On Caution Surface"),
                    )
                )
            ),
            ColorSection(
                label = "Neutral",
                rows = listOf(
                    ColorRow(
                        ColorItem(R.attr.labColorBackground, "Background"),
                        ColorItem(R.attr.labColorOnBackgroundSelector, "On Background"),
                    ),
                    ColorRow(
                        ColorItem(R.attr.labColorSurfaceSelector, "Surface"),
                        ColorItem(R.attr.labColorOnSurfaceSelector, "On Surface"),
                    ),
                    ColorRow(
                        ColorItem(R.attr.labColorOutlineSelector, "Outline"),
                        ColorItem(R.attr.labColorDividerSelector, "Divider"),
                    )
                )
            ),
            ColorSection(
                label = "Neutral Variant",
                rows = listOf(
                    ColorRow(
                        ColorItem(R.attr.labColorSurfaceVariantSelector, "Surface Variant"),
                        ColorItem(R.attr.labColorOnSurfaceVariantSelector, "On Surface Variant"),
                    ),
                    ColorRow(
                        ColorItem(R.attr.labColorOutlineVariantSelector, "Outline Variant"),
                        ColorItem(R.attr.labColorDividerVariantSelector, "Divider Variant"),
                    )
                )
            ),
        )
    }

    private fun getTextColor(@ColorInt backgroundColor: Int): Int {
        // Use white text color if the background color is considered dark.
        return if (MaterialColors.isColorLight(backgroundColor)) Color.BLACK else Color.WHITE
    }

    private fun setViewEnabled(view: View, enabled: Boolean) {
        if (view is ViewGroup) {
            for (child in view.children) {
                setViewEnabled(child, enabled)
            }
        }
        view.isEnabled = enabled
    }
}
