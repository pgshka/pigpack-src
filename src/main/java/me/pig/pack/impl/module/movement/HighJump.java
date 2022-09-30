package me.pig.pack.impl.module.movement;

import me.pig.pack.impl.module.Module;

@Module.Manifest( name = "HighJump", desc = "", cat = Module.Category.MOVEMENT )
public class HighJump extends Module {


    @Override
    public void onTick(){
        if (nullCheck()) return;
        if (mc.player.onGround && mc.gameSettings.keyBindJump.isKeyDown()) mc.player.motionY = 0.85f;
    }

}