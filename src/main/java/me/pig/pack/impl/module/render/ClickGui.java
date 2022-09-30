package me.pig.pack.impl.module.render;

import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.ui.interwebz.Screen;
import me.pig.pack.impl.module.Module;
import org.lwjgl.input.Keyboard;

@Module.Manifest(name = "ClickGui", cat = Module.Category.RENDER, key = Keyboard.KEY_INSERT)
public class ClickGui extends Module {
    public final Setting<Boolean> mainMenu = register("Main Menu", true);

    public ClickGui() {
    }

    @Override
    protected void onEnable() {
        mc.displayGuiScreen(Screen.getInstance());
        setToggled(false);
    }

}
