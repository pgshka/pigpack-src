package me.pig.pack.utils;

import me.pig.pack.api.Globals;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.function.BiFunction;

public final class RotationUtil implements Globals {
    public static float[] getNeededRotations(double d, double d2, double d3, float f, float f2, float f3) {
        double d4 = d - (double)f;
        double d5 = d3 - (double)f3;
        double d6 = d2 - (double)(f2 + mc.player.getEyeHeight());
        double d7 = MathHelper.sqrt((double)(d4 * d4 + d5 * d5));
        float f4 = (float)(Math.atan2(d5, d4) * 180.0 / Math.PI - 90.0);
        float f5 = (float)(-(Math.atan2(d6, d7) * 180.0 / Math.PI));
        f5 = MathHelper.clamp((float)f5, (float)-90.0f, (float)90.0f);
        return new float[]{f4, f5};
    }

    public static float getValue() {
        float pro;
        float result = (pro = (float) (mc.gameSettings.mouseSensitivity * 0.6 + 0.2)) * pro * pro * 8;
        return (float) (result * 0.15);

    }
    public static float getDeltaMouse(float delta) {
        return Math.round(delta / getValue());
    }
    public static float[] getRotation(Entity e, float predictValue, float aimPoint, boolean selfPredict) {
        float predicted = predictValue;
        double xDelta = e.posX + (e.posX - e.prevPosX) * predictValue - mc.player.posX - (selfPredict ? mc.player.motionX * predictValue : 0);
        double zDelta = e.posZ + (e.posZ - e.prevPosZ) * predictValue - mc.player.posZ - (selfPredict ? mc.player.motionZ * predictValue : 0);
        double diffY = e.posY + e.getEyeHeight() - (mc.player.posY + mc.player.getEyeHeight() + aimPoint);
        double distance = MathHelper.sqrt(xDelta * xDelta + zDelta * zDelta);
        float yaw = (float) ((MathHelper.atan2(zDelta, xDelta) * 180.0D / Math.PI) - 90.0F);
        float pitch = ((float) (-(MathHelper.atan2(diffY, distance) * 180.0D / Math.PI)));
        yaw = (mc.player.rotationYaw + getDeltaMouse((MathHelper.wrapDegrees(yaw - mc.player.rotationYaw))) * getValue());
        pitch = mc.player.rotationPitch + getDeltaMouse((MathHelper.wrapDegrees(pitch - mc.player.rotationPitch))) * getValue();
        pitch = MathHelper.clamp(pitch, -90F, 90F);
        return new float[]{yaw, pitch};
    }

}