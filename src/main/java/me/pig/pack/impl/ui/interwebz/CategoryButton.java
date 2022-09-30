package me.pig.pack.impl.ui.interwebz;

import me.pig.pack.PigPack;
import me.pig.pack.impl.ui.base.Widget;
import me.pig.pack.utils.RenderUtil;
import me.pig.pack.utils.font.FontUtil;
import me.pig.pack.impl.module.Module;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CategoryButton extends Widget {
    private final List<ModuleButton> moduleButtonList = new ArrayList<>();
    private final Module.Category category;
    private boolean active;

    public CategoryButton(@Nonnull ModuleWindow parent, double x, double y, double w, double h, Module.Category category) {
        super(parent, x, y, w, h);
        this.category = category;
        int count = 0;
        for (Module mod : PigPack.getModuleManager().get(category)) {
            ModuleButton button = new ModuleButton(getParent(), 16,  (6 + FontUtil.getFontHeight()) * 2 + 14 + (6 + FontUtil.getFontHeight()) * count, getParent().getW() / 2 - 16 - 8,6 + FontUtil.getFontHeight(), mod);
            moduleButtonList.add(button);
            count++;
        }
    }

    @Override @SuppressWarnings("ConstantConditions")
    public void render(int mouseX, int mouseY) {
        RenderUtil.drawRect(getParent().getX() + getX() - 1, getParent().getY() + getY(), getParent().getX() + getPointW() + 1, getParent().getY() + getPointH() + 1,0xFF38324E);
        RenderUtil.drawRect(getParent().getX() + getX(), getParent().getY() + getY(), getParent().getX() + getPointW(), getParent().getY() + getPointH(), isActive() ? 0xFF291F38 : 0xFF171226);
        if (isActive()) {
            RenderUtil.drawRect(getParent().getX() + getX(), getParent().getY() + getY(), getParent().getX() + getPointW(), getParent().getY() + getPointH() + 1, 0xFF291F38);
        }
        FontUtil.drawCenteredString(category.getName(), (float) (getParent().getX() + getX() + getW() / 2), (float) (getParent().getY() + getY() + 3), -1);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (inArea(mouseX, mouseY)) {
            if (getParent() instanceof ModuleWindow) {
                for (CategoryButton cat : ((ModuleWindow) getParent()).getButtons()) {
                    cat.setActive(false);
                }
            }
            setActive(true);
        }
    }

    public Module.Category getCategory() {
        return category;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override public boolean inArea(double mouseX, double mouseY) {
        return mouseX > getX() + getParent().getX() && mouseY > getY() + getParent().getY() && mouseX < getPointW() + getParent().getX() && mouseY < getPointH() + getParent().getY();
    }

    public List<ModuleButton> getModuleButtonList() {
        return moduleButtonList;
    }
}
