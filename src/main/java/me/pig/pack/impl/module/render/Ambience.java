package me.pig.pack.impl.module.render;

import loading.events.PacketEvent;
import me.pig.pack.impl.module.Module;
import me.pig.pack.api.setting.Setting;
import net.minecraft.network.play.server.SPacketTimeUpdate;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

@Module.Manifest( name = "Ambience", desc = "", cat = Module.Category.RENDER )
public class  Ambience extends Module {
    public final Setting<Boolean> light = register( "Light", false );

    public final Setting<Color> color = register( "Color", new Color( 255, 255, 255, 255 ), false );
    public final Setting<Number> time = register( "Time", 12, 0, 24, 1 );

    @Override
    public void onTick() {
        mc.world.setWorldTime(time.getValue().intValue() * 1000);
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event){
        if (nullCheck()) return;
        if (event.getPacket() instanceof SPacketTimeUpdate) {
            event.setCanceled(true);
        }
    }

}