package me.pig.pack.impl.module.render;

import me.pig.pack.impl.module.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;

@Module.Manifest( name = "RustMeEsp", desc = "", cat = Module.Category.RENDER )
public class RustMeEsp extends Module {

    @Override public void onRender3D(float partialTicks) {
        GlStateManager.clear((int) 256);
        RenderHelper.enableStandardItemLighting();
        for (Entity entity : mc.world.loadedEntityList) {
            if (!(entity instanceof EntityArmorStand) || entity == mc.getRenderViewEntity())
                continue;
            try {
                if (entity == null || entity == mc.player) {
                    return;
                }
                if (entity == mc.getRenderViewEntity() && mc.gameSettings.thirdPersonView == 0) {
                    return;
                }
                mc.entityRenderer.disableLightmap();
                mc.getRenderManager().renderEntityStatic(entity, partialTicks, false);
                mc.entityRenderer.enableLightmap();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}