# Button

* [Button styles](https://git.lab.mobi/tools/android-tools/labcomponents-android/-/blob/develop/lib/java/mobi/lab/components/button/res/values/styles.xml)
* [Material Button theming](https://github.com/material-components/material-components-android/blob/master/docs/components/Button.md)

`LabButton` extends `MaterialButton` from Material components.

Includes default themes for our design system's button styles. These button styles match the base Material3 button styles.

All configurable attributes are also supported by LabButton. LabButton's styling is built on top of the existing capabilities of MaterialButton itself.

## Custom styles

Most likely, any custom button style can be achieved by providing 4 custom values for any button style:

1. Background color. This can be stateful or fixed.
2. ShapeAppearance
3. Ripple color. This can also be stateful. 
4. Stroke size and color.

All these properties are supported in the same way as MaterialButton. Our LabButton uses these values to provide a base design based on our P42 design system. 
However, any specific configurations are best done by providing custom values for these 4 properties. 

Examples can be taken from this project's button style implementations.
