package me.pig.pack.impl.ui.interwebz.settings;

import me.pig.pack.PigPack;
import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.ui.base.Widget;
import me.pig.pack.utils.font.FontUtil;
import me.pig.pack.impl.ui.interwebz.ModuleButton;
import me.pig.pack.utils.RenderUtil;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.math.RoundingMode;

public final class SliderButton extends SettingButton<Number> {
    private boolean dragging;
    private double renderWidth;
    private double prevRenderWidth;
    private double speed;

    public SliderButton(@Nonnull Widget parent, double x, double y, Setting<Number> setting, ModuleButton button) {
        super(parent, x, y, parent.getW() / 2 - 45, 8 + FontUtil.getFontHeight(), setting, button);
    }

    @Override
    public void render(int mouseX, int mouseY) {
        final double diff = Math.min( 106, Math.max( 0, mouseX - (getX() + getParent().getX()) ) );
        final double min = setting.getMin( );
        final double max = setting.getMax( );
        setRenderWidth(104 * ( setting.getValue( ).doubleValue() - min ) / ( max - min ));
        if ( dragging ) {
            if ( diff == 0 ) setting.setValue( setting.getMin( ) );
            else {
                final double newValue = round( diff / 104 * ( max - min ) + min, 1 );
                double precision = 1.0D / setting.getInc( );
                setting.setValue( Math.round( Math.max( min, Math.min( max, newValue ) ) * precision ) / precision );
            }
        }
//        RenderUtil.drawRect(getParent().getX() + getX(), getParent().getY() + getY(), getParent().getX() + getPointW(), getPointH() + getParent().getY(), -1);
        FontUtil.drawString(setting.getName() + " " + round(setting.getValue().doubleValue(), 2), (float) (getParent().getX() + getX()), (float) (getParent().getY() + getY() + 1), 0xFF9389A2);
        RenderUtil.drawRect(getParent().getX() + getX(), getParent().getY() + getY() + FontUtil.getFontHeight() + 2, getParent().getX() + getPointW(), getParent().getY() + getY() + FontUtil.getFontHeight() + 6, 0xFF433954);
        RenderUtil.drawRect(getParent().getX() + getX() + 1, getParent().getY() + getY() + FontUtil.getFontHeight() + 3, getParent().getX() + getPointW() - 1, getParent().getY() + getY() + FontUtil.getFontHeight() + 5, 0xFF291F38);

        RenderUtil.drawHGradientRect((float) (getParent().getX() + getX() + 1), (float) (getParent().getY() + getY() + FontUtil.getFontHeight() + 3), (float) (getParent().getX() + getX() + getRenderWidth()), (float) (getParent().getY() + getY() + FontUtil.getFontHeight() + 5), 0xFFC67152, 0xFFD0475B);

//        GL11.glPushMatrix();
//        GL11.glScaled(.8, .8, 0);
//        FontUtil.drawCenteredString(String.valueOf(round(setting.getValue().doubleValue(), 2)), (float) (getParent().getX() + getX() + renderWidth) / .8f, (float) (getParent().getY() + getY() + FontUtil.getFontHeight() + 5) / .8f, -1);
//        GL11.glPopMatrix();

//        RenderUtil.drawRect(getParent().getX() + getX(), getParent().getY() + getY() + FontUtil.getFontHeight() + 2, getParent().getX() + getPointW(), getParent().getY() + getPointH(), 0xFF433954);

    }

    @Override public void mouseClicked(double mouseX, double mouseY, int button) {
        if (inArea(mouseX, mouseY) && button == 0) {
            dragging = true;
        }
    }

    @Override public void mouseReleased( final double mouseX, final double mouseY, final int mouseButton ) {
        dragging = false;
    }

    private static double round(final double value, final int places ) {
        if ( places < 0 ) {
            throw new IllegalArgumentException( );
        }

        BigDecimal bd = new BigDecimal( value );
        bd = bd.setScale( places, RoundingMode.HALF_UP );
        return bd.doubleValue( );
    }

    @Override public boolean inArea(double mouseX, double mouseY) {
        return mouseX > getX() + getParent().getX() && mouseY > getY() + getParent().getY() && mouseX < getPointW() + getParent().getX() && mouseY < getPointH() + getParent().getY();
    }

    public void setRenderWidth(double renderWidth) {
        if (this.renderWidth == renderWidth) return;
        this.prevRenderWidth = this.renderWidth;
        this.renderWidth = renderWidth;
    }

    public double getRenderWidth() {
        if (PigPack.getFpsManager().getFPS() < 20) {
            return renderWidth;
        }
        renderWidth = prevRenderWidth + (renderWidth - prevRenderWidth) * mc.getRenderPartialTicks() / (8 * (Math.min(240, PigPack.getFpsManager().getFPS()) / 240f));
        return renderWidth;
    }

    public void reset() {
        prevRenderWidth = 0;
        renderWidth = 0;
    }

}
