package me.pig.pack.impl.module.render;

import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.module.Module;

@Module.Manifest( name = "ViewModel", desc = "ViewModel", cat = Module.Category.RENDER )
public class ViewModel extends Module {
    public final Setting<Number> scaleX = register( "Scale X", 1, -3, 3, 0.1 );
    public final Setting<Number> scaleY = register( "Scale Y", 1, -3, 3, 0.1 );
    public final Setting<Number> scaleZ = register( "Scale Z", 1, -3, 3, 0.1);

    public final Setting<Number> posX = register( "Pos X", 0, -5, 5, 0.1 );

    public final Setting<Number> posY = register( "Pos Y", -0.5, -5, 5, 0.1 );

    public final Setting<Number> posZ = register( "Pos Z", 0, -5, 5, 0.1 );

}