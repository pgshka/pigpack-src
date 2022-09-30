package me.pig.pack.impl.module.misc;

import me.pig.pack.impl.module.Module;

@Module.Manifest( name = "NoPush", desc = "", cat = Module.Category.MISC )
public class NoPush extends Module {

    @Override
    public void onTick(){
        mc.player.entityCollisionReduction = 1;
    }

}