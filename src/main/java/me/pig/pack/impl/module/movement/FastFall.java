package me.pig.pack.impl.module.movement;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.pig.pack.impl.module.Module;
import net.minecraft.entity.Entity;

@Module.Manifest( name = "FastFall", desc = "", cat = Module.Category.MOVEMENT )
public class FastFall extends Module {

    @Override
    public void onTick(){
        if (nullCheck()) return;
        if ( this.mc.player.isInWater() || this.mc.player.isInLava() || this.mc.player.isOnLadder()) return;
        if (mc.player.onGround)
            mc.player.motionY -= 0.15f;
    }
}