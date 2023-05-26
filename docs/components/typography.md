# Typography

* [TextAppearance styles](https://git.lab.mobi/tools/android-tools/labcomponents-android/-/blob/develop/lib/java/mobi/lab/components/typography/res/values/styles.xml)
* [Theme.Lab attributes](https://git.lab.mobi/tools/android-tools/labcomponents-android/-/blob/develop/lib/java/mobi/lab/components/typography/res/values/attrs.xml)

## Letter spacing

Figma's letter spacing can't be directly used in Android as the values don't match. They need be calculated by:

```
Figma letter spacing / text size 
```

So if the letter spacing in Figma is `0.15` and text size is `16px`, the resulting letter spacing would be 0.15 / 16.

More information in this thread [https://stackoverflow.com/questions/39100969/how-to-calculate-letterspacing-for-textview-from-sketch-values](https://stackoverflow.com/questions/39100969/how-to-calculate-letterspacing-for-textview-from-sketch-values)
