package loading.mixins;

import me.pig.pack.PigPack;
import me.pig.pack.impl.module.render.NameProtect;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.particle.ParticleFirework;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiNewChat.class)
public abstract class MixinGuiNewChat {
    /**
     * target = {@link FontRenderer#drawStringWithShadow(String,
     * float, float, int)}
     */
    @Redirect(method = "drawChat", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;" + "drawStringWithShadow(Ljava/lang/String;FFI)I"))
    public int drawStringWithShadowHook(FontRenderer renderer, String text, float x, float y, int color) {

        NameProtect nameProtect = PigPack.getModuleManager().get(NameProtect.class);


        if (nameProtect.isToggled() && Minecraft.getMinecraft().getSession() != null){
            text = text.replace(Minecraft.getMinecraft().getSession().getUsername(), "pigpack_user");
            for (String friend : PigPack.getFriendManager().get()){
                text = text.replace(friend, "pigpack_friend");
            }
        }
        return renderer.drawStringWithShadow(text, x,y,color);
    }
}