package loading.mixins;

import me.pig.pack.PigPack;
import me.pig.pack.impl.module.misc.FreeCam;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class MixinBlock {

    @Inject( method = "getCollisionBoundingBox", at = @At( "HEAD" ), cancellable = true )
    private void getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, CallbackInfoReturnable<AxisAlignedBB> cir) {
        if(PigPack.getModuleManager().get(FreeCam.class).isToggled() )
            cir.setReturnValue( Block.NULL_AABB );
    }

}
