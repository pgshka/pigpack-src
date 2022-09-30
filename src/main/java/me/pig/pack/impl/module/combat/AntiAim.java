package me.pig.pack.impl.module.combat;

import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.module.Module;
import me.pig.pack.utils.Timer;
import loading.mixins.AccessorKeyBinding;

import java.util.Random;

@Module.Manifest( name = "AntiAim", desc = "", cat = Module.Category.COMBAT )
public class AntiAim extends Module {
    private final Setting<Boolean> client = register( "Fake", false );
    Timer timer = new Timer();
    @Override
    public void onEnable(){
        timer.reset();
    }
    @Override
    public void onTick(){
        if (nullCheck()) return;
        if (client.getValue()) {
            Random random = new Random();
            mc.player.renderYawOffset = random.nextInt(360);
            mc.player.rotationYawHead = random.nextInt(360);
        }
    }
}