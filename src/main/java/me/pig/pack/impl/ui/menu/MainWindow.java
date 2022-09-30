package me.pig.pack.impl.ui.menu;

import me.pig.pack.PigPack;
import me.pig.pack.impl.ui.base.Widget;
import me.pig.pack.impl.ui.base.impl.Button;
import me.pig.pack.impl.ui.base.impl.Window;
import me.pig.pack.utils.RenderUtil;
import me.pig.pack.utils.Timer;
import me.pig.pack.utils.font.FontUtil;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public final class MainWindow extends Window {
    private final List<Widget> widgetList = new ArrayList<>();
    private final Timer timer = new Timer();

    private String current = "";
    private final Random random = new Random();
    private int step = 1, counter = 0;
    private boolean transition;

    public MainWindow(GuiScreen screen, double x, double y) {
        super("Main", x, y, 400, 220);
        widgetList.addAll(
                Arrays.asList(
                        new Button(this, "SinglePlayer", 20, 50, 360, 20, () -> mc.displayGuiScreen(new GuiWorldSelection(screen))),
                        new Button(this, "MultiPlayer", 20, 75, 360, 20, () -> mc.displayGuiScreen(new GuiMultiplayer(screen))),
                        new Button(this, "ClickGui", 20, 100, 360, 20, () -> mc.displayGuiScreen(new me.pig.pack.impl.ui.interwebz.Screen() {
                            @Override public void drawScreen(int mouseX, int mouseY, float partialTicks) {
                                GlStateManager.disableCull();
                                Screen.shader.useShader(width*2, height*2, mouseX*2, mouseY*2, (System.currentTimeMillis() - Screen.initTime) / 1000f);
                                GL11.glBegin(GL11.GL_QUADS);
                                GL11.glVertex2f(-1f, -1f);
                                GL11.glVertex2f(-1f, 1f);
                                GL11.glVertex2f(1f, 1f);
                                GL11.glVertex2f(1f, -1f);
                                GL11.glEnd();
                                GL20.glUseProgram(0);
                                RenderUtil.drawVGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight() / 2f, 0xBB000000, 0x00000000);

                                RenderUtil.drawVGradientRect(0, sr.getScaledHeight() / 2f, sr.getScaledWidth(), sr.getScaledHeight(), 0x00000000, 0xBB000000);

                                RenderUtil.drawHGradientRect(0, 0, sr.getScaledWidth() / 4f, sr.getScaledHeight(), 0xBB000000, 0x00000000);
                                RenderUtil.drawHGradientRect(sr.getScaledWidth() - sr.getScaledWidth() / 4f, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0xBB000000, 0x00000000);

                                RenderUtil.drawVGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0x11C67152, 0x11D0475B);
                                super.drawScreen(mouseX, mouseY, partialTicks);
                            }
                        })),
                        new Button(this, "Alts", 20, 125, 360, 20, () -> {}),
                        new Button(this, "Options", 20, 165, 175, 20, () -> mc.displayGuiScreen(new GuiOptions(screen, mc.gameSettings))),
                        new Button(this, "Exit", 205, 165, 175, 20, mc::shutdown)
                )
        );
    }

    @Override public void render(int mouseX, int mouseY) {
        super.render(mouseX, mouseY);
        if (timer.passed(200)) {
            counter = counter + step;
            String result = "P i g P a c k";
            if(transition && counter < result.length() * 2 && counter > 0) {
                String symbols = "410\\_.*&:?=()";
                current = result.substring(0, (int) MathHelper.clamp(Math.floor(counter / 2f), 0, result.length())) + symbols.charAt(random.nextInt(symbols.length()));
                transition = false;
            } else {
                current = result.substring(0, (int) MathHelper.clamp(Math.floor(counter / 2f), 0, result.length()));
                transition = true;
            }
            if (counter >= result.length() * 2 + 4 || counter < -4) step = step * -1;
            timer.reset();
        }
        FontUtil.drawCenteredString(current, (int) (getX() + getW() / 2) + 1, (int) (getY() + 20) + 1, 0xAA000000);
        FontUtil.drawCenteredString(current, (int) (getX() + getW() / 2), (int) (getY() + 20), 0xFFC67152);
        FontUtil.drawCenteredString("v" + PigPack.VERSION, (float) (getX() + getW() / 2) + 1, (float) (getY() + 23 + FontUtil.getFontHeight()) + 1, 0xAA000000);
        FontUtil.drawCenteredString("v" + PigPack.VERSION, (float) (getX() + getW() / 2), (float) (getY() + 23 + FontUtil.getFontHeight()), 0xFFD0475B);
        widgetList.forEach(w -> w.render(mouseX, mouseY));
    }

    @Override public void mouseClicked(double mouseX, double mouseY, int button) {
        super.mouseClicked(mouseX, mouseY, button);
        if (button == 0) widgetList.forEach(w -> w.mouseClicked(mouseX, mouseY, button));
    }

}
