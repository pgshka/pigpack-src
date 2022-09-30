package me.pig.pack.impl.ui.interwebz;

import me.pig.pack.impl.ui.base.impl.Description;
import me.pig.pack.impl.ui.base.impl.Window;
import me.pig.pack.impl.ui.interwebz.settings.ColorWindow;
import me.pig.pack.impl.ui.interwebz.settings.UnloadWindow;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Screen extends GuiScreen {
    private static Screen INSTANCE;
    public static final Description description = new Description();
    protected ScaledResolution sr;
    private final List<Window> windows = new ArrayList<>();
    private ColorWindow colorWindow = null;
    private UnloadWindow unloadWindow = null;

    public Screen() {
        windows.add(new ModuleWindow(20, 20, 300, 320));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void initGui() {
        super.initGui();
        ((ModuleWindow)windows.get(0)).updateName();
        sr = new ScaledResolution(mc);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        description.reset();
        for (Window window : windows) {
            window.render(mouseX, mouseY);
        }
        if (unloadWindow != null) unloadWindow.render(mouseX, mouseY);
        if (colorWindow != null) colorWindow.render(mouseX, mouseY);
        description.draw(mouseX, mouseY, sr);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) mc.displayGuiScreen(null);
        for (Window window : windows) {
            window.keyTyped(keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (unloadWindow != null) unloadWindow.mouseClicked(mouseX, mouseY, mouseButton);
        if (colorWindow != null) colorWindow.mouseClicked(mouseX, mouseY, mouseButton);
        for (Window window : windows) {
            window.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (unloadWindow != null) unloadWindow.mouseReleased(mouseX, mouseY, state);
        if (colorWindow != null) colorWindow.mouseReleased(mouseX, mouseY, state);
        for (Window window : windows) {
            window.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @SubscribeEvent public void onTick(TickEvent.ClientTickEvent event) {
        for (Window window : windows) {
            window.onTick();
        }
    }

    public static Screen getInstance() {
        if (INSTANCE == null) INSTANCE = new Screen();
        return INSTANCE;
    }

    public List<Window> getWindows() {
        return windows;
    }

    public void setColorWindow(ColorWindow colorWindow) {
        this.colorWindow = colorWindow;
    }
    public void setUnloadWindow(UnloadWindow unloadWindow) {
        this.unloadWindow = unloadWindow;
    }
}
