package me.pig.pack.impl.ui.base.impl;

import me.pig.pack.PigPack;
import me.pig.pack.api.Globals;
import me.pig.pack.utils.MathUtil;
import me.pig.pack.utils.RenderUtil;
import me.pig.pack.utils.font.FontUtil;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public class Description implements Globals {
    private double w, h, prevW, prevH;
    private String content;

    public void draw(double mouseX, double mouseY, ScaledResolution sr) {
        double lw = getW(), lh = getH();
        if ((content == null || content.equals("")) && (Math.round(lw) <= 0 || Math.round(lh) <= 0)) {
            return;
        }
        double strwidth = FontUtil.getStringWidth(content) + 3;
        if (mouseX + 9 + strwidth > sr.getScaledWidth()) {
            RenderUtil.drawRect(mouseX - 9 - strwidth, mouseY - 1, mouseX + lw - 7 - strwidth, mouseY + lh + 1, 0xFF38324E);
            RenderUtil.drawRect(mouseX - 8 - strwidth, mouseY, mouseX + lw - 8 - strwidth, mouseY + lh, 0xFF2B1F38);
            if (content != null) {
                FontUtil.drawString(content, (float) (mouseX - 7 - strwidth), (float) (mouseY + 1), new Color(1f, 1f, 1f, (float) MathUtil.clamp(lw / strwidth, 0, 1)).hashCode());
            }
        } else {
            RenderUtil.drawRect(mouseX + 7, mouseY - 1, mouseX + lw + 9, mouseY + lh + 1, 0xFF38324E);
            RenderUtil.drawRect(mouseX + 8, mouseY, mouseX + lw + 8, mouseY + lh, 0xFF2B1F38);
            if (content != null) {
                FontUtil.drawString(content, (float) (mouseX + 9), (float) (mouseY + 1), new Color(1f, 1f, 1f, (float) MathUtil.clamp(lw / strwidth, 0, 1)).hashCode());
            }
        }
    }

    public void reset() {
        if (content == null) {
            setW(0);
            setH(0);
        } else {
            content = null;
        }
    }

    public void update(String content) {
        if (content == null || content.equals("")) return;
        this.content = content;
        setW(FontUtil.getStringWidth(content) + 3);
        setH(FontUtil.getFontHeight() + 2);
    }

    public void setH(double h) {
        if (this.h == h) return;
        this.prevH = this.h;
        this.h = h;
    }

    public void setW(double w) {
        if (this.w == w) return;
        this.prevW = this.w;
        this.w = w;
    }

    public double getW() {
        w = prevW + (w - prevW) * mc.getRenderPartialTicks() / (8 * (Math.min(240, PigPack.getFpsManager().getFPS()) / 240f));
        w = Math.max(0, w);
        return w;
    }

    public double getH() {
        h = prevH + (h - prevH) * mc.getRenderPartialTicks() / (8 * (Math.min(240, PigPack.getFpsManager().getFPS()) / 240f));
        h = Math.max(0, h);
        return h;
    }
}
