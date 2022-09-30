package me.pig.pack.impl.module.render;

import me.pig.pack.PigPack;
import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.module.Module;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import me.pig.pack.utils.EntityUtil;

import java.awt.*;

import static net.minecraft.client.renderer.GlStateManager.glLineWidth;

@Module.Manifest( name = "Tracers", desc = "", cat = Module.Category.RENDER )
public class Tracers extends Module {
    public final Setting<Color> enemyColor = register( "Enemy Color", new Color( 255, 255, 255, 255 ), false );
    public final Setting<Color> friendColor = register( "Friend Color", new Color( 255, 255, 255, 255 ), false );


    @Override public void onRender3D(float partialTicks){
        mc.gameSettings.viewBobbing = false;

        for(Entity e : mc.world.loadedEntityList) {
            if (e instanceof EntityPlayer) {
                if (e != mc.player) {
                    if (!e.isInvisible()) {
                        if (!PigPack.getFriendManager().is(e.getName())) {
                            drawTracer(e, 2, enemyColor.getValue());
                        } else {
                            drawTracer(e, 2, friendColor.getValue());
                        }
                    }
                }
            }
        }
    }



    public static void drawTracer(Entity e, float lineWidth, Color col) {
        Vec3d vec = EntityUtil.getInterpolatedPos(e, mc.getRenderPartialTicks());
        double x = vec.x - mc.getRenderManager().viewerPosX;
        double y = vec.y - mc.getRenderManager().viewerPosY;
        double z = vec.z - mc.getRenderManager().viewerPosZ;

        Vec3d eyes = (new Vec3d(0.0D, 0.0D, 1.0D)).rotatePitch(-((float)Math.toRadians(mc.player.rotationPitch))).rotateYaw( -((float)Math.toRadians(mc.player.rotationYaw)));

        drawLine3D(lineWidth, eyes.x, eyes.y + mc.player.getEyeHeight(), eyes.z, x, y + (e.height / 2), z, new Color(col.getRed(), col.getGreen(), col.getBlue(), col.getAlpha()).getRGB());
    }
    public static void drawLine3D(double lineWidth, double x1, double y1, double z1, double x2, double y2, double z2, int color) {
        drawLine3D(x1, y1, z1, x2, y2, z2, color, true, lineWidth);
    }
    public static void drawLine3D(double x1, double y1, double z1, double x2, double y2, double z2, int color, boolean disableDepth, double lineWidth) {

        GL11.glDepthMask(false);
        GL11.glDisable(2929);

        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4354);
        GL11.glLineWidth(0.1F);
        float alpha = (color >> 24 & 0xFF) / 255.0F;
        float red = (color >> 16 & 0xFF) / 255.0F;
        float green = (color >> 8 & 0xFF) / 255.0F;
        float blue = (color & 0xFF) / 255.0F;
        GL11.glColor4f(red, green, blue, (alpha == 0.0F) ? 1.0F : alpha);
        glLineWidth((float) lineWidth);
        GL11.glBegin(1);
        GL11.glVertex3d(x1, y1, z1);
        GL11.glVertex3d(x2, y2, z2);
        GL11.glEnd();

        GL11.glDepthMask(true);
        GL11.glEnable(2929);

        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDisable(2848);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }


}