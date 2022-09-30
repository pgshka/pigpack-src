package me.pig.pack.impl.module.movement;

import me.pig.pack.impl.module.Module;

@Module.Manifest( name = "Sprint", desc = "", cat = Module.Category.MOVEMENT )
public class Sprint extends Module {

    @Override
    public void onTick(){
        if (nullCheck()) return;
        if (!mc.player.isSneaking() && !mc.player.isInWater() && !mc.player.isInLava() && (mc.player.movementInput.moveForward != 0.0f || mc.player.movementInput.moveStrafe != 0.0f)) {mc.player.setSprinting(true);}
    }

    @Override
    public void onDisable(){
        mc.player.setSprinting(false);
    }
}