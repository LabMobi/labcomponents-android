# TextField

* [TextField styles](https://git.lab.mobi/tools/android-tools/labcomponents-android/-/blob/develop/lib/java/mobi/lab/components/textfield/res/values/styles.xml)

`LabTextField` extends `TextInputLayout` from Material components and `LabTextFieldEditText` extends `TextInputEditText`. 

There are 2 key points that change the default behaviour to achieve the Lab design spec:

1. The TextInputLayout is in `filled` background mode.
2. The inner EditText has a default background.
   1. The Material `TextInputLayout` checks for an existing background and skips drawing its own if one is set.
   2. This allows us to provide our own background implementation. (The Box around the entire EditText)

There are some constraints to customising these classes and styles:
1. The inner EditText must have a custom background set when the View is inflated.
   1. The `Widget.Lab.TextField` and `Widget.LabComponents.TextField` styles automatically set the inner `TextFieldEditText`'s style.
      1. This style has a default background for the EditText.
   4. This `filled` mode changes how the label is animated.
2. If a custom EditText style is needed in a layout for `LabTextFieldEditText`, make sure the style extends `Widget.Lab.TextFieldEditText` or `Widget.LabComponents.TextFieldEditText`
