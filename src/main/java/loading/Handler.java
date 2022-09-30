package loading;

import me.pig.pack.PigPack;
import io.netty.buffer.Unpooled;
import loading.mixins.AccessorCPacketCustomPayload;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketCustomPayload;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import org.lwjgl.input.Keyboard;
import loading.events.PacketEvent;
import me.pig.pack.api.managment.CommandManager;
import me.pig.pack.api.Globals;
import me.pig.pack.impl.module.Module;

public class Handler implements Globals {
    @SubscribeEvent public void onTick ( TickEvent.ClientTickEvent event ) {
        if ( mc.player != null && mc.world != null )
        {
            if( event.phase == TickEvent.Phase.START )
                PigPack.getModuleManager( ).get( ).stream( ).filter( Module::isToggled ).forEach( Module::onTick );
        }
    }

    @SubscribeEvent public void onKey ( InputEvent.KeyInputEvent event ) {
        PigPack.getModuleManager( ).get( ).stream( ).filter(module -> Keyboard.getEventKey( ) != 0 && Keyboard.getEventKeyState( ) && Keyboard.getEventKey( ) == module.getKey( ) ).forEach( Module::toggle );
    }

    @SubscribeEvent public void onRenderUpdate(RenderWorldLastEvent event) {
        PigPack.getFpsManager( ).update( );
        PigPack.getModuleManager( ).get( ).stream( ).filter( Module::isToggled ).forEach(m -> m.onRender3D(event.getPartialTicks()));
    }

    @SubscribeEvent public void onPacketSent ( PacketEvent.Send event ) {
        if ( event.getPacket( ) instanceof CPacketChatMessage ) {
            CPacketChatMessage packet = event.getPacket( );
            if ( packet.getMessage( ).startsWith( CommandManager.prefix ) ) {
                PigPack.getCommandManager( ).parseCommand( packet.getMessage( ).substring( 1 ) );
                event.setCanceled( true );
            }
        }
        if (event.getPacket() instanceof FMLProxyPacket && !mc.isSingleplayer()) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketCustomPayload) {
            final CPacketCustomPayload packet = event.getPacket();
            if (packet.getChannelName().equals("MC|Brand")) {
                ((AccessorCPacketCustomPayload) event.getPacket()).setData(new PacketBuffer(Unpooled.buffer()).writeString("vanilla"));
            }
        }
    }
}
