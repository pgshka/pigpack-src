package me.pig.pack.utils;

import me.pig.pack.api.Globals;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Objects;

public class RenderUtil implements Globals {
    public static void glScissor(int x, int y, int width, int height) {
        Minecraft mc = Minecraft.getMinecraft( );
        ScaledResolution resolution = new ScaledResolution( mc );
        int scale = resolution.getScaleFactor( );

        int scissorWidth = width * scale;
        int scissorHeight = height * scale;
        int scissorX = x * scale;
        int scissorY = mc.displayHeight - scissorHeight - ( y * scale );

        GL11.glScissor( scissorX, scissorY, scissorWidth, scissorHeight );
    }

    public static void drawRect( double left, double top, double right, double bottom, int color ) {
        float red = ( float ) ( color >> 16 & 0xFF ) / 255.0f;
        float green = ( float ) ( color >> 8 & 0xFF ) / 255.0f;
        float blue = ( float ) ( color & 0xFF ) / 255.0f;
        float alpha = ( float ) ( color >> 24 & 0xFF ) / 255.0f;
        GlStateManager.disableTexture2D( );
        GlStateManager.enableBlend( );
        GlStateManager.disableAlpha( );
        GlStateManager.tryBlendFuncSeparate( GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO );
        GlStateManager.shadeModel( 7425 );
        GL11.glEnable( 2848 );
        GL11.glHint( 3154, 4354 );
        Tessellator tessellator = Tessellator.getInstance( );
        BufferBuilder bufferbuilder = tessellator.getBuffer( );
        bufferbuilder.begin( 7, DefaultVertexFormats.POSITION_COLOR );
        bufferbuilder.pos( left, top, 0 ).color( red, green, blue, alpha ).endVertex( );
        bufferbuilder.pos( left, bottom, 0 ).color( red, green, blue, alpha ).endVertex( );
        bufferbuilder.pos( right, bottom, 0 ).color( red, green, blue, alpha ).endVertex( );
        bufferbuilder.pos( right, top, 0 ).color( red, green, blue, alpha ).endVertex( );
        tessellator.draw( );
        GL11.glDisable( 2848 );
        GlStateManager.shadeModel( 7424 );
        GlStateManager.disableBlend( );
        GlStateManager.enableAlpha( );
        GlStateManager.enableTexture2D( );
    }

    public static void drawRoundedRect( double left, double top, double right, double bottom, double smooth, Color color ) {
        drawRect( left + smooth, top, right - smooth, bottom, color.getRGB( ) );
        drawRect( left, top + smooth, right, bottom - smooth, color.getRGB( ) );
        drawPolygon( ( int ) left + smooth, ( int ) top + smooth, smooth, 360, color.getRGB( ) );
        drawPolygon( ( int ) right - smooth, ( int ) top + smooth, smooth, 360, color.getRGB( ) );
        drawPolygon( ( int ) right - smooth, ( int ) bottom - smooth, smooth, 360, color.getRGB( ) );
        drawPolygon( ( int ) left + smooth, ( int ) bottom - smooth, smooth, 360, color.getRGB( ) );
    }

    public static void drawVGradientRect( float left, float top, float right, float bottom, int startColor, int endColor ) {
        float f = ( float ) ( startColor >> 24 & 255 ) / 255.0F;
        float f1 = ( float ) ( startColor >> 16 & 255 ) / 255.0F;
        float f2 = ( float ) ( startColor >> 8 & 255 ) / 255.0F;
        float f3 = ( float ) ( startColor & 255 ) / 255.0F;
        float f4 = ( float ) ( endColor >> 24 & 255 ) / 255.0F;
        float f5 = ( float ) ( endColor >> 16 & 255 ) / 255.0F;
        float f6 = ( float ) ( endColor >> 8 & 255 ) / 255.0F;
        float f7 = ( float ) ( endColor & 255 ) / 255.0F;
        GlStateManager.disableTexture2D( );
        GlStateManager.enableBlend( );
        GlStateManager.disableAlpha( );
        GlStateManager.tryBlendFuncSeparate( GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO );
        GlStateManager.shadeModel( 7425 );
        Tessellator tessellator = Tessellator.getInstance( );
        BufferBuilder bufferbuilder = tessellator.getBuffer( );
        bufferbuilder.begin( 7, DefaultVertexFormats.POSITION_COLOR );
        bufferbuilder.pos( right, top, 0 ).color( f1, f2, f3, f ).endVertex( );
        bufferbuilder.pos( left, top, 0 ).color( f1, f2, f3, f ).endVertex( );
        bufferbuilder.pos( left, bottom, 0 ).color( f5, f6, f7, f4 ).endVertex( );
        bufferbuilder.pos( right, bottom, 0 ).color( f5, f6, f7, f4 ).endVertex( );
        tessellator.draw( );
        GlStateManager.shadeModel( 7424 );
        GlStateManager.disableBlend( );
        GlStateManager.enableAlpha( );
        GlStateManager.enableTexture2D( );
    }

