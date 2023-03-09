# Theme

* [Theme definitions](https://git.lab.mobi/tools/android-tools/labcomponents-android/-/blob/develop/lib/java/mobi/lab/components/theme/res/values/styles.xml)

`Theme.Lab.X` themes provide wrappers for `MaterialComponents` themes. They provide default values for all the theme attributes our components use. 
These are typically prefixed with `lab`. 

The theme also provides mappings from our design system's colors Material design system's colors. This makes it easier to use Material components that are not 
supported by our component library.

# Component styles

Components can also define an additional `LabComponents` style that directly depends on `MaterialComponents` theme and bypasses the entire `Lab` theme. 
This is especially useful in 2 scenarios:

- While the entire component library and design system are still in progress and not fully usable.
- When including only a subset of the components in projects that don't want to implement the entire design system.

