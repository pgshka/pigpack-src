package me.pig.pack.impl.module;

import me.pig.pack.PigPack;
import me.pig.pack.api.Globals;
import me.pig.pack.api.setting.Setting;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.input.Keyboard;
import loading.events.ModuleEvent;

import java.awt.*;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

public abstract class Module implements Globals {

    private String name, desc;
    protected String info = "";
    private Category category;
    private boolean toggled, exception;
    private int key;
    public Setting<Boolean> drawn;

    public Module ( ) {
        Manifest manifest = getClass( ).getAnnotation( Manifest.class );
        this.name = manifest.name( );
        this.category = manifest.cat( );
        this.desc = manifest.desc( );
        this.key = manifest.key( );
        this.exception = manifest.exception( );
        this.drawn = register( "Drawn", true );
    }

    public String getName ( ) {return name;}

//    public String getInfo ( ) {
//        HUD m = ( HUD ) Main.getModuleManager( ).get( HUD.class );
//        if ( m != null ) {
//            String format;
//            switch ( m.infoClient.getValue( ) ) {
//                case "Simple":
//                    format = String.format( " %s%s", ChatFormatting.GRAY, info );
//                    break;
//                case "Default":
//                    format = String.format( " %s[%s%s%s]", ChatFormatting.GRAY, ChatFormatting.RESET, info, ChatFormatting.GRAY );
//                    break;
//                case "Comment":
//                    format = String.format( " %s//%s", ChatFormatting.GRAY, info );
//                    break;
//                default:
//                    throw new IllegalStateException( "Unexpected value: " + m.infoClient.getValue( ) );
//            }
//            return info.equalsIgnoreCase( "" ) ? "" : format;
//        } else {
//            return info.equalsIgnoreCase( "" ) ? "" : String.format( " %s[%s%s%s]", ChatFormatting.GRAY, ChatFormatting.RESET, info, ChatFormatting.GRAY );
//        }
//    }

    public Category getCategory ( ) {return category;}

    public int getKey ( ) {return key;}

    public void setKey ( int key ) {this.key = key;}

    public String getDesc ( ) {return desc;}

    public boolean isException ( ) {return exception;}

    public boolean isToggled ( ) {return toggled;}
    public void setToggled ( boolean toggled ) {
        this.toggled = toggled;
        if ( toggled ) enable( );
        else disable( );
    }

    public void toggle ( ) {
        setToggled( !toggled );
    }
    public void toggleOff ( ) {
        setToggled(false);
    }

    public void enable ( ) {
        MinecraftForge.EVENT_BUS.post( new ModuleEvent( this, ModuleEvent.Type.ENABLE ) );
        MinecraftForge.EVENT_BUS.register( this );
        onEnable( );
    }

    public void disable ( ) {
        MinecraftForge.EVENT_BUS.post( new ModuleEvent( this, ModuleEvent.Type.DISABLE ) );
        MinecraftForge.EVENT_BUS.unregister( this );
        onDisable( );
    }

    protected Setting<Boolean> register ( String name, boolean value ) {
        Setting<Boolean> setting = new Setting<>( name, this, value );
        PigPack.getSettingManager( ).get( ).add( setting );
        return setting;
    }

    protected Setting<Number> register(String name, double value, double min, double max, double inc) {
        Setting<Number> setting = new Setting<>( name, this, value, min, max, inc );
        PigPack.getSettingManager( ).get( ).add( setting );
        return setting;
    }

    protected Setting<String> register ( String name, String value, List<String> Clients ) {
        Setting<String> setting = new Setting<>( name, this, value, Clients );
        PigPack.getSettingManager( ).get( ).add( setting );
        return setting;
    }

    protected Setting<Color> register ( String name, Color value, boolean rainbow ) {
        Setting<Color> setting = new Setting<>( name, this, value, rainbow );
        PigPack.getSettingManager( ).get( ).add( setting );
        return setting;
    }

    public void onTick ( ) {}

    public void onArtificialTick ( ) {}

    protected void onEnable ( ) {}

    protected void onDisable ( ) {}

    public void onRender3D(float partialTicks){}

    public enum Category {
        RENDER("Render"),
        COMBAT("Combat"),
        MOVEMENT("Movement"),
        MISC("Misc"),
        HUD("Hud");
        private final String name;

        Category ( String name ) {
            this.name = name;
        }

        public String getName ( ) {return name;}
    }

    protected boolean nullCheck ( ) {
        return mc.world == null || mc.player == null;
    }

    @Target( ElementType.TYPE )
    @Retention( RetentionPolicy.RUNTIME )
    public @interface Manifest {
        String name ( );

        Category cat ( );

        String desc ( ) default "";

        int key ( ) default Keyboard.KEY_NONE;

        boolean exception ( ) default false;
    }

}
