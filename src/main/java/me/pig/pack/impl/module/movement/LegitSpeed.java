package me.pig.pack.impl.module.movement;

import me.pig.pack.utils.EntityUtil;
import me.pig.pack.impl.module.Module;

@Module.Manifest( name = "LegitSpeed", desc = "", cat = Module.Category.MOVEMENT )
public class LegitSpeed extends Module {

    @Override
    public void onTick(){
        if (nullCheck()) return;
        if (mc.player.isSneaking()
                && mc.player.isInWater()
                && mc.player.isInLava() && !EntityUtil.isMoving()) return;
        if (EntityUtil.isMoving()){
            mc.player.setSprinting(true);
            if (mc.player.onGround) mc.player.motionY = 0.4;
        }
    }

    @Override
    public void onDisable(){
        mc.player.setSprinting(false);
    }
}