package loading.events;

import me.pig.pack.impl.module.Module;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ModuleEvent extends Event {

    private final Module module;
    private final Type type;

    public ModuleEvent( Module module, Type type ) {
        this.module = module;
        this.type = type;
    }

    public Module getModule( ) {return module;}

    public Type getType( ) {return type;}

    public enum Type {
        ENABLE, DISABLE
    }

}
