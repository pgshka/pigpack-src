package me.pig.pack.impl.module.render;

import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.module.Module;

import java.awt.*;

@Module.Manifest(name = "Chams", cat = Module.Category.RENDER)
public final class Chams extends Module {

    public final Setting<Boolean> hidePlayer = register("Hide-Model", true);

    //Visible
    public final Setting<Boolean> visible = register("Visible", false);
    public final Setting<Color> color = register("Color", new Color(0, 255, 0, 255), false);
    public final Setting<Boolean> outline = register("Outline", true);
    public final Setting<Color> outColor = register("Outline-Color", new Color(255, 255, 255, 255), false);
    public final Setting<Number> outWidth = register("Outline-Width", 1.0, 0.1, 10.0, 0.1);
    public final Setting<Boolean> glint = register("Glint", true);
    public final Setting<Color> glintColor = register("Glint-Color", new Color(0, 255, 0, 120), false);

    //Xqz
    public final Setting<Boolean> xqz = register("XQZ", true);
    public final Setting<Color> xqzColor = register("XQZ-Color", new Color(255, 0, 0, 255), false);

}
