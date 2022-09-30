package me.pig.pack.impl.ui.interwebz.settings;

import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.ui.interwebz.Screen;
import me.pig.pack.impl.ui.base.impl.Window;
import me.pig.pack.utils.ColorUtil;
import me.pig.pack.utils.RenderUtil;
import me.pig.pack.utils.font.FontUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

import java.awt.*;

public final class ColorWindow extends Window {
    private final Setting<Color> setting;
    private boolean picker, hue, alpha;
    private Color color;

    public ColorWindow(Setting<Color> setting, double x, double y) {
        super(setting.getName().length() > 7 ? setting.getName().substring(0, 7).trim() + "..." : setting.getName() , x, y, 106, 127 + FontUtil.getFontHeight());
        this.setting = setting;
    }

    @Override public void render(int mouseX, int mouseY) {
        super.render(mouseX, mouseY);
        float[] hsb = new float[]{
                Color.RGBtoHSB( setting.getValue( ).getRed( ), setting.getValue( ).getGreen( ), setting.getValue( ).getBlue( ), null )[ 0 ],
                Color.RGBtoHSB( setting.getValue( ).getRed( ), setting.getValue( ).getGreen( ), setting.getValue( ).getBlue( ), null )[ 1 ],
                Color.RGBtoHSB( setting.getValue( ).getRed( ), setting.getValue( ).getGreen( ), setting.getValue( ).getBlue( ), null )[ 2 ]
        };

        int alphas = setting.getValue( ).getAlpha( );

        final int header = (FontUtil.getFontHeight() + 6);

            if ( picker ) {
                float restrictedX = ( float ) Math.min( Math.max( getX( ) + 3, mouseX ), getX( ) + 103 );
                float restrictedY = ( float ) Math.min( Math.max( getY( ) + header, mouseY ), getY( ) + header + 100 );

                hsb[ 1 ] = Math.max( Math.min( ( restrictedX - ( float ) getX( ) + 3 ) / 103, 1 ), 0 );
                hsb[ 2 ] = Math.max( Math.min( 1f - ( restrictedY - ( float ) ( getY() + header ) ) / 100, 1 ), 0 );
            }
            else if ( hue && !setting.isRainbow( ) ) {
                float restrictedX = ( float ) Math.min( Math.max( getX( ) + 3, mouseX ), getX( ) + 103 );

                hsb[ 0 ] = Math.min( ( restrictedX - ( float ) getX( ) + 3 ) / 103, 1 );
            }
            else if ( alpha ) {
                float restrictedX = ( float ) Math.min( Math.max( getX( ) + 3, mouseX ), getX( ) + 103 );
                alphas = ( int ) ( Math.min( 1 - ( restrictedX - ( float ) getX( ) + 2 ) / 103, 1 ) * 255 );
            }


        int selectedColor = Color.HSBtoRGB( hsb[ 0 ], 1.0f, 1.0f );

        float selectedRed = ( selectedColor >> 16 & 0xFF ) / 255.0f;
        float selectedGreen = ( selectedColor >> 8 & 0xFF ) / 255.0f;
        float selectedBlue = ( selectedColor & 0xFF ) / 255.0f;
        
        FontUtil.drawString("X", (float) (getPointW() - 4 - FontUtil.getStringWidth("X")), (float) getY() + 3, -1);

        drawPickerBase((int) (getX() + 3), (int) (getY() + header), 100, 100, selectedRed, selectedGreen, selectedBlue, alphas / 255f);

        drawHueSlider((int) (getX() + 3),
                (int) (getY() + header + 103),
                100,
                6,
                hsb[ 0 ] );

        drawAlphaSlider((int) (getX() + 3),
                (int) (getY() + header + 112),
                100,
                6,
                selectedRed,
                selectedGreen,
                selectedBlue,
                alphas / 255f );


        float xPos = (float) (getX() + 2 + hsb[ 1 ] * 100),
                yPos = (float) (( getY() + header + 95 ) - hsb[ 2 ] * 95);

        GlStateManager.pushMatrix( );
//        Gui.drawRect( ( int ) xPos - 3, ( int ) yPos + 3, ( int ) xPos + 1, ( int ) yPos - 1, Color.BLACK.getRGB( ) );
//        Gui.drawRect( ( int ) xPos - 2, ( int ) yPos + 2, ( int ) xPos, ( int ) yPos, -1 );
        RenderUtil.drawPolygonOutline( xPos - 2, yPos + 2, 2f, 3, 360, Color.black.getRGB( ) );
//        RenderUtil.drawPolygon( xPos - 2, yPos + 2, 1, 360, -1 );
        GlStateManager.popMatrix( );

        color = ColorUtil.injectAlpha( new Color( Color.HSBtoRGB( hsb[ 0 ], hsb[ 1 ], hsb[ 2 ] ) ), alphas );

        setting.setValue(color);
    }

