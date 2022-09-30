package me.pig.pack.impl.ui.interwebz.settings;

import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.ui.base.Widget;
import me.pig.pack.impl.ui.interwebz.ModuleButton;
import me.pig.pack.impl.ui.interwebz.settings.popup.Popup;
import me.pig.pack.utils.RenderUtil;
import me.pig.pack.utils.font.FontUtil;

import javax.annotation.Nonnull;
import java.awt.*;

public final class ModeButton extends SettingButton<String> {
    public static Widget popup;

    public ModeButton(@Nonnull Widget parent, double x, double y, Setting<String> setting, ModuleButton button) {
        super(parent, x, y, parent.getW() / 2 - 45, 18 + FontUtil.getFontHeight(), setting, button);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        FontUtil.drawString(setting.getName(), (float) (getParent().getX() + getX()), (float) (getParent().getY() + getY() + 1), 0xFF9389A2);

        RenderUtil.drawRoundedRect(getParent().getX() + getX(), getParent().getY() + getY() + FontUtil.getFontHeight() + 2, getParent().getX() + getPointW(), getParent().getY() + getPointH() - 2, 2, new Color(0x38324E));
        RenderUtil.drawRoundedRect(getParent().getX() + getX() + 1, getParent().getY() + getY() + FontUtil.getFontHeight() + 3, getParent().getX() + getPointW() - 1, getParent().getY() + getPointH() - 3, 2, new Color(0x1F162B));

        FontUtil.drawString(setting.getValue(), (float) (getParent().getX() + getX() + 4), (float) (getParent().getY() + getY() + FontUtil.getFontHeight() + FontUtil.getFontHeight() / 2 + 1.5), 0xFF9389A2);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (inArea(mouseX, mouseY) && (button == 0 || button == 1)) {
            popup = new Popup(this, getX(), getPointH() - 3, getW());
        }
    }

    @Override public boolean inArea(double mouseX, double mouseY) {
        return mouseX > getX() + getParent().getX() && mouseY > getY() + getParent().getY() && mouseX < getPointW() + getParent().getX() && mouseY < getPointH() + getParent().getY();
    }

}
