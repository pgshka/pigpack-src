package me.pig.pack.impl.module.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.pig.pack.api.Globals;
import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.module.Module;
import me.pig.pack.impl.module.combat.AutoAim;
import me.pig.pack.utils.ChatUtil;
import me.pig.pack.utils.RenderUtil;
import me.pig.pack.utils.Timer;
import net.minecraft.entity.Entity;

import java.awt.*;
import java.util.Random;

@Module.Manifest( name = "AutoFarm", desc = "", cat = Module.Category.MISC )
public class AutoFarm extends Module {

    @Override
    public void onRender3D(float partialTicks) {
        for (Entity entity : mc.world.getLoadedEntityList()){
            String cope = ChatFormatting.stripFormatting(entity.getName());
            if (cope.equalsIgnoreCase("\u2739") || cope.equalsIgnoreCase("\u2718")) {
                //RenderUtil.drawBoxESP( mc.world.getBlockState(entity.getPosition()).getSelectedBoundingBox(mc.world, entity.getPosition()), new Color(255, 103, 103,255), 0.1f, false, true, 255, 255);
                float [] sd = AutoAim.getPredict(entity, 0);
                mc.player.rotationYaw = sd[0];
                mc.player.rotationPitch = sd[1];
                entity.setGlowing(true);
            }
        }
    }


}