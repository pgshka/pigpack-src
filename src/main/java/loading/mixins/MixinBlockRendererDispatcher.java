package loading.mixins;

import me.pig.pack.api.Globals;
import me.pig.pack.impl.module.render.RustXray;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import net.minecraft.init.Blocks;
import me.pig.pack.PigPack;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.world.IBlockAccess;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.block.state.IBlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(BlockRendererDispatcher.class)
public abstract class MixinBlockRendererDispatcher implements Globals
{

    @Shadow public abstract boolean renderBlock(IBlockState state, BlockPos pos, IBlockAccess blockAccess, BufferBuilder bufferBuilderIn);

    @Shadow public abstract IBakedModel getModelForState(IBlockState state);

    @Shadow @Final private BlockModelRenderer blockModelRenderer;

    @Inject(method = { "renderBlock" }, at = { @At("HEAD") }, cancellable = true)
    public void renderBlock(final IBlockState state, final BlockPos pos, final IBlockAccess blockAccess, final BufferBuilder bufferBuilderIn, final CallbackInfoReturnable<Boolean> info) {
        RustXray xray = PigPack.getModuleManager().get(RustXray.class);
        if (xray.isToggled()) {
            if (mc.world.getBlockState(pos).getBlock().equals(Blocks.STAINED_HARDENED_CLAY) || mc.world.getBlockState(pos).getBlock().equals(Blocks.HARDENED_CLAY)) {
                final IBakedModel model = this.getModelForState(state);
                info.setReturnValue(this.blockModelRenderer.renderModel(blockAccess, model, state, pos, bufferBuilderIn, false));
            }
            else {
                info.setReturnValue(false);
            }
        }
    }
}