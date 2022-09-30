package me.pig.pack.impl.ui.interwebz;

import me.pig.pack.PigPack;
import me.pig.pack.api.Globals;
import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.module.combat.AntiBot;
import me.pig.pack.impl.ui.base.impl.Window;
import me.pig.pack.impl.ui.interwebz.settings.ModeButton;
import me.pig.pack.impl.ui.interwebz.settings.SettingButton;
import me.pig.pack.impl.ui.interwebz.settings.popup.Popup;
import me.pig.pack.utils.RenderUtil;
import me.pig.pack.utils.font.FontUtil;
import me.pig.pack.impl.module.Module;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class ModuleWindow extends Window {
    private final List<String> motd = Arrays.asList("hake is better", "triplecat", ">:^)", "wurst plus three :0", "meh", "pig oink oink", "W202 learnt about the Internet yesterday");
    private final List<CategoryButton> categoryButtons = new ArrayList<>();//17
    private Module current = PigPack.getModuleManager().get(AntiBot.class);
    private boolean secondPanel = false;
    private boolean listening;

    private int settingScroll = 0, prevSettingScroll = 0;
    private double settingScrollSpeed = 0;

    private int modScroll = 0, prevModScroll = 0;
    private double modScrollSpeed = 0;

    public ModuleWindow(double x, double y, double w, double h) {
        super("the hake", x, y, w, h);
        int counter = 0;
        final double length = (getW() - 8f) / Module.Category.values().length;
        for (Module.Category category : Module.Category.values()) {
            CategoryButton cat = new CategoryButton(this, 4 + counter * length, 6 + FontUtil.getFontHeight(), length, 6 + FontUtil.getFontHeight(), category);
            if (counter == 0) cat.setActive(true);
            categoryButtons.add(cat);
            counter++;
        }
    }

    @Override public void render(int mouseX, int mouseY) {
        super.render(mouseX, mouseY);
        if (secondPanel) {
            FontUtil.drawString("<", (float) (getPointW() - 4 - FontUtil.getStringWidth("<")), (float) getY() + 3, -1);
        } else {
            FontUtil.drawString(">", (float) (getPointW() - 4 - FontUtil.getStringWidth(">")), (float) getY() + 3, -1);
        }

        int w = Mouse.getDWheel();

        if (w > 0) {
            if (inArea(mouseX, mouseY, getX() + 13, getY() + (6 + FontUtil.getFontHeight()) * 2 + 8, getW() / 2 - 7 + getX(), getPointH() - 10)) {
                modScrollSpeed = 16;
            } else if (inArea(mouseX, mouseY, getW() / 2 + 7 + getX(), getY() + (6 + FontUtil.getFontHeight()) * 2 + 8, getPointW() - 13, getPointH() - 10)) {
                settingScrollSpeed = 16;
            }
        } else if (w < 0){
            if (inArea(mouseX, mouseY, getX() + 13, getY() + (6 + FontUtil.getFontHeight()) * 2 + 8, getW() / 2 - 7 + getX(), getPointH() - 10)) {
                modScrollSpeed = -16;
            } else if (inArea(mouseX, mouseY, getW() / 2 + 7 + getX(), getY() + (6 + FontUtil.getFontHeight()) * 2 + 8, getPointW() - 13, getPointH() - 10)) {
                settingScrollSpeed = -16;
            }
        }

        setSettingScroll(settingScroll + settingScrollSpeed);
        settingScrollSpeed *= 0.5f;
        if (settingScrollSpeed < 0.125) {
            settingScrollSpeed = 0;
            this.prevSettingScroll = settingScroll;
        }
        setModScroll(modScroll + modScrollSpeed);
        modScrollSpeed *= 0.5f;
        if (modScrollSpeed < 0.125) {
            modScrollSpeed = 0;
            this.prevModScroll = modScroll;
        }

        RenderUtil.drawRoundedRect(getX() + 3, getY() + 5 + FontUtil.getFontHeight(), getPointW() - 3, getPointH() - 2, 2, new Color(0x38324E));
        RenderUtil.drawRoundedRect(getX() + 4, getY() + 6 + FontUtil.getFontHeight(), getPointW() - 4, getPointH() - 3, 2, new Color(0x1F162B));

//        if (true) { //потом чек сделаю // 8
        final double width = FontUtil.getStringWidth("Modules");

        RenderUtil.drawRect(getX() + 12, getY() + (6 + FontUtil.getFontHeight()) * 2 + 8, getW() / 2 - 6 + getX(), getPointH() - 9, 0xFF38324E);
        RenderUtil.drawRect(getX() + 13, getY() + (6 + FontUtil.getFontHeight()) * 2 + 8, getW() / 2 - 7 + getX(), getPointH() - 10, 0xFF2B1F38);
        RenderUtil.drawRect(getX() + 12, getY() + (6 + FontUtil.getFontHeight()) * 2 + 7, getX() + 18, getY() +  (6 + FontUtil.getFontHeight()) * 2 + 8, 0xFF38324E);
        RenderUtil.drawRect(getX() + 24 + width, getY() + (6 + FontUtil.getFontHeight()) * 2 + 7, getW() / 2 - 6 + getX(), getY() +  (6 + FontUtil.getFontHeight()) * 2 + 8, 0xFF38324E);

        FontUtil.drawString("Modules", (float) (getX() + 22), (float) (getY() +  (6 + FontUtil.getFontHeight()) * 2 + 4), -1);

        if (current != null) {
            final String name = String.format("%s [%s]", current.getName(), listening ? "..." : StringUtils.capitalize(Keyboard.getKeyName(current.getKey()).toLowerCase()));
            final double modWidth = FontUtil.getStringWidth(name);
//            System.out.println( (getPointH() - 9) - (getY() + (6 + FontUtil.getFontHeight()) * 2 + 8));
            RenderUtil.drawRect(getW() / 2 + 6 + getX(), getY() + (6 + FontUtil.getFontHeight()) * 2 + 8, getPointW() - 12, getPointH() - 9, 0xFF38324E);
            RenderUtil.drawRect(getW() / 2 + 7 + getX(), getY() + (6 + FontUtil.getFontHeight()) * 2 + 8, getPointW() - 13, getPointH() - 10, 0xFF2B1F38);
            RenderUtil.drawRect(getW() / 2 + 6 + getX(), getY() + (6 + FontUtil.getFontHeight()) * 2 + 7, getW() / 2 + 12 + getX(), getY() +  (6 + FontUtil.getFontHeight()) * 2 + 8, 0xFF38324E);
            RenderUtil.drawRect(getW() / 2 + 20 + getX() + modWidth, getY() + (6 + FontUtil.getFontHeight()) * 2 + 7, getPointW() - 12, getY() +  (6 + FontUtil.getFontHeight()) * 2 + 8, 0xFF38324E);
            FontUtil.drawString(name, (float) (getX() + getW() / 2 + 16), (float) (getY() +  (6 + FontUtil.getFontHeight()) * 2 + 4), -1);
        }
//        } else {
//
//        }

        for (CategoryButton button : categoryButtons) {
            button.render(mouseX, mouseY);
            for (ModuleButton module : button.getModuleButtonList()) {
                if (button.isActive()) {
                    GL11.glPushMatrix();
                    Popup.glScissor((int) (getX() + 13), (int) (getY() + (6 + FontUtil.getFontHeight()) * 2 + 12), (int) (getW() / 2 - 7), (int) (getH() - 53));
                    GL11.glEnable(GL11.GL_SCISSOR_TEST);
                    module.render(mouseX, mouseY);
                    GL11.glDisable(GL11.GL_SCISSOR_TEST);
                    GL11.glPopMatrix();
                }
                if (module.getModule() == current) {
                    for (SettingButton<?> settingButton : module.getSettingButtons()) {
                        GL11.glPushMatrix();
                        Popup.glScissor((int) (getW() / 2 + 7 + getX()), (int) (getY() + (6 + FontUtil.getFontHeight()) * 2 + 12), (int) (getPointW() - 13), (int) (getH() - 53));
                        GL11.glEnable(GL11.GL_SCISSOR_TEST);
                        settingButton.render(mouseX, mouseY);
                        GL11.glDisable(GL11.GL_SCISSOR_TEST);
                        GL11.glPopMatrix();
                    }
                }
            }
        }

        if (secondPanel) {
            //player viewer
            RenderUtil.drawRoundedRect(getPointW() + 5, getY() - 2, getPointW() + 137, getPointH() - 136, 2, new Color(0x1A0D22));
            RenderUtil.drawRoundedRect(getPointW() + 6, getY() - 1, getPointW() + 136, getPointH() - 137, 2, new Color(0x433856));
            RenderUtil.drawRoundedRect(getPointW() + 7, getY(), getPointW() + 135, getPointH() - 138, 1, new Color(0x2B1F38));
            FontUtil.drawString("Player viewer", (float)getPointW() + 11, (float) getY() + 3, -1);
            drawPlayer(getPointW() - 290, getY());

            //alt manager
            RenderUtil.drawRoundedRect(getPointW() + 5, getY() - 2 + 190, getPointW() + 137, getPointH() + 2, 2, new Color(0x1A0D22));
            RenderUtil.drawRoundedRect(getPointW() + 6, getY() - 1 + 190, getPointW() + 136, getPointH() + 1, 2, new Color(0x433856));
            RenderUtil.drawRoundedRect(getPointW() + 7, getY() + 190, getPointW() + 135, getPointH(), 1, new Color(0x2B1F38));
            FontUtil.drawString("Manager", (float)getPointW() + 11, (float) getY() + 3 + 190, -1);

            //AltManager button
            RenderUtil.drawRoundedRect(getPointW() + 12, getY() - 2 + 210, getPointW() + 130, getPointH() - 95, 2, inArea(mouseX, mouseY, getPointW() + 12, getY() - 2 + 210, getPointW() + 130, getPointH() - 95) ? new Color(0x201028) : new Color(0x1A0D22));
            FontUtil.drawString("config update" , (float) (getPointW() + 15), (float) (getY() - 2 + 215), -1);
            //Unload button
            RenderUtil.drawRoundedRect(getPointW() + 12, getY() - 2 + 210 + 21, getPointW() + 130, getPointH() - 95 + 21, 2, inArea(mouseX, mouseY, getPointW() + 12, getY() - 2 + 210 + 21, getPointW() + 130, getPointH() - 95 + 21) ? new Color(0x201028) : new Color(0x1A0D22));
            FontUtil.drawString("unload pigpack", (float) (getPointW() + 15), (float) (getY() - 2 + 210 + 26), -1);


        }

        if (ModeButton.popup != null) {
            ModeButton.popup.render(mouseX, mouseY);
        }

    }

    @Override public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        listening = false;

        if (ModeButton.popup != null) {
            ModeButton.popup.mouseClicked(mouseX, mouseY, button);
            ((Popup)ModeButton.popup).reset();
            return;
        }

        final String name = String.format("%s [%s]", current.getName(), StringUtils.capitalize(Keyboard.getKeyName(current.getKey()).toLowerCase()));
        final double modWidth = FontUtil.getStringWidth(name);

        if (current != null && inArea(mouseX, mouseY, (float) (getX() + getW() / 2 + 16), (float) (getY() +  (6 + FontUtil.getFontHeight()) * 2 + 4), getW() / 2 + 20 + getX() + modWidth, (float) (getY() +  (6 + FontUtil.getFontHeight()) * 2.5 + 4)) && button == 0 && !listening) {
            listening = true;
        }

        if (inArea(mouseX, mouseY, getPointW() - 6 - FontUtil.getStringWidth("<"), getY() + 3, getPointW() - 4, getY() + 3 + FontUtil.getFontHeight())) {
            secondPanel = !secondPanel;
        } else if (secondPanel &&  inArea(mouseX, mouseY, getPointW() + 12, getY() - 2 + 210, getPointW() + 130, getPointH() - 95)) {
            PigPack.getConfigManager().load();
        }  else if (secondPanel && inArea(mouseX, mouseY, getPointW() + 12, getY() - 2 + 210 + 21, getPointW() + 130, getPointH() - 95 + 21)){
            //unload hack
        } else {
            for (CategoryButton category : categoryButtons) {
                category.mouseClicked(mouseX, mouseY, button);
                for (ModuleButton module : category.getModuleButtonList()) {
                    if (category.isActive()) {
                        module.mouseClicked(mouseX, mouseY, button);
                    }
                    if (module.getModule() == current) {
                        for (SettingButton<?> settingButton : module.getSettingButtons()) {
                            settingButton.mouseClicked(mouseX, mouseY, button);
                        }
                    }
                }
            }
        }
    }

    @Override public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
            for (CategoryButton category : categoryButtons) {
                category.mouseReleased(mouseX, mouseY, mouseButton);
                for (ModuleButton module : category.getModuleButtonList()) {
                    if (category.isActive()) {
                        module.mouseReleased(mouseX, mouseY, mouseButton);
                    }
                    if (module.getModule() == current) {
                        for (SettingButton<?> settingButton : module.getSettingButtons()) {
                            settingButton.mouseReleased(mouseX, mouseY, mouseButton);
                        }
                    }
                }
        }
    }

    @Override
    public void keyTyped(int key) {
        super.keyTyped(key);
        if (listening) {
            listening = false;
            if (Keyboard.KEY_ESCAPE == key) return;
            getCurrent().setKey(key);
        }
    }

    public void updateName() {
        Collections.shuffle(motd, new SecureRandom());
        setName(motd.get(0));
    }

    public List<CategoryButton> getButtons() {
        return categoryButtons;
    }

    public Module getCurrent() {
        return current;
    }

    public void setCurrent(Module current) {
        this.current = current;
    }

    public double getSettingScroll() {
        if (prevSettingScroll == settingScroll) return settingScroll;
        return prevSettingScroll + (settingScroll - prevSettingScroll) * Globals.mc.getRenderPartialTicks();
    }

    public double getModScroll() {
        if (prevModScroll == modScroll) return modScroll;
        return prevModScroll + (modScroll - prevModScroll) * Globals.mc.getRenderPartialTicks();
    }

    public double getPrevSettingScroll() {
        return prevSettingScroll;
    }

    public void setSettingScroll(double settingScroll) {
        if (this.settingScroll == settingScroll) return;
        if (current == null) return;
        double max = getMax(getCurrent());
        if (max < 273.0) {
            this.prevSettingScroll = 0;
            this.settingScroll = 0;
            return;
        }
        this.prevSettingScroll = this.settingScroll;
        this.settingScroll = (int) MathHelper.clamp(settingScroll, -max + 273 - 9, 0);
    }

    public void setModScroll(double modScroll) {
        if (this.modScroll == modScroll) return;
        double max = getMax() * (6 + FontUtil.getFontHeight());
        if (max < 273.0) {
            this.prevModScroll = 0;
            this.modScroll = 0;
            return;
        }
        this.prevModScroll = this.modScroll;
        this.modScroll = (int) MathHelper.clamp(modScroll, -max + 273 - 9, 0);
    }

    public void resetSettingScroll() {
        this.settingScroll = 0;
        this.prevSettingScroll = 0;
    }

    public void resetModScroll() {
        this.modScroll = 0;
        this.prevModScroll = 0;
    }

    double getMax(Module module) {
        double max = 0;
        for (Setting<?> setting : PigPack.getSettingManager().get(module)) {
            switch (setting.getType()) {
                case B:
                case C:
                    max += 6 + FontUtil.getFontHeight();
                    break;
                case N:
                    max += 8 + FontUtil.getFontHeight();
                    break;
                case M:
                    max += 18 + FontUtil.getFontHeight();
                    break;
            }
        }
        return max;
    }

    double getMax() {
        for (CategoryButton categoryButton : categoryButtons) {
            if (categoryButton.isActive()) {
                return categoryButton.getModuleButtonList().size();
            }
        }
        return 0;
    }

    public void drawPlayer(double x, double y) {
        EntityPlayerSP ent = Globals.mc.player;
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.enableAlpha();
        GlStateManager.shadeModel(7424);
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        GlStateManager.rotate(0.0f, 0.0f, 5.0f, 0.0f);
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) (x + 360), (float) (y + 168), 50.0f);
        GlStateManager.scale(-50.0f * 1.5f, 50.0f * 1.5f, 50.0f * 1.5f);
        GlStateManager.rotate(180.0f, 0.0f, 0.0f, 1.0f);
        GlStateManager.rotate(135.0f, 0.0f, 1.0f, 0.0f);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(-135.0f, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(-((float) Math.atan((float) 1 / 40.0f)) * 20.0f, 1.0f, 0.0f, 0.0f);
        GlStateManager.translate(0.0f, 0.0f, 0.0f);
        RenderManager rendermanager = Globals.mc.getRenderManager();
        rendermanager.setPlayerViewY(180.0f);
        rendermanager.setRenderShadow(false);
        try {
            rendermanager.renderEntity(ent, 0.0, 0.0, 0.0, 0f, 1.0f, false);
        } catch (Exception exception) {
            // empty catch block
        }
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

}
