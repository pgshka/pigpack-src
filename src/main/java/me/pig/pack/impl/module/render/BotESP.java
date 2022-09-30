package me.pig.pack.impl.module.render;

import me.pig.pack.impl.module.Module;
import me.pig.pack.api.setting.Setting;
import me.pig.pack.utils.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

import java.awt.*;

@Module.Manifest( name = "BotESP", desc = "", cat = Module.Category.RENDER )
public class BotESP extends Module {
    public final Setting<Color> defaultBot = register( "Default Bot", new Color(0, 255, 208, 140), false );
    public final Setting<Color> eliteBot = register( "Elite Bot", new Color(255, 219, 0, 140), false );

    private final Setting<Number> lineWidth = register("LineWidth", 0.1, 0.1, 2, 0.1);
    private final Setting<Boolean> fill = register("Fill", true);
    private final Setting<Boolean> outline = register("Outline", true);
    @Override
    public void onRender3D(float partialTicks) {

        for (Entity entity : mc.world.loadedEntityList){
            if (entity == null) return;
            if (entity instanceof EntityZombie) RenderUtil.drawBoxESP( entity.getEntityBoundingBox(), defaultBot.getValue(), lineWidth.getValue().floatValue(), outline.getValue(), fill.getValue(), defaultBot.getValue().getAlpha(), defaultBot.getValue().getAlpha());
            if (entity instanceof EntityPigZombie) RenderUtil.drawBoxESP( entity.getEntityBoundingBox(), eliteBot.getValue(), lineWidth.getValue().floatValue(), outline.getValue(), fill.getValue(), eliteBot.getValue().getAlpha(), eliteBot.getValue().getAlpha());
        }
    }
}