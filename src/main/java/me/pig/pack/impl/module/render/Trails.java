package me.pig.pack.impl.module.render;

import loading.mixins.AccessorRenderManager;
import me.pig.pack.PigPack;
import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.module.Module;
import me.pig.pack.utils.MathUtil;
import me.pig.pack.utils.Timer;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;

@Module.Manifest( name = "Trails", desc = "Trails", cat = Module.Category.RENDER )
public class Trails extends Module {

    public final Setting<Number> tickClear = register("Tick Clear", 5, 1, 45, 1);
    public final Setting<Number> fadeSpeed = register("Fade Speed", 0.5, 0.1, 2, 0.1);
    public final Setting<Color> color = register( "Color", new Color(255, 54, 54, 255 ), false );

    AccessorRenderManager renderManager = (AccessorRenderManager) mc.getRenderManager();

    ArrayList<BreadCrumb> bcs = new ArrayList<>();


    public static void putVertex3d(Vec3d vec) { GL11.glVertex3d(vec.x, vec.y, vec.z); }

    public Vec3d getRenderPos(double x, double y, double z) {
        x = x - renderManager.getRenderPosX();
        y = y - renderManager.getRenderPosY();
        z = z - renderManager.getRenderPosZ();
        return new Vec3d(x, y, z);
    }

    @Override
    public void onDisable() {
        bcs.clear();
    }

    @Override
    public void onRender3D(float partialTicks) {
        double interpolatedX = MathUtil.interpolate(mc.player.lastTickPosX, mc.player.posX, mc.getRenderPartialTicks());
        double interpolatedY = MathUtil.interpolate(mc.player.lastTickPosY, mc.player.posY, mc.getRenderPartialTicks());
        double interpolatedZ = MathUtil.interpolate(mc.player.lastTickPosZ, mc.player.posZ, mc.getRenderPartialTicks());
        bcs.add(new BreadCrumb(new Vec3d(interpolatedX, interpolatedY, interpolatedZ)));
        int time = tickClear.getValue().intValue() * 50;
        GL11.glPushAttrib(1048575);
        GL11.glPushMatrix();
        GL11.glDisable(3008);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glEnable(2884);
        GL11.glEnable(2848);
        GL11.glHint(3154, 4353);
        GL11.glDisable(2896);
        GL11.glLineWidth(1.2f);
        GL11.glBegin(3);
        for (int i = 0; i < bcs.size(); i++) {
            BreadCrumb crumb = bcs.get(i);
            if (crumb.getTimer().passed(time)) {
                crumb.update(bcs);
            }
            GL11.glColor4f(color.getValue().getRed() / 255.0F, color.getValue().getGreen() / 255.0f, color.getValue().getBlue() / 255.0f, (float) (crumb.getAlpha() / 255f));
            putVertex3d(getRenderPos(crumb.getVector().x, crumb.getVector().y + 0.3, crumb.getVector().z));
        }
        GL11.glEnd();
        GL11.glEnable(2896);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glDepthMask(true);
        GL11.glCullFace(1029);
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    public class BreadCrumb {

        Vec3d vector;
        Timer timer;
        double alpha;

        public BreadCrumb(Vec3d vector) {
            timer = new Timer();
            timer.reset();
            this.vector = vector;
            this.alpha = color.getValue().getAlpha();
        }

        public Timer getTimer() {
            return timer;
        }

        public Vec3d getVector() {
            return vector;
        }

        public double getAlpha() {
            return alpha;
        }

        public void update(ArrayList arrayList) {
            if (alpha <= 0) {
                alpha = 0;
                arrayList.remove(this);
            }
            this.alpha -= color.getValue().getAlpha() / fadeSpeed.getValue().floatValue() * PigPack.getFpsManager().getFrametime();
        }

    }
}