    public static void drawHGradientRect( float left, float top, float right, float bottom, int startColor, int endColor ) {
        float f = ( float ) ( startColor >> 24 & 255 ) / 255.0F;
        float f1 = ( float ) ( startColor >> 16 & 255 ) / 255.0F;
        float f2 = ( float ) ( startColor >> 8 & 255 ) / 255.0F;
        float f3 = ( float ) ( startColor & 255 ) / 255.0F;
        float f4 = ( float ) ( endColor >> 24 & 255 ) / 255.0F;
        float f5 = ( float ) ( endColor >> 16 & 255 ) / 255.0F;
        float f6 = ( float ) ( endColor >> 8 & 255 ) / 255.0F;
        float f7 = ( float ) ( endColor & 255 ) / 255.0F;
        GlStateManager.disableTexture2D( );
        GlStateManager.enableBlend( );
        GlStateManager.disableAlpha( );
        GlStateManager.tryBlendFuncSeparate( GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO );
        GlStateManager.shadeModel( 7425 );
        Tessellator tessellator = Tessellator.getInstance( );
        BufferBuilder bufferbuilder = tessellator.getBuffer( );
        bufferbuilder.begin( 7, DefaultVertexFormats.POSITION_COLOR );
        bufferbuilder.pos( left, top, 0 ).color( f1, f2, f3, f4 ).endVertex( );
        bufferbuilder.pos( left, bottom, 0 ).color( f1, f2, f3, f4 ).endVertex( );
        bufferbuilder.pos( right, bottom, 0 ).color( f5, f6, f7, f4 ).endVertex( );
        bufferbuilder.pos( right, top, 0 ).color( f5, f6, f7, f4 ).endVertex( );
        tessellator.draw( );
        GlStateManager.shadeModel( 7424 );
        GlStateManager.disableBlend( );
        GlStateManager.enableAlpha( );
        GlStateManager.enableTexture2D( );
    }

    public static void drawOutline( double left, double top, double right, double bottom, float t, int color ) {
        float red = ( float ) ( color >> 16 & 0xFF ) / 255.0f;
        float green = ( float ) ( color >> 8 & 0xFF ) / 255.0f;
        float blue = ( float ) ( color & 0xFF ) / 255.0f;
        float alpha = ( float ) ( color >> 24 & 0xFF ) / 255.0f;
        GlStateManager.pushMatrix( );
        GlStateManager.disableTexture2D( );
        GlStateManager.enableBlend( );
        GlStateManager.disableAlpha( );
        GlStateManager.tryBlendFuncSeparate( 770, 771, 1, 0 );
        GlStateManager.shadeModel( 7425 );
        GL11.glLineWidth( t );
        GL11.glEnable( 2848 );
        GL11.glHint( 3154, 4354 );
        Tessellator tessellator = Tessellator.getInstance( );
        BufferBuilder bufferbuilder = tessellator.getBuffer( );
        bufferbuilder.begin( 2, DefaultVertexFormats.POSITION_COLOR );
        bufferbuilder.pos( right, top, 0 ).color( red, green, blue, alpha ).endVertex( );
        bufferbuilder.pos( left, top, 0 ).color( red, green, blue, alpha ).endVertex( );
        bufferbuilder.pos( left, bottom, 0 ).color( red, green, blue, alpha ).endVertex( );
        bufferbuilder.pos( right, bottom, 0 ).color( red, green, blue, alpha ).endVertex( );
        tessellator.draw( );
        GlStateManager.shadeModel( 7424 );
        GL11.glDisable( 2848 );
        GlStateManager.disableBlend( );
        GlStateManager.enableAlpha( );
        GlStateManager.enableTexture2D( );
        GlStateManager.popMatrix( );
    }

