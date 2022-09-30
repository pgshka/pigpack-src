package me.pig.pack.impl.ui.interwebz.settings;

import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.ui.interwebz.Screen;
import me.pig.pack.impl.ui.base.Widget;
import me.pig.pack.impl.ui.interwebz.ModuleButton;
import me.pig.pack.utils.RenderUtil;
import me.pig.pack.utils.font.FontUtil;

import javax.annotation.Nonnull;
import java.awt.*;

public final class ColorButton extends SettingButton<Color> {
    public ColorButton(@Nonnull Widget parent, double x, double y, Setting<Color> setting, ModuleButton button) {
        super(parent, x, y, parent.getW() / 2 - 35, 6 + FontUtil.getFontHeight(), setting, button);
    }

    @Override public void render(int mouseX, int mouseY) {
        FontUtil.drawString(setting.getName(), (float) (getParent().getX() + getX() + 10), (float) (getParent().getY() + getY() + 2), -1);

        RenderUtil.drawRoundedRect(getParent().getX() + getPointW() - 10,
                getParent().getY() + getY() + 2,
                getParent().getX() + getPointW(),
                getParent().getY() + getY() + 12, 2, new Color(0x38324E));


        RenderUtil.drawRect(getParent().getX() + getPointW() - 9,
                getParent().getY() + getY() + 3,
                getParent().getX() + getPointW() - 1,
                getParent().getY() + getY() + 11, 0xFF000000);

        RenderUtil.drawRect(getParent().getX() + getPointW() - 8.5,
                getParent().getY() + getY() + 3.5,
                getParent().getX() + getPointW() - 1.5,
                getParent().getY() + getY() + 10.5, getSetting().getValue().hashCode());

    }

    @Override public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (inArea(mouseX, mouseY) && button == 0) {
            Screen.getInstance().setColorWindow(new ColorWindow(setting, mouseX, mouseY));
        }
    }

    @Override public boolean inArea(double mouseX, double mouseY) {
        return mouseX > getX() + getParent().getX() && mouseY > getY() + getParent().getY() && mouseX < getPointW() + getParent().getX() && mouseY < getPointH() + getParent().getY();
    }

}
