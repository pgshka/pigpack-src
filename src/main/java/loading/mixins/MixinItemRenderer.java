package loading.mixins;

import me.pig.pack.PigPack;
import me.pig.pack.impl.module.render.NoRender;
import me.pig.pack.impl.module.render.ViewModel;
import me.pig.pack.api.Globals;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin( value = { ItemRenderer.class } )
public class MixinItemRenderer implements Globals {

    @Shadow @Final private Minecraft mc;
    @Shadow @Final private RenderItem itemRenderer;

    @Inject( method = "renderItemSide", at = @At( value = "HEAD" ) )
    public void renderItemSide(EntityLivingBase entityLivingBase, ItemStack stack, ItemCameraTransforms.TransformType transform, boolean leftHanded, CallbackInfo info) {
        ViewModel view = ( ViewModel ) PigPack.getModuleManager().get(ViewModel.class);
        if (view.isToggled() && entityLivingBase == mc.player) {
            GlStateManager.scale(view.scaleX.getValue().floatValue(), view.scaleY.getValue().floatValue(), view.scaleZ.getValue().floatValue());
            if (mc.player.getActiveItemStack() != stack) {
                GlStateManager.translate((view.posX.getValue().floatValue() * 0.1f) * (leftHanded ? -1 : 1), view.posY.getValue().floatValue() * 0.1f, view.posZ.getValue().floatValue() * 0.1);
            }
        }
    }


    @Inject( method = { "renderFireInFirstPerson" }, at = { @At( value = "HEAD" ) }, cancellable = true )
    public void renderFireInFirstPersonHook(CallbackInfo info) {
        if (PigPack.getModuleManager().get(NoRender.class).isToggled()) info.cancel();

    }

    @Inject( method = { "renderSuffocationOverlay" }, at = { @At( value = "HEAD" ) }, cancellable = true )
    public void renderSuffocationOverlay(CallbackInfo ci) {
        if (PigPack.getModuleManager().get(NoRender.class).isToggled()) ci.cancel();
    }
}