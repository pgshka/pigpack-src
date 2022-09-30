package me.pig.pack.impl.module.render;

import loading.events.CameraEvent;
import me.pig.pack.impl.module.Module;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.Manifest( name = "ViewClip", desc = "", cat = Module.Category.RENDER )
public class ViewClip extends Module {
    @SubscribeEvent
    public void event(CameraEvent event){
        event.distance = 4;
    }
}