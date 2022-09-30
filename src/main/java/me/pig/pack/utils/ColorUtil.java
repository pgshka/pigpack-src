package me.pig.pack.utils;

import java.awt.*;

public class ColorUtil {

   public static Color getGuiColor( ) {
        return new Color(255, 47, 255,255);
    }

    public static Color injectAlpha( Color color, int alpha ) {
        return new Color( color.getRed( ), color.getGreen( ), color.getBlue( ), Math.max(0, alpha) );
    }

}
