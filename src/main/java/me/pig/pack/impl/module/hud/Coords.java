package me.pig.pack.impl.module.hud;

import me.pig.pack.utils.font.FontUtil;
import me.pig.pack.impl.module.Module;
import me.pig.pack.utils.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.pig.pack.api.setting.Setting;

import java.awt.*;

@Module.Manifest( name = "Coords", desc = "", cat = Module.Category.HUD )
public class Coords extends Module {
    public final Setting<Color> colorText = register( "Text Color", new Color( 255, 255, 255, 255 ), false );
    public final Setting<Color> color1 = register( "Color 1", new Color(109, 30, 255, 255 ), false );
    public final Setting<Color> color2 = register( "Color 2", new Color(247, 1, 255, 255 ), false );
    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Text event ) {
        if (nullCheck()) return;

        ScaledResolution sr = new ScaledResolution(mc);

        int x = 5;
        int y = sr.getScaledHeight() - 23;
        String text = String.format("XYZ: %.1f, %.1f, %.1f", mc.player.posX, mc.player.posY,mc.player.posZ);

        RenderUtil.drawRoundedRect(x,y,x + FontUtil.getStringWidth(text) + 3 + 5,y + 17, 2,new Color(0x1A0D22));

        RenderUtil.drawHGradientRect(x + 1,y + 1,x + FontUtil.getStringWidth(text) + 2 + 5,y + 3, color1.getValue().getRGB(), color2.getValue().getRGB());

        RenderUtil.drawRoundedRect(x + 1,y + 2,x + FontUtil.getStringWidth(text) + 2 + 5,y + 16, 2,new Color(0x433856));
        RenderUtil.drawRoundedRect(x + 2,y + 3,x + FontUtil.getStringWidth(text) + 1 + 5,y + 15, 2,new Color(0x2B1F38));

        FontUtil.drawString(text, x + 4, y + 5, colorText.getValue().getRGB());
    }


}