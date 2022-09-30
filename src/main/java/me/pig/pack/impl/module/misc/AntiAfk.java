package me.pig.pack.impl.module.misc;

import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.module.Module;
import me.pig.pack.utils.Timer;

import java.util.Random;

@Module.Manifest( name = "AntiAfk", desc = "", cat = Module.Category.MISC )
public class AntiAfk extends Module {
    private final Setting<Boolean> jump = register( "Jump", true );
    private final Setting<Boolean> rotate = register( "Rotate", true );
    private final Setting<Number> delay = register( "Delay", 10,1,60,1 );

    Timer timer = new Timer();


    @Override
    public void onTick(){
        if (nullCheck()) return;
        if (timer.passed(delay.getValue().intValue() * 1000)){
            Random random = new Random();
            if (rotate.getValue()){
                mc.player.rotationYaw = random.nextInt(90);
                mc.player.rotationPitch = random.nextInt(90);
            }
            if (jump.getValue() && mc.player.onGround) mc.player.jump();
            timer.reset();
        }
    }
    @Override
    public void onDisable(){
        mc.player.setSprinting(false);
    }
}