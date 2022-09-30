package me.pig.pack.utils.font;

import me.pig.pack.api.Globals;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class FontUtil
{

    public static final IFontRenderer FONT = new CustomFontRenderer("verdana", 18);

    // floats
    public static float drawStringWithShadow( String text, float x, float y, int color )
    {
        return drawStringWithShadow( text, ( int )x, ( int )y, color );
    }

    public static void prepare() {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.depthMask((boolean)false);
        GlStateManager.disableAlpha();
        GlStateManager.disableCull();
        GlStateManager.enableBlend();
        GL11.glDisable(3553);
        GL11.glEnable(2848);
        GL11.glBlendFunc(770, 771);
    }

    public static float drawString( String text, float x, float y, int color )
    {
        return FONT.drawString( text, x, y, color );
    }

    // ints
    public static float drawStringWithShadow( String text, int x, int y, int color )
    {
        return Globals.mc.fontRenderer.drawStringWithShadow( text, x, y, color );
    }

    public static float drawString( String text, int x, int y, int color )
    {
        return FONT.drawString( text, x, y, color );
    }

    public static float drawCenteredString(String text, float x, float y, int color) {
        return FONT.drawString(text, (int) (x - getStringWidth(text) / 2f), (int) y, color);
    }

    public static int getStringWidth( String str )
    {
        return (int) FONT.getStringWidth( str );
    }

    public static int getFontHeight( )
    {
        return FONT.getFontHeight();
    }

}
