package me.pig.pack.impl.ui.interwebz.settings.popup;

import me.pig.pack.PigPack;
import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.ui.base.Widget;
import me.pig.pack.utils.font.FontUtil;
import me.pig.pack.impl.ui.interwebz.settings.ModeButton;
import me.pig.pack.utils.RenderUtil;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class Popup extends Widget {
    private final List<PopupElement> elements = new ArrayList<>();
    private final Setting<String> mode;
    private double dynamicY;
    private double prevDynamicY;
    private boolean init = true;
    private boolean reset = false;

    public Popup(ModeButton parent, double x, double y, double w) {
        super(parent, x, y, w, 0);
        this.mode = parent.getSetting();

        for (int i = 0; i < parent.getSetting().getClients().size(); i++) {
            elements.add(new PopupElement(this, parent.getSetting().getClients().get(i), 0, i * (6 + FontUtil.getFontHeight()), w, 6 + FontUtil.getFontHeight()));
        }

        setH((6 + FontUtil.getFontHeight()) * elements.size());
        this.dynamicY = 0;
        this.prevDynamicY = dynamicY;
    }

    @Override public void render(int mouseX, int mouseY) {
        GL11.glPushMatrix();
        if (reset || init) {
            glScissor((int) getX(), (int) getY() - 1, (int) getPointW(), (int) (dynamicY));
            prevDynamicY = dynamicY;
            if (init) {
                dynamicY = Math.min(prevDynamicY + (dynamicY - prevDynamicY) * mc.getRenderPartialTicks() + (1.f / Math.min(240, PigPack.getFpsManager().getFPS())) * 100, getH());
            } else if (reset){
                dynamicY = prevDynamicY + (dynamicY - prevDynamicY) * mc.getRenderPartialTicks() - (1.f / Math.min(240, PigPack.getFpsManager().getFPS())) * 100;
            }
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
        }
        RenderUtil.drawRect(getX(), getY() - 1, getPointW(), getPointH(), 0xFF38324E);
        RenderUtil.drawRect(getX() + 1, getY(), getPointW() - 1, getPointH() - 1, 0xFF1F162B);
        for (PopupElement element : elements) {
            element.render(mouseX, mouseY);
        }
        if (reset || init) {
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }
        GL11.glPopMatrix();
        if (dynamicY + getY() <= getY() + 1 && !init) {
            ModeButton.popup = null;
        }

        if (init && dynamicY + getY() >= getY() + getH()) {
            init = false;
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (reset) return;
        for (PopupElement element : elements) {
            element.mouseClicked(mouseX, mouseY, button);
        }
    }

    public Setting<String> getMode() {
        return mode;
    }

    public void reset() {
        System.out.println("y");
        reset = true;
    }

    public static void glScissor(int x, int y, int width, int height) {
        ScaledResolution resolution = new ScaledResolution(mc);
        int scale = resolution.getScaleFactor();

        int scissorWidth = width * scale;
        int scissorHeight = height * scale;
        int scissorX = x * scale;
        int scissorY = mc.displayHeight - scissorHeight - (y * scale);

        GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
    }

    @Override public double getX() {
        return super.getX() + getParent().getParent().getX();
    }

    @Override public double getY() {
        return super.getY() + getParent().getParent().getY();
    }
}