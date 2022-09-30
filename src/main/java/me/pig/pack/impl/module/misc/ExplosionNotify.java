package me.pig.pack.impl.module.misc;

import loading.events.PacketEvent;
import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.module.Module;
import me.pig.pack.utils.ChatUtil;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.Manifest( name = "ExplosionNotify", desc = "", cat = Module.Category.MISC )
public class ExplosionNotify extends Module {
    private final Setting<Boolean> all = register( "All", true );

    private final Setting<Boolean> sishka = register( "C4", true );
    private final Setting<Boolean> f1 = register( "F1", true );
    private final Setting<Boolean> rocket = register( "Rocket", true );
    private final Setting<Boolean> satchel = register( "Satchel", true );
    private final Setting<Boolean> beancan = register( "Beancan", true );


    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event ) {

        if (event.getPacket() instanceof SPacketCustomSound){
            final SPacketCustomSound packet = (SPacketCustomSound) event.getPacket();
            String position = String.format(" - x: %s y: %s z: %s", packet.getX(), packet.getY(), packet.getZ());
            String name = packet.getSoundName();
            if (all.getValue())ChatUtil.sendMessage(String.format("%sSound: %s", ChatFormatting.RED, packet.getSoundName()));
            if ((name.equalsIgnoreCase("rocket.explosion.1.3p") || name.equalsIgnoreCase("rocket.explosion.2.3p")) && rocket.getValue()){
                ChatUtil.sendMessage(String.format("%sRocket Explosion %s", ChatFormatting.YELLOW, position));
            }
            if (packet.getSoundName().contains("grenade_f1.explosion") && f1.getValue()){
                ChatUtil.sendMessage(String.format("%sGrenade F1 Explosion %s", ChatFormatting.YELLOW, position));
            }
            if (packet.getSoundName().contains("beancan_grenade.explosion") && beancan.getValue()){
                ChatUtil.sendMessage(String.format("%Beancan Explosion %s", ChatFormatting.YELLOW, position));
            }
            if (packet.getSoundName().contains("satchel_charge.explosion") && satchel.getValue()){
                ChatUtil.sendMessage(String.format("%sSatchel Explosion %s", ChatFormatting.YELLOW, position));
            }
            if (packet.getSoundName().contains("timed_explosive_charge.explosion") && sishka.getValue()){
                ChatUtil.sendMessage(String.format("%sC4 Explosion %s", ChatFormatting.YELLOW, position));
            }


        }
    }
}