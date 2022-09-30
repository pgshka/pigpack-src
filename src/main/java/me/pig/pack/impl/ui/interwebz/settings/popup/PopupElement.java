package me.pig.pack.impl.ui.interwebz.settings.popup;

import me.pig.pack.impl.ui.base.Widget;
import me.pig.pack.utils.font.FontUtil;
import me.pig.pack.utils.RenderUtil;

public class PopupElement extends Widget {
    private final String value;

    public PopupElement(Popup parent, String value, double x, double y, double w, double h) {
        super(parent, x, y, w, h);
        this.value = value;
    }

    @Override public void render(int mouseX, int mouseY) {
        if (inArea(mouseX, mouseY)) {
            RenderUtil.drawRect(getParent().getX() + 1, getParent().getY() + getY(), getW() + getParent().getX() - 1, getParent().getY() + getPointH() - 1, 0xFF2B1F38);
        }
        FontUtil.drawString(value, (float) (getParent().getX() + 4), (float) (getParent().getY() + getY() + 3), ((Popup)getParent()).getMode().getValue().equalsIgnoreCase(value) ? 0xFFD0475B : inArea(mouseX, mouseY) ? -1 : 0xFF9389A2);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (inArea(mouseX, mouseY) && button == 0) {
            ((Popup)getParent()).getMode().setValue(getValue());
        }
    }

    public String getValue() {
        return value;
    }

    @Override public boolean inArea(double mouseX, double mouseY) {
        return mouseX > getX() + getParent().getX() && mouseY > getY() + getParent().getY() && mouseX < getPointW() + getParent().getX() && mouseY < getPointH() + getParent().getY();
    }
}
