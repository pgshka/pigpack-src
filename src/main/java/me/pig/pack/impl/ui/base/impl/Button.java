package me.pig.pack.impl.ui.base.impl;

import me.pig.pack.impl.ui.base.Widget;
import me.pig.pack.utils.RenderUtil;
import me.pig.pack.utils.font.FontUtil;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.function.Supplier;

public class Button extends Widget {
    private final Supplier<String> supplier;
    private final Runnable runnable;

    public Button(@Nonnull Widget parent, String name, double x, double y, double w, double h, Runnable runnable) {
        this(parent, () -> name, x, y, w, h, runnable);
    }

    public Button(@Nonnull Widget parent, Supplier<String> name, double x, double y, double w, double h, Runnable runnable) {
        super(parent, x, y, w, h);
        this.supplier = name;
        this.runnable = runnable;
    }

    @Override public void render(int mouseX, int mouseY) {
        RenderUtil.drawRoundedRect(getParent().getX() + getX(), getParent().getY() + getY(), getParent().getX() + getPointW(), getParent().getY() + getPointH(), 2, inArea(mouseX, mouseY) ? new Color(0x201028) : new Color(0x1A0D22));
        FontUtil.drawCenteredString(supplier.get(), (float) (getParent().getX() + getX() + getW() / 2), (float) (getParent().getY() + getY() + getH() / 2 - FontUtil.getFontHeight() / 2), -1);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (inArea(mouseX, mouseY)) {
            runnable.run();
        }
    }

    @Override public boolean inArea(double mouseX, double mouseY) {
        return mouseX > getX() + getParent().getX() && mouseY > getY() + getParent().getY() && mouseX < getPointW() + getParent().getX() && mouseY < getPointH() + getParent().getY();
    }
}
