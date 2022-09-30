package me.pig.pack.impl.module.combat;

import me.pig.pack.PigPack;
import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.module.Module;
import me.pig.pack.utils.RotationUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;

import java.util.Comparator;
import java.util.function.Predicate;

@Module.Manifest( name = "Aura", desc = "", cat = Module.Category.COMBAT )
public class Aura extends Module {
    private final Setting<Boolean> rotate = register( "Rotate", false );
    private final Setting<Boolean> onlySword = register( "Only Sword", false );
    private final Setting<Boolean> fovCheck = register( "Fov Check", false );
    private final Setting<Number> range = register( "Range", 5, 1, 6, 1);
    private final Setting<Number> fov = register( "Fov", 40, 1, 200, 1);

    public static boolean inFov(Entity en, float angle) {
        double x = en.posX - mc.player.posX;
        double z = en.posZ - mc.player.posZ;
        double yaw = Math.atan2(x, z) * 57.29577951308232D;
        yaw = -yaw;
        angle = (float)(angle * 0.5D);
        double angleDifference = ((mc.player.rotationYaw - yaw) % 360.0D + 540.0D) % 360.0D - 180.0D;
        return ((angleDifference > 0.0D) && (angleDifference < angle)) || ((-angle < angleDifference) && (angleDifference < 0.0D));
    }
    private Entity findTarget(Predicate<Entity> predicate) {
        return mc.world.loadedEntityList.stream()
                .filter(e -> !PigPack.getFriendManager().is(e.getName()) && e != mc.player)
                .filter(predicate)
                .min(Comparator.comparing(e -> mc.player.getDistanceSq(e)))
                .orElse(null);
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        Entity entity = findTarget(EntityPlayer.class::isInstance);
        if (entity != null) {
            if (mc.player.getDistance(entity) < range.getValue().floatValue()) {
                if (fovCheck.getValue() && !inFov(entity, fov.getValue().intValue())) return;
                if (onlySword.getValue() && !(mc.player.getHeldItemMainhand().getItem() instanceof ItemSword)) return;
                if (mc.player.getCooledAttackStrength(0) >= 1f) {
                    if (rotate.getValue()) {
                        float[] rot = RotationUtil.getRotation(entity, 0, 0, false);
                        mc.player.rotationYaw = rot[0];
                        mc.player.rotationPitch = rot[1];
                    }
                    mc.playerController.attackEntity(mc.player, entity);
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    mc.player.resetCooldown();
                }
            }
        }
    }
}