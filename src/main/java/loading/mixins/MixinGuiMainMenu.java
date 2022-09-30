package loading.mixins;

import me.pig.pack.PigPack;
import me.pig.pack.impl.module.render.ClickGui;
import me.pig.pack.impl.ui.menu.Screen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu {

    @Inject(method = "initGui", at = @At("HEAD"))
    private void init(CallbackInfo info) {
        ClickGui gui = PigPack.getModuleManager().get(ClickGui.class);
        if (gui.mainMenu.getValue()) {
            Minecraft.getMinecraft().displayGuiScreen(new Screen());
        }
    }

}
