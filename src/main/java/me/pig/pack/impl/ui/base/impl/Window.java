package me.pig.pack.impl.ui.base.impl;

import me.pig.pack.utils.font.FontUtil;
import me.pig.pack.impl.ui.base.Widget;
import me.pig.pack.utils.RenderUtil;

import java.awt.*;

public class Window extends Widget {
    private String name;
    private boolean dragging;
    private double[] offsets;

    public Window(String name, double x, double y, double w, double h) {
        super(x, y, w, h);
        this.name = name;
    }

    @Override public void render(int mouseX, int mouseY) {
        if (dragging) {
            setX( mouseX - offsets[0] );
            setY( mouseY - offsets[1] );
        }
        RenderUtil.drawRoundedRect(getX() - 2, getY() - 2, getPointW() + 2, getPointH() + 2, 2, new Color(0x1A0D22));
        RenderUtil.drawRoundedRect(getX() - 1, getY() - 1, getPointW() + 1, getPointH() + 1, 2, new Color(0x433856));
        RenderUtil.drawRoundedRect(getX(), getY(), getPointW(), getPointH(), 1, new Color(0x291F38));
        FontUtil.drawString("PigPack - " + name, (float) getX() + 4, (float) getY() + 3, -1);
    }

    @Override public void mouseClicked(double mouseX, double mouseY, int button) {
        if (inArea(mouseX, mouseY, getX(), getY(), getPointW(), getY() + 6 + FontUtil.getFontHeight()) && button == 0) {
            dragging = true;
            offsets = new double[] { mouseX - getX( ), mouseY - getY() };
        }
    }

    @Override public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
        dragging = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
