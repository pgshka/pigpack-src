package me.pig.pack.impl.ui.base;

import me.pig.pack.api.Globals;

import javax.annotation.Nullable;

public abstract class Widget implements Globals {
    @Nullable private final Widget parent;
    private double x,y,w,h;

    public Widget(double x, double y, double w, double h) {
        this(null, x, y, w, h);
    }

    public Widget(@Nullable Widget parent, double x, double y, double w, double h) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public abstract void render( int mouseX, int mouseY );

    public void updateComponent( double mouseX, double mouseY ) {}

    public void mouseClicked( double mouseX, double mouseY, int button ) {}

    public void mouseReleased( double mouseX, double mouseY, int mouseButton ) {}

    public void onTick() {}

    public void keyTyped( int key ) {}

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getW() {
        return w;
    }

    public double getPointW() {
        return getX() + getW();
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getH() {
        return h;
    }

    public double getPointH() {
        return getY() + getH();
    }

    public void setH(double h) {
        this.h = h;
    }

    @Nullable public Widget getParent() {
        return parent;
    }

    public boolean inArea(double mouseX, double mouseY) {
        return mouseX > getX() && mouseY > getY() && mouseX < getPointW() && mouseY < getPointH();
    }

    public boolean inArea(double mouseX, double mouseY, double x, double y, double w, double h) {
        return mouseX > x && mouseY > y && mouseX < w && mouseY < h;
    }

}