    public static void drawLine( float x, float y, float x1, float y1, float t, int color ) {
        float red = ( color >> 16 & 0xFF ) / 255.0F;
        float green = ( color >> 8 & 0xFF ) / 255.0F;
        float blue = ( color & 0xFF ) / 255.0F;
        float alpha = ( color >> 24 & 0xFF ) / 255.0F;

        GlStateManager.pushMatrix( );
        GlStateManager.disableTexture2D( );
        GlStateManager.enableBlend( );
        GlStateManager.disableAlpha( );
        GlStateManager.tryBlendFuncSeparate( 770, 771, 1, 0 );
        GlStateManager.shadeModel( GL11.GL_SMOOTH );
        GL11.glLineWidth( t );
        GL11.glEnable( GL11.GL_LINE_SMOOTH );
        GL11.glHint( GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST );
        Tessellator tessellator = Tessellator.getInstance( );
        BufferBuilder bufferbuilder = tessellator.getBuffer( );
        bufferbuilder.begin( GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR );
        bufferbuilder.pos( x, y, 0 ).color( red, green, blue, alpha ).endVertex( );
        bufferbuilder.pos( x1, y1, 0 ).color( red, green, blue, alpha ).endVertex( );
        tessellator.draw( );
        GlStateManager.shadeModel( GL11.GL_FLAT );
        GL11.glDisable( GL11.GL_LINE_SMOOTH );
        GlStateManager.disableBlend( );
        GlStateManager.enableAlpha( );
        GlStateManager.enableTexture2D( );
        GlStateManager.popMatrix( );
    }

    public static void drawRoundedOutline( float left, float top, float right, float bottom, int smooth, float t, Color color ) {
        drawLine( left + smooth, top, right - smooth, top, t, color.getRGB( ) );
        drawLine( right, top + smooth, right, bottom - smooth, t, color.getRGB( ) );
        drawLine( left + smooth, bottom, right - smooth, bottom, t, color.getRGB( ) );
        drawLine( left, top + smooth, left, bottom - smooth, t, color.getRGB( ) );
        //( int ) left + smooth, ( int ) top + smooth, smooth
        drawPolygonPartOutline( left + smooth, top + smooth, smooth, 0, t, color.getRGB( ) );
        drawPolygonPartOutline( right - smooth, top + smooth, smooth, 3, t, color.getRGB( ) );
        drawPolygonPartOutline( right - smooth,  bottom - smooth, smooth, 2, t, color.getRGB( ) );
        drawPolygonPartOutline(  left + smooth, bottom - smooth, smooth, 1, t, color.getRGB( ) );
    }


    public static void drawPolygonOutline( double x, double y, double lineWidth, int radius, int sides, int color ) {
        float alpha = ( float ) ( color >> 24 & 255 ) / 255.0F;
        float red = ( float ) ( color >> 16 & 255 ) / 255.0F;
        float green = ( float ) ( color >> 8 & 255 ) / 255.0F;
        float blue = ( float ) ( color & 255 ) / 255.0F;
        GlStateManager.pushMatrix( );
        GlStateManager.disableTexture2D( );
        GlStateManager.enableBlend( );
        GlStateManager.disableAlpha( );
        GlStateManager.tryBlendFuncSeparate( 770, 771, 1, 0 );
        GlStateManager.shadeModel( 7425 );
        GlStateManager.glLineWidth( ( float ) lineWidth );
        final Tessellator tessellator = Tessellator.getInstance( );
        final BufferBuilder bufferbuilder = tessellator.getBuffer( );
        bufferbuilder.begin( 2, DefaultVertexFormats.POSITION_COLOR );
        final double TWICE_PI = Math.PI * 2;
        for ( int i = 0; i <= sides; i++ ) {
            double angle = ( TWICE_PI * i / sides ) + Math.toRadians( 180 );
            bufferbuilder.pos( x + Math.sin( angle ) * radius, y + Math.cos( angle ) * radius, 0 ).color( red, green, blue, alpha ).endVertex( );
        }
        tessellator.draw( );
        GlStateManager.shadeModel( 7424 );
        GL11.glDisable( 2848 );
        GlStateManager.disableBlend( );
        GlStateManager.enableAlpha( );
        GlStateManager.enableTexture2D( );
        GlStateManager.popMatrix( );
    }

