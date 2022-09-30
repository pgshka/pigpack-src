package me.pig.pack.impl.module.misc;

import me.pig.pack.impl.module.Module;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.UUID;

@Module.Manifest( name = "FakePlayer", desc = "Spawns a fake player", cat = Module.Category.MISC )
public class Player extends Module {
    private EntityOtherPlayerMP player;

    @SubscribeEvent
    public void onEvent(final FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        setToggled(false);
    }


    @Override public void onEnable( ) {
        if ( mc.player == null ) {
            disable( );
            return;
        }
        if ( player == null ) {
            player = new EntityOtherPlayerMP( mc.world, new GameProfile( UUID.randomUUID( ), "Player" ) );
            player.copyLocationAndAnglesFrom( mc.player );
            player.inventory.copyInventory( mc.player.inventory );
        }
        mc.world.spawnEntity( player );
    }

    @Override public void onDisable( ) {
        if ( player != null ) {
            mc.world.removeEntity( player );
            player = null;
        }
    }
}