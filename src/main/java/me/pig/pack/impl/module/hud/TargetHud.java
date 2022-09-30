package me.pig.pack.impl.module.hud;

import me.pig.pack.PigPack;
import me.pig.pack.utils.font.FontUtil;
import me.pig.pack.impl.module.Module;
import me.pig.pack.impl.module.combat.AutoAim;
import me.pig.pack.utils.RenderUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.pig.pack.api.setting.Setting;

import java.awt.*;

@Module.Manifest( name = "TargetHud", desc = "", cat = Module.Category.HUD )
public class TargetHud extends Module {
    private float targetHealth;
    private float prevTargetHealth = targetHealth;
    public final Setting<Color> colorText = register( "Text Color", new Color( 255, 255, 255, 255 ), false );
    public final Setting<Color> color1 = register( "Color 1", new Color(109, 30, 255, 255 ), false );
    public final Setting<Color> color2 = register( "Color 2", new Color(247, 1, 255, 255 ), false );

    public final Setting<Number> xp = register("X Pos", 10, 5, 350, 1);
    public final Setting<Number> yp = register("Y Pos", 10, 5, 350, 1);

    @Override public void onTick() {
        super.onTick();
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Text event ) {
        if (nullCheck()) return;
        int x = xp.getValue().intValue();
        int y = yp.getValue().intValue();
        AutoAim aimBot = (AutoAim) PigPack.getModuleManager().get(AutoAim.class);

        if (aimBot.target != null) {
            setTargetHealth(aimBot.target.getHealth());
        } else {
            setTargetHealth(0);
        }

        RenderUtil.drawRoundedRect(x,y,x + 98 + 3 + 5,y + 50 + 3, 2,new Color(0x1A0D22));

        RenderUtil.drawHGradientRect(x + 1,y + 1,x + 98 + 2 + 5,y + 3, color1.getValue().getRGB(), color2.getValue().getRGB());

        RenderUtil.drawRoundedRect(x + 1,y + 2,x + 98 + 2 + 5,y + 50 + 2, 2,new Color(0x433856));
        RenderUtil.drawRoundedRect(x + 2,y + 3,x + 98 + 1 + 5,y + 50 + 1, 2,new Color(0x2B1F38));

        FontUtil.drawString("Target: " + (aimBot.target != null ? aimBot.target.getName() : "None"), x + 4, y + 5, colorText.getValue().getRGB());
        FontUtil.drawString("Range: " + (aimBot.target != null ? (int) mc.player.getDistance(aimBot.target) : "0"), x + 4, y + FontUtil.getFontHeight() + 5, colorText.getValue().getRGB());
        FontUtil.drawString("Health: " + (aimBot.target != null ? aimBot.target.getHealth() * 5 : "0"), x + 4, y + FontUtil.getFontHeight() + 14, colorText.getValue().getRGB());
        FontUtil.drawString("AimBot: " + (aimBot.isToggled() ? "true" : "false"), x + 4, y + FontUtil.getFontHeight() + 23, colorText.getValue().getRGB());

        if (aimBot.target != null) drawPlayer(aimBot.target, x + 65, y + (aimBot.target.isSneaking() ? 10  : 13), 0.34f);
        RenderUtil.drawRoundedRect(x + 2,y + 42,x + 20 * 5.2f,y + 51, 2,new Color(0x312641));
        if (aimBot.target != null) RenderUtil.drawHGradientRect(x + 2,y + 42,x + getTargetHealth() * 5.2f,y + 51, new Color(109, 30, 255, 255 ).getRGB(), new Color(64, 0, 220, 255 ).getRGB());

        //RenderUtil.drawRoundedRect(x + 6, y - 1 + 190, FontUtil.getStringWidth(text) + 1, 15, 2, new Color(0x433856));
        //RenderUtil.drawRoundedRect(x + 7, y + 190, FontUtil.getStringWidth(text), 5, 15, new Color(0x2B1F38));

    }
    public void drawPlayer(final EntityPlayer player, final float xpos, final float ypos, final float scale) {
        final EntityPlayer ent = player;
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.rotate(0.0f, 0.0f, 5.0f, 0.0f);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((xpos + 25), (ypos + 25), 50.0f);
        GlStateManager.scale(-50.0f * scale, 50.0f * scale, 50.0f * scale);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-(float)Math.atan(ypos / 40.0f) * 20.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        final RenderManager rendermanager = mc.getRenderManager();
        rendermanager.setPlayerViewY(180.0f);
        rendermanager.setRenderShadow(false);
        try {
            rendermanager.renderEntity((Entity)ent, 0.0, 0.0, 0.0, 0.0f, 1.0f, false);
        }
        catch (Exception ex) {}
        rendermanager.setRenderShadow(true);
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        GlStateManager.depthFunc(515);
        GlStateManager.resetColor();
        GlStateManager.disableDepth();
        GlStateManager.popMatrix();
    }

    public void setTargetHealth(float targetHealth) {
        if (this.targetHealth == targetHealth) return;
        this.prevTargetHealth = this.targetHealth;
        this.targetHealth = targetHealth;
    }

    public float getTargetHealth() {
        targetHealth = prevTargetHealth + (targetHealth - prevTargetHealth) * mc.getRenderPartialTicks() / (8 * (Math.min(240, PigPack.getFpsManager().getFPS()) / 240f));
        return targetHealth;
    }

}