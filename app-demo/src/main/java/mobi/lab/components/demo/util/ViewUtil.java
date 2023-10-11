package mobi.lab.components.demo.util;

import android.graphics.Color;

public class ViewUtil {

    public static int blendColors(int foreground, int background) {
        int bgRed = Color.red(background);
        int bgGreen = Color.green(background);
        int bgBlue = Color.blue(background);

        int fgRed = Color.red(foreground);
        int fgGreen = Color.green(foreground);
        int fgBlue = Color.blue(foreground);

        float alpha = Color.alpha(foreground) / 255.0f;

        int red = blendAlpha(alpha, fgRed, bgRed);
        int green = blendAlpha(alpha, fgGreen, bgGreen);
        int blue = blendAlpha(alpha, fgBlue, bgBlue);
        int result = Color.rgb(red, green, blue);
//        Log log = Log.getInstance("ViewUtil");
//        log.d("getColorWithoutAlpha red=" + fgRed + " => " + red);
//        log.d("getColorWithoutAlpha red=" + fgGreen + " => " + green);
//        log.d("getColorWithoutAlpha red=" + fgBlue + " => " + blue);
//        log.d("getColorWithoutAlpha alpha=" + alpha + " color=" + getColorString(foreground) + " blended=" + getColorString(result));
        return result;
    }

    public static String getColorString(int color) {
        int alpha = Color.alpha(color);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);

        return "#" + Integer.toHexString(alpha) + Integer.toHexString(red) + Integer.toHexString(green) + Integer.toHexString(blue);
    }

    private static int blendAlpha(float alpha, int fgColor, int bgColor) {
        return Math.round(fgColor * alpha + (1 - alpha) * bgColor);
    }
}
