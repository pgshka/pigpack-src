package me.pig.pack.api.managment;

import me.pig.pack.impl.module.Module;
import me.pig.pack.impl.module.combat.AntiAim;
import me.pig.pack.impl.module.combat.AntiBot;
import me.pig.pack.impl.module.combat.Aura;
import me.pig.pack.impl.module.combat.AutoAim;
import me.pig.pack.impl.module.hud.Coords;
import me.pig.pack.impl.module.hud.ModuleList;
import me.pig.pack.impl.module.hud.TargetHud;
import me.pig.pack.impl.module.hud.Watermark;
import me.pig.pack.impl.module.misc.*;
import me.pig.pack.impl.module.movement.*;
import me.pig.pack.impl.module.render.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleManager {

    private final List<Module> modules;

    public ModuleManager( ) {
        modules = Arrays.asList(
                //CLIENT
                new ClickGui( ),
                new AntiBot( ),
                new FastFall(),
                new MCF(),
                new GuiMove(),
                new TickShift(),
                new Player(),
                new ExplosionNotify(),
                new InstantMove(),
                new NoRotate(),
                new AutoAim(),
                new BlockOutline(),
                new NameTags(),
                new AntiAfk(),
                new CustomFov(),
                new FullBright(),
                new NameProtect(),
                new Ambience(),
                new HighJump(),
                new Spider(),
                new RustXray(),
                new BotESP(),
                new BulletTracker(),
                new Sprint(),
                new WaterUp(),
                new LegitSpeed(),
                new NoRender(),
                new NoPush(),
                new PiarlTricker(),
                new ViewModel(),
                new Tracers(),
                new PacketFly(),
                new AntiAim(),
                new Notifications(),
                new FreeCam(),
                new Trails(),
                new ViewClip(),
                new NoInterp(),
                new ChestESP(),
                new RustMeEsp(),
                new Aura(),
                new Coords(),
                new Watermark(),
                new TargetHud(),
                new ModuleList(),

                new Chams(),
                new AutoPeek()
                );

        modules.sort( Comparator.comparing( Module::getName ) );
    }

    public List<Module> get( ) {return modules;}

    public Module get( String name ) {
        return modules.stream( ).filter( module -> module.getName( ).equalsIgnoreCase( name ) ).findAny( ).orElse( null );
    }

    public <T extends Module> T get(Class<T> clazz) {
        return (T) modules.stream( ).filter( module -> module.getClass( ).equals( clazz ) ).findAny( ).orElse( null );
    }

    public List<Module> get( Module.Category category ) {
        return modules.stream( ).filter( module -> module.getCategory( ).equals( category ) ).collect( Collectors.toList( ) );
    }

}
