package me.pig.pack.impl.module.render;

import me.pig.pack.impl.module.Module;

@Module.Manifest( name = "FullBright", desc = "", cat = Module.Category.RENDER )
public class FullBright extends Module {

    @Override
    public void onTick(){
        mc.gameSettings.gammaSetting = 999 * 999;
    }
}