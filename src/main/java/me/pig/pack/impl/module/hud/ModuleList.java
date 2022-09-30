package me.pig.pack.impl.module.hud;

import me.pig.pack.PigPack;
import me.pig.pack.utils.font.FontUtil;
import me.pig.pack.impl.module.Module;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.pig.pack.api.setting.Setting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

@Module.Manifest( name = "ModuleList", desc = "", cat = Module.Category.HUD )
public class ModuleList extends Module {
    public final Setting<Color> colorText = register( "Text Color", new Color( 255, 255, 255, 255 ), false );
    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Text event ) {
        if (nullCheck()) return;

        ScaledResolution sr = new ScaledResolution(mc);
        ArrayList<Module> modules = PigPack.getModuleManager().get().stream().sorted(Comparator.comparing(m -> -FontUtil.getStringWidth(m.getName()))).collect(Collectors.toCollection(ArrayList::new));
        int y = 0, count = 1;
        for (Module module : modules) {
            if (!module.isToggled()) continue;

            FontUtil.drawString(module.getName(), sr.getScaledWidth() - FontUtil.getStringWidth(module.getName()) - 2, y + 1, colorText.getValue().getRGB());
            y += FontUtil.getFontHeight() + 1;
            count++;
        }
    }


}