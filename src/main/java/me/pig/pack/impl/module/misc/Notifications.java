package me.pig.pack.impl.module.misc;

import me.pig.pack.PigPack;
import me.pig.pack.impl.module.Module;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import loading.events.ModuleEvent;
import me.pig.pack.api.setting.Setting;
import me.pig.pack.utils.ChatUtil;
import me.pig.pack.impl.module.render.ClickGui;

import java.util.ArrayList;

@Module.Manifest( name = "Notifications", desc = "", cat = Module.Category.MISC )
public class Notifications extends Module {

    private final ArrayList<Module> blacklist = new ArrayList<>( );
    private final Setting<Boolean> module = register( "Module", true );
    @Override protected void onEnable( ) {
        blacklist.clear( );
        blacklist.add( PigPack.getModuleManager().get( ClickGui.class ) );
    }

    @SubscribeEvent public void onModuleChange( ModuleEvent event ) {
        if ( !module.getValue( ) ) return;
        if ( event.getType( ) == ModuleEvent.Type.ENABLE ) {
            if ( !blacklist.contains( event.getModule( ) ) )
                ChatUtil.sendMessage( "" + ChatFormatting.WHITE + ChatFormatting.BOLD + event.getModule( ).getName( ) + " : " + ChatFormatting.GREEN + "Enabled", true );
        }
        if ( event.getType( ) == ModuleEvent.Type.DISABLE ) {
            if ( !blacklist.contains( event.getModule( ) ) )
                ChatUtil.sendMessage( "" + ChatFormatting.WHITE + ChatFormatting.BOLD + event.getModule( ).getName( ) + " : " + ChatFormatting.RED + "Disabled", true );
        }
    }
}