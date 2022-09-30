package me.pig.pack.api;

import com.google.gson.Gson;
import net.minecraft.client.Minecraft;
import me.pig.pack.PigPack;

import java.util.Objects;
import java.util.Random;

public interface Globals {
    Gson gson = new Gson( );
    Minecraft mc = Minecraft.getMinecraft( );
    String WATERMARK = PigPack.NAME;
    String VERSION = PigPack.VERSION;
    Random random = new Random();

    static int getPlayerPing( ) {
        try {
            return Objects.requireNonNull( mc.getConnection( ) ).getPlayerInfo( mc.getConnection( ).getGameProfile( ).getId( ) ).getResponseTime( );
        } catch ( Exception e ) {
            return 0;
        }
    }

    static String getPlayerName( ){
        return mc.getConnection().getGameProfile().getName();
    }

}
