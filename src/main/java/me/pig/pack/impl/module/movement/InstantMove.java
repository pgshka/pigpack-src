package me.pig.pack.impl.module.movement;

import loading.events.MoveEvent;
import me.pig.pack.impl.module.Module;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInput;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Objects;

@Module.Manifest( name = "InstantMove", desc = "", cat = Module.Category.MOVEMENT )
public class InstantMove extends Module {
    public static double getMaxSpeed() {
        double maxModifier = 0.2873;
        if (mc.player.isPotionActive((Potion) Objects.requireNonNull(Potion.getPotionById(1)))) {
            maxModifier *= 1.0 + 0.2 * (Objects.requireNonNull(mc.player.getActivePotionEffect((Potion)Objects.requireNonNull(Potion.getPotionById(1)))).getAmplifier() + 1);
        }
        return maxModifier;
    }

    @SubscribeEvent
    public void onEvent(MoveEvent event){
        if (nullCheck()) return;
        if (!mc.player.isSneaking() && !mc.player.isInWater() && !mc.player.isInLava() && (mc.player.movementInput.moveForward != 0.0f || mc.player.movementInput.moveStrafe != 0.0f)) {
            final MovementInput movementInput = mc.player.movementInput;
            float moveForward = movementInput.moveForward;
            float moveStrafe = movementInput.moveStrafe;
            float rotationYaw = mc.player.rotationYaw;
            if (moveForward == 0.0 && moveStrafe == 0.0) {
                event.motionX = 0.0;
                event.motionZ = 0.0;
            } else {
                if (moveForward != 0.0) {
                    if (moveStrafe > 0.0) {
                        rotationYaw += ((moveForward > 0.0) ? -45 : 45);
                    } else if (moveStrafe < 0.0) {
                        rotationYaw += ((moveForward > 0.0) ? 45 : -45);
                    }
                    moveStrafe = 0.0f;
                    moveForward = ((moveForward == 0.0f) ? moveForward : ((moveForward > 0.0) ? 1.0f : -1.0f));
                }
                moveStrafe = ((moveStrafe == 0.0f) ? moveStrafe : ((moveStrafe > 0.0) ? 1.0f : -1.0f));
                event.motionX = moveForward * getMaxSpeed() * Math.cos(Math.toRadians(rotationYaw + 90.0f)) + moveStrafe * getMaxSpeed() * Math.sin(Math.toRadians(rotationYaw + 90.0f));
                event.motionZ = moveForward * getMaxSpeed() * Math.sin(Math.toRadians(rotationYaw + 90.0f)) - moveStrafe * getMaxSpeed() * Math.cos(Math.toRadians(rotationYaw + 90.0f));
            }
        }
    }
}