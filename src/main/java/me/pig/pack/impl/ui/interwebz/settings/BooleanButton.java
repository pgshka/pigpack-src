package me.pig.pack.impl.ui.interwebz.settings;

import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.ui.base.Widget;
import me.pig.pack.impl.ui.interwebz.ModuleButton;
import me.pig.pack.utils.RenderUtil;
import me.pig.pack.utils.font.FontUtil;

import javax.annotation.Nonnull;
import java.awt.*;

public final class BooleanButton extends SettingButton<Boolean> {

    public BooleanButton(@Nonnull Widget parent, double x, double y, Setting<Boolean> setting, ModuleButton button) {
        super(parent, x, y, 0, 6 + FontUtil.getFontHeight(), setting, button);
    }

    @Override public void render(int mouseX, int mouseY) {
        RenderUtil.drawRoundedRect(getParent().getX() + getX(), getParent().getY() + getY() + 2, getParent().getX() + getX() + 8, getParent().getY() + getY() + 10, 2, new Color(0x38324E));
        if (setting.getValue()) {
            RenderUtil.drawHGradientRect((float) (getParent().getX() + getX() + 1), (float) (getParent().getY() + getY() + 3), (float) (getParent().getX() + getX() + 7), (float) (getParent().getY() + getY() + 9), 0xFFC67152, 0xFFD0475B);
        } else {
            RenderUtil.drawRect((float) (getParent().getX() + getX() + 1), (float) (getParent().getY() + getY() + 3), (float) (getParent().getX() + getX() + 7), (float) (getParent().getY() + getY() + 9), 0xFF1F1629);
        }
        FontUtil.drawString(setting.getName(), (float) (getParent().getX() + getX() + 10), (float) (getParent().getY() + getY() + 2), -1);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (inArea(mouseX, mouseY) && button == 0) {
            setting.setValue(!setting.getValue());
        }
    }

    @Override public double getW() {
        return 8;
    }

    @Override public boolean inArea(double mouseX, double mouseY) {
        return mouseX > getX() + getParent().getX() && mouseY > getY() + getParent().getY() && mouseX < getPointW() + getParent().getX() && mouseY < getPointH() + getParent().getY();
    }
}
