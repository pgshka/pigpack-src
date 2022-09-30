package me.pig.pack.impl.module.render;

import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.module.Module;
import me.pig.pack.utils.RenderUtil;
import net.minecraft.block.BlockChest;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import java.awt.*;

@Module.Manifest( name = "ChestESP", desc = "", cat = Module.Category.RENDER )
public class ChestESP extends Module {
    public final Setting<Color> trapChest = this.register("Trap Chest", new Color(255, 234, 0, 255), false);
    public final Setting<Color> basicChest = this.register("Basic Chest", new Color(255, 54, 54, 255), false);
    @Override
    public void onRender3D(float partialTicks) {
        for (TileEntity entity : ChestESP.mc.world.loadedTileEntityList) {
            if (!(entity instanceof TileEntityChest) && !(entity instanceof TileEntityChest)) continue;
            if (((TileEntityChest)((Object)entity)).getChestType().equals(BlockChest.Type.TRAP)){
                RenderUtil.drawBoxESP(mc.world.getBlockState(entity.getPos()).getSelectedBoundingBox(mc.world, entity.getPos()), trapChest.getValue(), (float) 1.5, false, true, trapChest.getValue().getAlpha(), trapChest.getValue().getAlpha());
            } else {
                RenderUtil.drawBoxESP(mc.world.getBlockState(entity.getPos()).getSelectedBoundingBox(mc.world, entity.getPos()), basicChest.getValue(), (float) 1.5, false, true, basicChest.getValue().getAlpha(), basicChest.getValue().getAlpha());
            }
        }
    }
}