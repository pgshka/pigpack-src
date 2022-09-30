package me.pig.pack.impl.module.combat;

import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.module.Module;
import me.pig.pack.utils.ChatUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.pig.pack.utils.EntityUtil;
import net.minecraft.block.BlockAir;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;

@Module.Manifest( name = "AntiBot", desc = "", cat = Module.Category.COMBAT )
public class AntiBot extends Module {
    public final Setting<Boolean> debug = register("Debug", true);
    public final Setting<Boolean> tickExistedCheck = register("TickExisted Check", true);

    public final Setting<Number> rangeCheck = register("Range", 7, 1, 25, 1);

    @Override
    public void onTick() {
        if (nullCheck()) return;
        for (int size = mc.world.playerEntities.size(), i = 0; i < mc.world.playerEntities.size()   ; ++i) {
            try {
                final EntityPlayer bot = mc.world.playerEntities.get(i);
                if (bot != mc.player && bot != null && mc.player.getDistance(bot) < rangeCheck.getValue().intValue()) {

                    if (bot.isInvisible()) {
                        mc.world.removeEntity(bot);
                    } else {
                        Item item = bot.getHeldItem(EnumHand.OFF_HAND).getItem();
                        BlockPos pos = bot.getPosition().down();
                        if (item.equals(Items.AIR) && mc.world.getBlockState(pos).getBlock() instanceof BlockAir) {
                            if (tickExistedCheck.getValue() && bot.ticksExisted > 12) return;
                            if (debug.getValue()) ChatUtil.sendMessage(bot.getName() + " : removed from world");
                            mc.world.removeEntity(bot);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}