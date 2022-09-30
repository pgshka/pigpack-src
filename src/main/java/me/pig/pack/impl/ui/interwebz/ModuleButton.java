package me.pig.pack.impl.ui.interwebz;

import me.pig.pack.PigPack;
import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.ui.base.Widget;
import me.pig.pack.impl.ui.interwebz.settings.*;
import me.pig.pack.utils.RenderUtil;
import me.pig.pack.utils.font.FontUtil;
import me.pig.pack.impl.module.Module;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ModuleButton extends Widget {
    private final List<SettingButton<?>> settingButtons = new ArrayList<>();
    private final Module module;

    public ModuleButton(@Nullable Widget parent, double x, double y, double w, double h, Module module) {
        super(parent, x, y, w, h);
        this.module = module;
        double offset = 0;
        for (Setting<?> setting : PigPack.getSettingManager().get(module)) {
            switch (setting.getType()) {
                case B:
                    final BooleanButton button = new BooleanButton(getParent(), getParent().getW() / 2 + 10, offset + (6 + FontUtil.getFontHeight()) * 2 + 14, (Setting<Boolean>) setting, this);
                    settingButtons.add(button);
                    offset += button.getH();
                    break;
                case N:
                    final SliderButton button1 = new SliderButton(getParent(), getParent().getW() / 2 + 20, offset + (6 + FontUtil.getFontHeight()) * 2 + 14, (Setting<Number>) setting, this);
                    settingButtons.add(button1);
                    offset += button1.getH();
                    break;
                case M:
                    final ModeButton button2 = new ModeButton(getParent(), getParent().getW() / 2 + 20, offset + (6 + FontUtil.getFontHeight()) * 2 + 14, (Setting<String>) setting, this);
                    settingButtons.add(button2);
                    offset += button2.getH();
                    break;
                case C:
                    final ColorButton button3 = new ColorButton(getParent(), getParent().getW() / 2 + 10, offset + (6 + FontUtil.getFontHeight()) * 2 + 14, (Setting<Color>) setting, this);
                    settingButtons.add(button3);
                    offset += button3.getH();
                    break;
            }
        }
    }

    @Override public void render(int mouseX, int mouseY) {
        RenderUtil.drawRect(getParent().getX() + getX(), getParent().getY() + getY() + 1, getParent().getX() + getPointW() - 2, getParent().getY() + getPointH() - 1, 0xFF38324E);
        RenderUtil.drawRect(getParent().getX() + getX() + 1, getParent().getY() + getY() + 2, getParent().getX() + getPointW() - 3, getParent().getY() + getPointH() - 2, 0xFF1F162B);
        FontUtil.drawString(module.getName(), (float) (getParent().getX() + getX() + 3), (float) (getParent().getY() + getY() + 4), module.isToggled() ? -1 : Color.gray.hashCode());
        if (inArea(mouseX, mouseY)) {
            Screen.description.update(module.getDesc());
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (inArea(mouseX, mouseY)) {
            if (button == 0) {
                module.toggle();
            } else if (button == 1){
                ((ModuleWindow)getParent()).setCurrent(module);
                ((ModuleWindow)getParent()).resetSettingScroll();
                for (SettingButton<?> settingButton : settingButtons) {
                    if (settingButton instanceof SliderButton) {
                        ((SliderButton) settingButton).reset();
                    }
                }
            }
        }
    }

    public Module getModule() {
        return module;
    }

    @Override public boolean inArea(double mouseX, double mouseY) {
        return mouseX > getX() + getParent().getX() && mouseY > getY() + getParent().getY() && mouseX < getPointW() + getParent().getX() && mouseY < getPointH() + getParent().getY();
    }

    @Override
    public double getY() {
        return super.getY() + ((ModuleWindow)getParent()).getModScroll();
    }

    public List<SettingButton<?>> getSettingButtons() {
        return settingButtons;
    }
}