    public static void drawPolygon( double x, double y, double radius, int sides, int color ) {
        float alpha = ( float ) ( color >> 24 & 255 ) / 255.0F;
        float red = ( float ) ( color >> 16 & 255 ) / 255.0F;
        float green = ( float ) ( color >> 8 & 255 ) / 255.0F;
        float blue = ( float ) ( color & 255 ) / 255.0F;
        GL11.glEnable( GL11.GL_BLEND );
        GL11.glDisable( GL11.GL_TEXTURE_2D );
        GL11.glBlendFunc( GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA );
        final Tessellator tessellator = Tessellator.getInstance( );
        final BufferBuilder bufferbuilder = tessellator.getBuffer( );
        bufferbuilder.begin( GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR );
        bufferbuilder.pos( x, y, 0 ).color( red, green, blue, alpha ).endVertex( );
        final double TWICE_PI = Math.PI * 2;
        for ( int i = 0; i <= sides; i++ ) {
            double angle = ( TWICE_PI * i / sides ) + Math.toRadians( 180 );
            bufferbuilder.pos( x + Math.sin( angle ) * radius, y + Math.cos( angle ) * radius, 0 ).color( red, green, blue, alpha ).endVertex( );
        }
        tessellator.draw( );
        GL11.glEnable( GL11.GL_TEXTURE_2D );
        GL11.glDisable( GL11.GL_BLEND );
    }

    public static void drawPolygonPart( double x, double y, double radius, int part, int color, int endcolor ) {
        float alpha = ( float ) ( color >> 24 & 255 ) / 255.0F;
        float red = ( float ) ( color >> 16 & 255 ) / 255.0F;
        float green = ( float ) ( color >> 8 & 255 ) / 255.0F;
        float blue = ( float ) ( color & 255 ) / 255.0F;
        float alpha1 = ( float ) ( endcolor >> 24 & 255 ) / 255.0F;
        float red1 = ( float ) ( endcolor >> 16 & 255 ) / 255.0F;
        float green1 = ( float ) ( endcolor >> 8 & 255 ) / 255.0F;
        float blue1 = ( float ) ( endcolor & 255 ) / 255.0F;
        GlStateManager.disableTexture2D( );
        GlStateManager.enableBlend( );
        GlStateManager.disableAlpha( );
        GlStateManager.tryBlendFuncSeparate( GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO );
        GlStateManager.shadeModel( 7425 );
        final Tessellator tessellator = Tessellator.getInstance( );
        final BufferBuilder bufferbuilder = tessellator.getBuffer( );
        bufferbuilder.begin( GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR );
        bufferbuilder.pos( x, y, 0 ).color( red, green, blue, alpha ).endVertex( );
        final double TWICE_PI = Math.PI * 2;
        for ( int i = part * 90; i <= part * 90 + 90; i++ ) {
            double angle = ( TWICE_PI * i / 360 ) + Math.toRadians( 180 );
            bufferbuilder.pos( x + Math.sin( angle ) * radius, y + Math.cos( angle ) * radius, 0 ).color( red1, green1, blue1, alpha1 ).endVertex( );
        }
        tessellator.draw( );
        GlStateManager.shadeModel( 7424 );
        GlStateManager.disableBlend( );
        GlStateManager.enableAlpha( );
        GlStateManager.enableTexture2D( );
    }


