package me.pig.pack.impl.ui.interwebz.settings;

import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.ui.base.Widget;
import me.pig.pack.impl.ui.interwebz.ModuleButton;
import me.pig.pack.impl.ui.interwebz.ModuleWindow;

import javax.annotation.Nonnull;

public abstract class SettingButton<T> extends Widget {
    protected final Setting<T> setting;
    protected final ModuleButton button;

    public SettingButton(@Nonnull Widget parent, double x, double y, double w, double h, Setting<T> setting, ModuleButton button) {
        super(parent, x, y, w, h);
        this.setting = setting;
        this.button = button;
    }

    @Override public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (!getParent().inArea(mouseX, mouseY)) return;
    }

    @Override public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
        super.mouseReleased(mouseX, mouseY, mouseButton);
        if (!getParent().inArea(mouseX, mouseY)) return;
    }

    public Setting<T> getSetting() {
        return setting;
    }

    public double getScroll() {
        return ((ModuleWindow)getParent()).getSettingScroll();
    }

    @Override public double getY() {
        return super.getY() + getScroll();
    }
}
