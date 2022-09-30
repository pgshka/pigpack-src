package me.pig.pack.impl.module.render;

import me.pig.pack.impl.module.Module;


@Module.Manifest( name = "RustXray", desc = "", cat = Module.Category.RENDER )
public class RustXray extends Module {
    public void onEnable() {
        RustXray.mc.renderGlobal.loadRenderers();
    }

    public void onDisable() {
        RustXray.mc.renderGlobal.loadRenderers();
    }
}