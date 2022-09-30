package me.pig.pack.impl.module.movement;

import me.pig.pack.api.Globals;
import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.module.Module;
import net.minecraft.network.play.client.CPacketPlayer;

@Module.Manifest( name = "Spider", desc = "", cat = Module.Category.MOVEMENT )
public class Spider extends Module {

    public final Setting<Boolean> ground = register( "Ground", false );
    public final Setting<Boolean> packet = register( "Packet", false );

    @Override
    public void onTick(){
        if (nullCheck()) return;
        if (mc.player.motionY < 0 && mc.player.collidedHorizontally){
            mc.player.motionY = 0.2f;
            if (packet.getValue()){
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, ground.getValue()));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY - 1337, mc.player.posZ, ground.getValue()));
            }
            if (ground.getValue()) mc.getConnection().sendPacket(new CPacketPlayer(true));
        }
    }

}