package loading.mixins;

import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;
import loading.ducks.IMinecraft;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin( Minecraft.class )
public class MixinMinecraft implements IMinecraft {
    @Mutable @Final @Shadow private Session session;
    @Shadow public GameSettings gameSettings;

    @Override
    public void setClientSession( Session session ) {
        this.session = session;
    }

    @Inject(method = "getLimitFramerate", at = @At("HEAD"), cancellable = true)
    public void getLimitFramerateHook(CallbackInfoReturnable<Integer> cir) {
        cir.cancel();
        cir.setReturnValue(this.gameSettings.limitFramerate); //30 fps is too low lol
    }

}