package me.pig.pack.impl.module.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.pig.pack.PigPack;
import me.pig.pack.impl.module.Module;
import me.pig.pack.impl.ui.interwebz.Screen;
import me.pig.pack.utils.ChatUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import org.lwjgl.input.Mouse;

@Module.Manifest( name = "MCF", desc = "Middle Click Friends", cat = Module.Category.MISC )
public class MCF extends Module {
    boolean clicked = false;
    @Override public void onTick( ) {
        Entity entity;
        RayTraceResult result = mc.objectMouseOver;
        if (Mouse.isButtonDown(2)) {
            if (!clicked) {
                if (mc.currentScreen instanceof Screen || mc.currentScreen instanceof GuiContainer) return;
                if (result != null && result.typeOfHit == RayTraceResult.Type.ENTITY && (entity = result.entityHit) instanceof EntityPlayer) {
                    if (PigPack.getFriendManager().is(entity.getName())) {
                        PigPack.getFriendManager().del(entity.getName());
                        ChatUtil.sendMessage(ChatFormatting.RED + entity.getName() + ChatFormatting.RED + " has been unfriended.");
                    } else {
                        PigPack.getFriendManager().add(entity.getName());
                        ChatUtil.sendMessage(ChatFormatting.RED + entity.getName() + ChatFormatting.GREEN + " has been friended.");
                    }
                }
                clicked = true;
            }
            clicked = true;
        } else {
            clicked = false;
        }
    }
}