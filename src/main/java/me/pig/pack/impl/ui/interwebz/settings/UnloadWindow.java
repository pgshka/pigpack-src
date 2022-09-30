package me.pig.pack.impl.ui.interwebz.settings;

import me.pig.pack.PigPack;
import me.pig.pack.impl.ui.interwebz.Screen;
import me.pig.pack.impl.ui.base.impl.Window;
import me.pig.pack.utils.font.FontUtil;

import java.awt.*;

public final class UnloadWindow extends Window {
    private boolean picker, hue, alpha;
    private Color color;

    public UnloadWindow(double x, double y) {
        super("unload?" , x, y, 106, 20 + FontUtil.getFontHeight());

    }

    @Override public void render(int mouseX, int mouseY) {
        super.render(mouseX, mouseY);

        final int header = (FontUtil.getFontHeight() + 6);


        FontUtil.drawString("X", (float) (getPointW() - 4 - FontUtil.getStringWidth("X")), (float) getY() + 3, -1);
        FontUtil.drawString("[ Yes ]", (float) (getPointW() - 80 - FontUtil.getStringWidth("[ Yes ]")), (float) getY() + 15, -1);
        FontUtil.drawString("[ No ]", (float) (getPointW() - 3 - FontUtil.getStringWidth("[ No ]")), (float) getY() + 15, -1);


    }

    @Override public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        final int header = (FontUtil.getFontHeight() + 6);

        if (inArea(mouseX, mouseY, getPointW() - 6 - FontUtil.getStringWidth("<"), getY() + 3, getPointW() - 4, getY() + 3 + FontUtil.getFontHeight()) && button == 0) {
            Screen.getInstance().setUnloadWindow(null);
        }
        if (inArea(mouseX, mouseY, (float) (getPointW() - 80 - FontUtil.getStringWidth("[ Yes ]")), getY() + 15, (float) (getPointW() - 82), getY() + 15 + FontUtil.getFontHeight()) && button == 0) {
            Screen.getInstance().setUnloadWindow(null);
        }
        if (inArea(mouseX, mouseY, (float) (getPointW() - 3 - FontUtil.getStringWidth("[ No ]")), getY() + 15, (float) (getPointW() - 5), getY() + 15 + FontUtil.getFontHeight()) && button == 0) {
            Screen.getInstance().setUnloadWindow(null);
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        picker = false;
        hue = false;
        alpha = false;
    }
}