    @Override public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        final int header = (FontUtil.getFontHeight() + 6);

        if (inArea(mouseX, mouseY, getPointW() - 6 - FontUtil.getStringWidth("<"), getY() + 3, getPointW() - 4, getY() + 3 + FontUtil.getFontHeight()) && button == 0) {
            Screen.getInstance().setColorWindow(null);
        }

        if (mouseX > getX( ) + 2 && mouseX < getX( ) + 103 && mouseY > getY( ) + header && mouseY < getY( ) + header + 100) {
            picker = true;
        }

        if (mouseX > getX( ) + 2 && mouseX < getX( ) + 103 && mouseY > getY( ) + header + 103 && mouseY < getY( ) + header + 109) {
            hue = true;
        }

        if (mouseX > getX( ) + 2 && mouseX < getX( ) + 103 && mouseY > getY( ) + header + 112 && mouseY < getY( ) + header + 118) {
            alpha = true;
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        picker = false;
        hue = false;
        alpha = false;
    }

    private void drawPickerBase(int pickerX, int pickerY, int pickerWidth, int pickerHeight, float red, float green, float blue, float alpha ) {
        GL11.glEnable( GL11.GL_BLEND );
        GL11.glDisable( GL11.GL_TEXTURE_2D );
        GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
        GL11.glShadeModel( GL11.GL_SMOOTH );
        GL11.glBegin( GL11.GL_POLYGON );
        GL11.glColor4f( 1.0f, 1.0f, 1.0f, 1.0f );
        GL11.glVertex2f( pickerX, pickerY );
        GL11.glVertex2f( pickerX, pickerY + pickerHeight );
        GL11.glColor4f( red, green, blue, alpha );
        GL11.glVertex2f( pickerX + pickerWidth, pickerY + pickerHeight );
        GL11.glVertex2f( pickerX + pickerWidth, pickerY );
        GL11.glEnd( );
        GL11.glDisable( GL11.GL_ALPHA_TEST );
        GL11.glBegin( GL11.GL_POLYGON );
        GL11.glColor4f( 0.0f, 0.0f, 0.0f, 0.0f );
        GL11.glVertex2f( pickerX, pickerY );
        GL11.glColor4f( 0.0f, 0.0f, 0.0f, 1.0f );
        GL11.glVertex2f( pickerX, pickerY + pickerHeight );
        GL11.glVertex2f( pickerX + pickerWidth, pickerY + pickerHeight );
        GL11.glColor4f( 0.0f, 0.0f, 0.0f, 0.0f );
        GL11.glVertex2f( pickerX + pickerWidth, pickerY );
        GL11.glEnd( );
        GL11.glEnable( GL11.GL_ALPHA_TEST );
        GL11.glShadeModel( GL11.GL_FLAT );
        GL11.glEnable( GL11.GL_TEXTURE_2D );
        GL11.glDisable( GL11.GL_BLEND );
    }

