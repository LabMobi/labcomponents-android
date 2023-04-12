# Colors

* [Theme.Lab attributes](https://git.lab.mobi/tools/android-tools/labcomponents-android/-/blob/develop/lib/java/mobi/lab/components/color/res/values/attrs.xml)
* [Default color system](https://git.lab.mobi/tools/android-tools/labcomponents-android/-/tree/develop/lib/java/mobi/lab/components/color/res/values/colors.xml)
* [Default color selectors](https://git.lab.mobi/tools/android-tools/labcomponents-android/-/tree/develop/lib/java/mobi/lab/components/color/res/color)

## Color System

There are 3 types of color attributes. The attributes are named so that each level builds on top of the previous to become more specific:
1. Full colors 
   1. Attribute example: `labColorPrimary`
2. Color selectors 
   1. Attribute example: `labColorPrimarySelector`
   2. Color selectors have default implementations that automatically use the selector states.
   3. Default selector are defined as attributes so they can be overridden to add/remove additional states.
3. Color selector states
   1. Attribute example: `labColorPrimarySelectorDisabled`
   1. Used be the default color selector definitions.

## Internal color resources 

There are multiple layers of color definitions used throughout the components:

### Theme attributes

Colors set by the theme. These should be used when the color should be definable by the integrator.

```
?attr/labColorPrimary
?attr/labColorSecondarySelector
```

### Color System Resources

Internal semantic color resources. These are reusable across different themes, etc. These values are not changeable by integrators and are always fixed.

**NB! There are separate colors for `light` and `dark` themes.**

```
@color/lab_internal_sys_color_light_primary
@color/lab_internal_sys_color_light_secondary

@color/lab_internal_sys_color_dark_primary
@color/lab_internal_sys_color_dark_secondary
```

### Color Palette Resources

Internal color palette resources. Should be used only to define the `Color System Resources` level colors. 
The main idea here is to reuse these definitions across `light` and `dark` mode colors and get an overview of the entire color palette.

These values have no semantic meaning and thus should not be used anywhere expect the `Color System Resources` level.

```
@color/lab_internal_ref_palette_primary60
@color/lab_internal_ref_palette_secondary80
```
