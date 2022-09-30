package loading.mixins;

import me.pig.pack.PigPack;
import me.pig.pack.impl.module.misc.FreeCam;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static me.pig.pack.api.Globals.mc;

@Mixin(Entity.class)
public class MixinEntity {

    @Inject(method = "isEntityInsideOpaqueBlock", at = @At("HEAD"), cancellable = true)
    public void isEntityInsideOpaqueBlockHook(CallbackInfoReturnable<Boolean> cir) {
        Entity entity = Entity.class.cast(this);
        if(entity == mc.player && PigPack.getModuleManager().get(FreeCam.class).isToggled()) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

}
