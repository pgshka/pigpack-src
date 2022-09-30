package me.pig.pack.impl.module.hud;

import me.pig.pack.PigPack;
import me.pig.pack.api.Globals;
import me.pig.pack.utils.font.FontUtil;
import me.pig.pack.impl.module.Module;
import me.pig.pack.impl.module.combat.AutoAim;
import me.pig.pack.utils.RenderUtil;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.pig.pack.api.setting.Setting;

import java.awt.*;

@Module.Manifest( name = "Watermark", desc = "", cat = Module.Category.HUD )
public class Watermark extends Module {
    public final Setting<Color> colorText = register( "Text Color", new Color( 255, 255, 255, 255 ), false );
    public final Setting<Color> color1 = register( "Color 1", new Color(109, 30, 255, 255 ), false );
    public final Setting<Color> color2 = register( "Color 2", new Color(247, 1, 255, 255 ), false );
    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Text event ) {
        if (nullCheck()) return;
        int x = 5;
        int y = 5;
        AutoAim aimBot = (AutoAim) PigPack.getModuleManager().get(AutoAim.class);
        String text = String.format("%s %s | %sfps | %sms | %spt", PigPack.NAME, PigPack.VERSION, PigPack.getFpsManager().getFPS(), Globals.getPlayerPing(), aimBot.isToggled() ? (float)mc.getConnection( ).getPlayerInfo( mc.getConnection( ).getGameProfile( ).getId( ) ).getResponseTime( ) * 6 / 100 : "0" );
        RenderUtil.drawRoundedRect(x,y,x + FontUtil.getStringWidth(text) + 3 + 5,y + 17, 2,new Color(0x1A0D22));

        RenderUtil.drawHGradientRect(x + 1,y + 1,x + FontUtil.getStringWidth(text) + 2 + 5,y + 3, color1.getValue().getRGB(), color2.getValue().getRGB());

        RenderUtil.drawRoundedRect(x + 1,y + 2,x + FontUtil.getStringWidth(text) + 2 + 5,y + 16, 2,new Color(0x433856));
        RenderUtil.drawRoundedRect(x + 2,y + 3,x + FontUtil.getStringWidth(text) + 1 + 5,y + 15, 2,new Color(0x2B1F38));

        FontUtil.drawString(text, x + 4, y + 5, colorText.getValue().getRGB());
    }
}