    public static void drawPolygonPartOutline( double x, double y, int radius, int part, float t, int color ) {
        float alpha = ( float ) ( color >> 24 & 255 ) / 255.0F;
        float red = ( float ) ( color >> 16 & 255 ) / 255.0F;
        float green = ( float ) ( color >> 8 & 255 ) / 255.0F;
        float blue = ( float ) ( color & 255 ) / 255.0F;
        GlStateManager.pushMatrix( );
        GlStateManager.disableTexture2D( );
        GlStateManager.enableBlend( );
        GlStateManager.disableAlpha( );
        GlStateManager.tryBlendFuncSeparate( 770, 771, 1, 0 );
        GlStateManager.shadeModel( 7425 );
        GlStateManager.glLineWidth( t );
        final Tessellator tessellator = Tessellator.getInstance( );
        final BufferBuilder bufferbuilder = tessellator.getBuffer( );
        bufferbuilder.begin( GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR );
        final double TWICE_PI = Math.PI * 2;
        for ( int i = part * 90; i <= part * 90 + 90; i++ ) {
            double angle = ( TWICE_PI * i / 360 ) + Math.toRadians( 180 );
            bufferbuilder.pos( x + Math.sin( angle ) * radius, y + Math.cos( angle ) * radius, 0 ).color( red, green, blue, alpha ).endVertex( );
        }
        tessellator.draw( );
        GlStateManager.shadeModel( 7424 );
        GL11.glDisable( 2848 );
        GlStateManager.disableBlend( );
        GlStateManager.enableAlpha( );
        GlStateManager.enableTexture2D( );
        GlStateManager.popMatrix( );
    }

    private static ICamera camera = new Frustum( );

    public static void prepare ( ) {
        GlStateManager.pushMatrix( );
        GlStateManager.disableDepth( );
        GlStateManager.disableLighting( );
        GlStateManager.depthMask( false );
        GlStateManager.disableAlpha( );
        GlStateManager.disableCull( );
        GlStateManager.enableBlend( );
        GL11.glDisable( 3553 );
        GL11.glEnable( 2848 );
        GL11.glBlendFunc( 770, 771 );
    }
    private static void colorVertex(double x, double y, double z, Color color, int alpha, BufferBuilder bufferbuilder) {
        bufferbuilder.pos(x - mc.getRenderManager().viewerPosX, y - mc.getRenderManager().viewerPosY, z - mc.getRenderManager().viewerPosZ).color(color.getRed(), color.getGreen(), color.getBlue(), alpha).endVertex();
    }

