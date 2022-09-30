package loading.mixins;

import me.pig.pack.PigPack;
import me.pig.pack.api.Globals;
import me.pig.pack.impl.module.movement.GuiMove;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.MovementInputFromOptions;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin( MovementInputFromOptions.class )
public class MixinMovementInputFromOptions implements Globals {

    @Redirect( method = "updatePlayerMoveState", at = @At( value = "INVOKE", target = "Lnet/minecraft/client/settings/KeyBinding;isKeyDown()Z" ) )
    public boolean isKeyPressed( KeyBinding keyBinding ) {
        GuiMove noslow = PigPack.getModuleManager( ).get( GuiMove.class );
        return noslow.isToggled() && !( mc.currentScreen instanceof GuiChat)     ? Keyboard.isKeyDown( keyBinding.getKeyCode( ) ) : keyBinding.isKeyDown( );
    }

}