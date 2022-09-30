package me.pig.pack.impl.module.render;

import me.pig.pack.impl.module.Module;
import me.pig.pack.api.setting.Setting;
import me.pig.pack.utils.RenderUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.awt.*;

@Module.Manifest( name = "BlockOutline", desc = "", cat = Module.Category.RENDER )
public class BlockOutline extends Module {
    public final Setting<Color> color = register( "Color", new Color(255, 54, 54, 255 ), false );
    @Override
    public void onRender3D(float partialTicks) {
        RayTraceResult ray = mc.objectMouseOver;
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            BlockPos positionRender = ray.getBlockPos();
            RenderUtil.drawBoxESP( mc.world.getBlockState( positionRender ).getSelectedBoundingBox( mc.world, positionRender ), color.getValue(), (float) 1.5, true, false, color.getValue().getAlpha(), color.getValue().getAlpha());
        }
    }
}