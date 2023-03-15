# TextField

* [TextField styles](https://git.lab.mobi/tools/android-tools/labcomponents-android/-/blob/develop/lib/java/mobi/lab/components/textfield/res/values/styles.xml)

`LabTextField` extends `TextInputLayout` from Material components and `LabTextFieldEditText` extends `TextInputEditText`.

The `Widget.Lab.TextField` and `Widget.LabComponents.TextField` styles automatically set the inner `TextFieldEditText`'s style. This mainly affects the background.

There are some constraints to customising these classes and styles:
1. The inner EditText must have a custom background set when the View is inflated. 
   1. The Material `TextInputLayout` checks for a background and skips drawing its own in this case.
   2. Our own `LabTextField` background uses the point from previous step to force the `TextInputLayou` into `filled` mode but with our custom background.
   3. This `filled` mode changes how the label is animated.
2. The background is set automatically. If the inner `LabTextFieldEditText` does not have a style set in the layout.
3. If a style is needed in a layout, make sure the style extends `Widget.Lab.TextFieldEditText` or `Widget.LabComponents.TextFieldEditText`
