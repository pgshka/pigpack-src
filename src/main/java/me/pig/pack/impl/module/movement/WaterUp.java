package me.pig.pack.impl.module.movement;

import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.module.Module;
import me.pig.pack.utils.EntityUtil;

@Module.Manifest( name = "WaterUp", desc = "", cat = Module.Category.MOVEMENT )
public class WaterUp extends Module {
    private final Setting<Number> speed = register( "Speed", 1,1,6,0.1 );
    @Override
    public void onTick(){
        if (nullCheck()) return;
        if ((mc.player.isInWater() || mc.player.isInLava())){
            if (mc.gameSettings.keyBindJump.isKeyDown()) mc.player.motionY = speed.getValue().floatValue() / 15;
        }
    }

}