    public static void drawBoundingBox(AxisAlignedBB bb, double width, Color color, int alpha) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.glLineWidth((float) width);
        color.getRGB();
        bufferbuilder.begin(GL11.GL_LINE_STRIP, DefaultVertexFormats.POSITION_COLOR);
        colorVertex(bb.minX, bb.minY, bb.minZ, color, color.getAlpha(), bufferbuilder);
        colorVertex(bb.minX, bb.minY, bb.maxZ, color, color.getAlpha(), bufferbuilder);
        colorVertex(bb.maxX, bb.minY, bb.maxZ, color, color.getAlpha(), bufferbuilder);
        colorVertex(bb.maxX, bb.minY, bb.minZ, color, color.getAlpha(), bufferbuilder);
        colorVertex(bb.minX, bb.minY, bb.minZ, color, color.getAlpha(), bufferbuilder);
        colorVertex(bb.minX, bb.maxY, bb.minZ, color, alpha, bufferbuilder);
        colorVertex(bb.minX, bb.maxY, bb.maxZ, color, alpha, bufferbuilder);
        colorVertex(bb.minX, bb.minY, bb.maxZ, color, color.getAlpha(), bufferbuilder);
        colorVertex(bb.maxX, bb.minY, bb.maxZ, color, color.getAlpha(), bufferbuilder);
        colorVertex(bb.maxX, bb.maxY, bb.maxZ, color, alpha, bufferbuilder);
        colorVertex(bb.minX, bb.maxY, bb.maxZ, color, alpha, bufferbuilder);
        colorVertex(bb.maxX, bb.maxY, bb.maxZ, color, alpha, bufferbuilder);
        colorVertex(bb.maxX, bb.maxY, bb.minZ, color, alpha, bufferbuilder);
        colorVertex(bb.maxX, bb.minY, bb.minZ, color, color.getAlpha(), bufferbuilder);
        colorVertex(bb.maxX, bb.maxY, bb.minZ, color, alpha, bufferbuilder);
        colorVertex(bb.minX, bb.maxY, bb.minZ, color, alpha, bufferbuilder);
        tessellator.draw();
    }

    public static AxisAlignedBB fixBB(final AxisAlignedBB bb) {
        return new AxisAlignedBB(bb.minX - mc.getRenderManager().viewerPosX, bb.minY - mc.getRenderManager().viewerPosY, bb.minZ - mc.getRenderManager().viewerPosZ, bb.maxX - mc.getRenderManager().viewerPosX, bb.maxY - mc.getRenderManager().viewerPosY, bb.maxZ - mc.getRenderManager().viewerPosZ);
    }

    public static void drawBlockOutline(final AxisAlignedBB bb, final Color color, final float linewidth) {
        final float red = color.getRed() / Float.intBitsToFloat(Float.floatToIntBits(0.010800879f) ^ 0x7F4FF62C);
        final float green = color.getGreen() / Float.intBitsToFloat(Float.floatToIntBits(0.013595752f) ^ 0x7F21C0B8);
        final float blue = color.getBlue() / Float.intBitsToFloat(Float.floatToIntBits(0.014829914f) ^ 0x7F0DF92B);
        final float alpha = Float.intBitsToFloat(Float.floatToIntBits(5.635761f) ^ 0x7F345827);
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(linewidth);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GL11.glDisable(2848);
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }


    public static void drawBlockOutline(final BlockPos pos, final Color color, final float linewidth, final boolean air, final double height) {
        final IBlockState iblockstate = mc.world.getBlockState(pos);
        if (!air) {
            if (iblockstate.getMaterial() == Material.AIR) {
                return;
            }
        }
        if (mc.world.getWorldBorder().contains(pos)) {
            final AxisAlignedBB blockAxis = new AxisAlignedBB(pos.getX() - mc.getRenderManager().viewerPosX, pos.getY() - mc.getRenderManager().viewerPosY, pos.getZ() - mc.getRenderManager().viewerPosZ, pos.getX() + 1 - mc.getRenderManager().viewerPosX, pos.getY() + 1 - mc.getRenderManager().viewerPosY + height, pos.getZ() + 1 - mc.getRenderManager().viewerPosZ);
            drawBlockOutline(blockAxis.grow(Double.longBitsToDouble(Double.doubleToLongBits(3177.4888695024906) ^ 0x7FC8B0B7AD1A7A6BL)), color, linewidth);
        }
    }

    public static void drawBoxESP ( final BlockPos pos, final Color color, final float lineWidth, final boolean outline, final boolean box, final int boxAlpha, final int outlineAlpha, final float height ) {
        final AxisAlignedBB bb = new AxisAlignedBB( pos.getX( ) - mc.getRenderManager( ).viewerPosX, pos.getY( ) - mc.getRenderManager( ).viewerPosY, pos.getZ( ) - mc.getRenderManager( ).viewerPosZ, pos.getX( ) + 1 - mc.getRenderManager( ).viewerPosX, pos.getY( ) + height - mc.getRenderManager( ).viewerPosY, pos.getZ( ) + 1 - mc.getRenderManager( ).viewerPosZ );
        camera.setPosition( Objects.requireNonNull( mc.getRenderViewEntity( ) ).posX, mc.getRenderViewEntity( ).posY, mc.getRenderViewEntity( ).posZ );
        if ( camera.isBoundingBoxInFrustum( new AxisAlignedBB( pos ) ) ) {
            GlStateManager.pushMatrix( );
            GlStateManager.enableBlend( );
            GlStateManager.disableDepth( );
            GlStateManager.tryBlendFuncSeparate( 770, 771, 0, 1 );
            GlStateManager.disableTexture2D( );
            GlStateManager.depthMask( false );
            GL11.glEnable( 2848 );
            GL11.glHint( 3154, 4354 );
            GL11.glLineWidth( lineWidth );
            if ( box ) {
                RenderGlobal.renderFilledBox( bb, color.getRed( ) / 255.0f, color.getGreen( ) / 255.0f, color.getBlue( ) / 255.0f, boxAlpha / 255.0f );
            }
            if ( outline ) {
                RenderGlobal.drawBoundingBox( bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, color.getRed( ) / 255.0f, color.getGreen( ) / 255.0f, color.getBlue( ) / 255.0f, outlineAlpha / 255.0f );
            }
            GL11.glDisable( 2848 );
            GlStateManager.depthMask( true );
            GlStateManager.enableDepth( );
            GlStateManager.enableTexture2D( );
            GlStateManager.disableBlend( );
            GlStateManager.popMatrix( );
        }
    }

    public static void drawBoxESP ( final AxisAlignedBB pos, final Color color, final float lineWidth, final boolean outline, final boolean box, final int boxAlpha, final int outlineAlpha ) {
        final AxisAlignedBB bb = new AxisAlignedBB( pos.minX - mc.getRenderManager( ).viewerPosX, pos.minY - mc.getRenderManager( ).viewerPosY, pos.minZ - mc.getRenderManager( ).viewerPosZ, pos.maxX - mc.getRenderManager( ).viewerPosX, pos.maxY - mc.getRenderManager( ).viewerPosY, pos.maxZ - mc.getRenderManager( ).viewerPosZ );
        camera.setPosition( Objects.requireNonNull( mc.getRenderViewEntity( ) ).posX, mc.getRenderViewEntity( ).posY, mc.getRenderViewEntity( ).posZ );
        if ( camera.isBoundingBoxInFrustum( pos ) ) {
            GlStateManager.pushMatrix( );
            GlStateManager.enableBlend( );
            GlStateManager.disableDepth( );
            GlStateManager.tryBlendFuncSeparate( 770, 771, 0, 1 );
            GlStateManager.disableTexture2D( );
            GlStateManager.depthMask( false );
            GL11.glEnable( 2848 );
            GL11.glHint( 3154, 4354 );
            GL11.glLineWidth( lineWidth );
            if ( box ) {
                RenderGlobal.renderFilledBox( bb, color.getRed( ) / 255.0f, color.getGreen( ) / 255.0f, color.getBlue( ) / 255.0f, boxAlpha / 255.0f );
            }
            if ( outline ) {
                RenderGlobal.drawBoundingBox( bb.minX, bb.minY, bb.minZ, bb.maxX, bb.maxY, bb.maxZ, color.getRed( ) / 255.0f, color.getGreen( ) / 255.0f, color.getBlue( ) / 255.0f, outlineAlpha / 255.0f );
            }
            GL11.glDisable( 2848 );
            GlStateManager.depthMask( true );
            GlStateManager.enableDepth( );
            GlStateManager.enableTexture2D( );
            GlStateManager.disableBlend( );
            GlStateManager.popMatrix( );
        }
    }

    public static void drawFilledBox(final AxisAlignedBB bb, final int color) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.tryBlendFuncSeparate(770, 771, 0, 1);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        final float alpha = (color >> 24 & 0xFF) / Float.intBitsToFloat(Float.floatToIntBits(0.0121679185f) ^ 0x7F385BF3);
        final float red = (color >> 16 & 0xFF) / Float.intBitsToFloat(Float.floatToIntBits(0.009070697f) ^ 0x7F6B9D43);
        final float green = (color >> 8 & 0xFF) / Float.intBitsToFloat(Float.floatToIntBits(0.013924689f) ^ 0x7F1B2461);
        final float blue = (color & 0xFF) / Float.intBitsToFloat(Float.floatToIntBits(0.067761265f) ^ 0x7EF5C66B);
        final Tessellator tessellator = Tessellator.getInstance();
        final BufferBuilder bufferbuilder = tessellator.getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_COLOR);
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.maxX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.minZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.minY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.maxZ).color(red, green, blue, alpha).endVertex();
        bufferbuilder.pos(bb.minX, bb.maxY, bb.minZ).color(red, green, blue, alpha).endVertex();
        tessellator.draw();
        GlStateManager.depthMask(true);
        GlStateManager.enableDepth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

}
