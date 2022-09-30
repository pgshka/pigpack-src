package me.pig.pack.impl.ui.menu;

import me.pig.pack.impl.ui.base.Widget;
import me.pig.pack.impl.ui.menu.taskbar.TaskBar;
import me.pig.pack.utils.RenderUtil;
import me.pig.pack.utils.shaders.GLSLSandboxShader;
import me.pig.pack.utils.shaders.Shaders;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class Screen extends GuiScreen {
    ScaledResolution sr;
    List<Widget> windows = new ArrayList<>();
    public static GLSLSandboxShader shader;
    public static long initTime;

    public Screen() {
        windows.add(new MainWindow(this, 0, 0));
        windows.add(new TaskBar(0, 15));
        initTime = System.currentTimeMillis();
        try {
            shader = new GLSLSandboxShader(Shaders.TOWERS.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GL11.glPushMatrix();
        GlStateManager.disableCull();
        shader.useShader(width*2, height*2, mouseX*2, mouseY*2, (System.currentTimeMillis() - initTime) / 1000f);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(-1f, -1f);
        GL11.glVertex2f(-1f, 1f);
        GL11.glVertex2f(1f, 1f);
        GL11.glVertex2f(1f, -1f);
        GL11.glEnd();
        GL20.glUseProgram(0);
        GL11.glPopMatrix();

        RenderUtil.drawVGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight() / 2f, 0xBB000000, 0x00000000);

        RenderUtil.drawVGradientRect(0, sr.getScaledHeight() / 2f, sr.getScaledWidth(), sr.getScaledHeight(), 0x00000000, 0xBB000000);

        RenderUtil.drawHGradientRect(0, 0, sr.getScaledWidth() / 4f, sr.getScaledHeight(), 0xBB000000, 0x00000000);
        RenderUtil.drawHGradientRect(sr.getScaledWidth() - sr.getScaledWidth() / 4f, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0xBB000000, 0x00000000);

        RenderUtil.drawVGradientRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0x11C67152, 0x11D0475B);
        windows.forEach(w -> w.render(mouseX, mouseY));
    }

    @Override protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        windows.forEach(w -> w.mouseClicked(mouseX, mouseY, mouseButton));
    }

    @Override protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        windows.forEach(w -> w.mouseReleased(mouseX, mouseY, state));
    }

    @Override public void initGui() {
        super.initGui();
        sr = new ScaledResolution(mc);
        windows.get(0).setX(sr.getScaledWidth() / 2f - 200);
        windows.get(0).setY(sr.getScaledHeight() / 2f - 120);
        windows.get(1).setY(sr.getScaledHeight() - windows.get(1).getH());
        windows.get(1).setW(sr.getScaledWidth());
    }

}
