package me.pig.pack.api.managment;

import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.module.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SettingManager {

    private final List<Setting<?>> settings = new ArrayList<>( );

    public List<Setting<?>> get( ) {return settings;}

    public Setting<?> get( String name ) {
        return settings.stream( ).filter( setting -> setting.getName( ).equalsIgnoreCase( name ) ).findAny( ).orElse( null );
    }

    public Setting<?> get( String name, Module module ) {
        return settings.stream( ).filter( setting -> setting.getName( ).equalsIgnoreCase( name ) && setting.getParent( ).equals( module ) ).findAny( ).orElse( null );
    }

    public List<Setting<?>> get( Module module ) {
        return settings.stream( ).filter( setting -> setting.getParent( ).equals( module ) ).collect( Collectors.toList( ) );
    }

}
