package me.pig.pack;

import me.pig.pack.api.Globals;
import me.pig.pack.api.managment.*;
import me.pig.pack.impl.Discord;
import me.pig.pack.impl.module.Module;
import me.pig.pack.impl.ui.interwebz.Screen;
import loading.Handler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.Display;

public class PigPack {

    public static String NAME = "PigPack";
    public static String VERSION = "2.2-1";
    private static ModuleManager moduleManager;
    private static SettingManager settingManager;
    private static FriendManager friendManager;
    private static ConfigManager configManager;
    private static CommandManager commandManager;
    private static FpsManager fpsManager;
    private static Screen clickGui;
    public static float TIMER_VALUE = 1;

    private static final Thread artificialTick = new Thread( ( ) -> {while ( true ) {
        try {
            if ( Minecraft.getMinecraft( ).player != null ) moduleManager.get( ).stream( ).filter( Module::isToggled ).forEach( Module::onArtificialTick );
            Thread.sleep( 10 );
        } catch ( Exception ignored ) {}
    }
    } );

    public static void load() {
        Display.setTitle("PigPack Loading...");

        fpsManager = new FpsManager();
        settingManager = new SettingManager();
        friendManager = new FriendManager();
        moduleManager = new ModuleManager();
        clickGui = new Screen();
        commandManager = new CommandManager();
        configManager = new ConfigManager();

        artificialTick.start();
        MinecraftForge.EVENT_BUS.register(new Handler());
        Runtime.getRuntime().addShutdownHook(configManager);
        Discord.start();
        Display.setTitle("Minecraft 1.12.2");
        configManager.load();
    }

    public static ConfigManager getConfigManager( ) { return configManager; }
    public static ModuleManager getModuleManager( ) {return moduleManager;}
    public static SettingManager getSettingManager( ) {return settingManager;}
    public static FriendManager getFriendManager( ) {return friendManager;}
    public static CommandManager getCommandManager( ) { return commandManager; }
    public static FpsManager getFpsManager( ) { return fpsManager; }

}