    private void drawHueSlider( int x, int y, int width, int height, float hue ) {
        int step = 0;

        for ( int colorIndex = 0; colorIndex < 5; colorIndex++ ) {
            int previousStep = Color.HSBtoRGB( ( float ) step / 5, 1.0f, 1.0f );
            int nextStep = Color.HSBtoRGB( ( float ) ( step + 1 ) / 5, 1.0f, 1.0f );

            RenderUtil.drawHGradientRect( x + step * ( width / 5 ), y, x + ( step + 1 ) * ( width / 5 ), y + height, previousStep, nextStep );

            step++;
        }

        int sliderMinX = ( int ) ( x + ( width * hue ) );

        RenderUtil.drawRect( sliderMinX - 1, y, sliderMinX + 1, y + height, -1 );
        RenderUtil.drawOutline( sliderMinX - 1, y, sliderMinX + 1, y + height, 1f, Color.BLACK.getRGB( ) );
    }

    private void drawAlphaSlider( int x, int y, int width, int height, float red, float green, float blue, float alpha ) {
        boolean left = true;

        int checkerBoardSquareSize = height / 2;

        for ( int squareIndex = -checkerBoardSquareSize; squareIndex < width; squareIndex += checkerBoardSquareSize ) {
            if ( !left ) {
                Gui.drawRect( x + squareIndex, y, x + squareIndex + checkerBoardSquareSize, y + height, 0xFFFFFFFF );
                Gui.drawRect( x + squareIndex, y + checkerBoardSquareSize, x + squareIndex + checkerBoardSquareSize, y + height, 0xFF909090 );

                if ( squareIndex < width - checkerBoardSquareSize ) {
                    int minX = x + squareIndex + checkerBoardSquareSize;
                    int maxX = Math.min( x + width, x + squareIndex + checkerBoardSquareSize * 2 );

                    Gui.drawRect( minX, y, maxX, y + height, 0xFF909090 );
                    Gui.drawRect( minX, y + checkerBoardSquareSize, maxX, y + height, 0xFFFFFFFF );
                }
            }

            left = !left;
        }

        drawLeftGradientRect( x, y, x + width, y + height, new Color( red, green, blue, 1 ).getRGB( ), 0 );

        int sliderMinX = ( int ) ( x + width - ( width * alpha ) );

        Gui.drawRect( sliderMinX - 1, y, sliderMinX + 1, y + height, -1 );
        RenderUtil.drawOutline( sliderMinX - 1, y, sliderMinX + 1, y + height, 1f, Color.BLACK.getRGB( ) );
    }

    private void drawLeftGradientRect( int left, int top, int right, int bottom, int startColor, int endColor ) {
        float f = ( float ) ( startColor >> 24 & 255 ) / 255.0f;
        float f1 = ( float ) ( startColor >> 16 & 255 ) / 255.0f;
        float f2 = ( float ) ( startColor >> 8 & 255 ) / 255.0f;
        float f3 = ( float ) ( startColor & 255 ) / 255.0f;
        float f4 = ( float ) ( endColor >> 24 & 255 ) / 255.0f;
        float f5 = ( float ) ( endColor >> 16 & 255 ) / 255.0f;
        float f6 = ( float ) ( endColor >> 8 & 255 ) / 255.0f;

        GlStateManager.disableTexture2D( );
        GlStateManager.enableBlend( );
        GlStateManager.disableAlpha( );
        GlStateManager.tryBlendFuncSeparate( GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO );
        GlStateManager.shadeModel( 7425 );

        Tessellator tessellator = Tessellator.getInstance( );
        BufferBuilder bufferbuilder = tessellator.getBuffer( );

        bufferbuilder.begin( 7, DefaultVertexFormats.POSITION_COLOR );
        bufferbuilder.pos( right, top, 0 ).color( f4, f5, f6, f4 ).endVertex( );
        bufferbuilder.pos( left, top, 0 ).color( f1, f2, f3, f ).endVertex( );
        bufferbuilder.pos( left, bottom, 0 ).color( f1, f2, f3, f ).endVertex( );
        bufferbuilder.pos( right, bottom, 0 ).color( f4, f5, f6, f4 ).endVertex( );

        tessellator.draw( );

        GlStateManager.shadeModel( 7424 );
        GlStateManager.disableBlend( );
        GlStateManager.enableAlpha( );
        GlStateManager.enableTexture2D( );
    }

}
