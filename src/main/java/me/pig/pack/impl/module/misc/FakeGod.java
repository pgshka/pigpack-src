package me.pig.pack.impl.module.misc;

import me.pig.pack.impl.module.Module;
import net.minecraft.world.GameType;

@Module.Manifest( name = "FakeGod", desc = "fake gamemode?", cat = Module.Category.MISC )
public class FakeGod extends Module {
    @Override
    public void onEnable(){
        mc.playerController.setGameType(GameType.CREATIVE);
    }
    @Override
    public void onDisable(){
        mc.playerController.setGameType(GameType.SURVIVAL);
    }
}