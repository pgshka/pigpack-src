package me.pig.pack.impl.module.render;

import me.pig.pack.impl.module.Module;
import me.pig.pack.api.setting.Setting;

@Module.Manifest( name = "CustomFov", desc = "", cat = Module.Category.RENDER )
public class CustomFov extends Module {
    public final Setting<Number> fov = register( "FOV", 110, 20, 160, 1 );

    @Override
    public void onTick(){
        mc.gameSettings.fovSetting = fov.getValue().intValue();
    }
}