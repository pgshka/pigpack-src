package me.pig.pack.impl.module.render;

import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderPearl;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.*;
import java.util.List;


@Module.Manifest( name = "PearlTracker", desc = "Draws pearl's trajectory", cat = Module.Category.RENDER )
public class PiarlTricker extends Module {

    private final Setting<Boolean> render = register("Render", false);
    private final Setting<Number> thick = register("Thick", 3,0.1,10, 0.1);
    private final Setting<Number> aliveTime = register("Time", 5,0,20, 1);
    private final Setting<Color> color = register( "Color", new Color( 255, 255, 255 ), false );

    private final HashMap<UUID, List<Vec3d>> poses = new HashMap<>();
    private final HashMap<UUID, Double> time = new HashMap<>();
    int rdelay = 120;
    @Override public void onTick(){

        UUID toRemove = null;
        for (UUID uuid : time.keySet()) {
            if (time.get(uuid) <= 0) {
                poses.remove(uuid);
                toRemove = uuid;
            } else {
                time.replace(uuid, time.get(uuid) - 0.05);
            }
        }
        if (toRemove != null) {
            time.remove(toRemove);
        }

        for (Entity e : mc.world.getLoadedEntityList()) {
            if (!(e instanceof EntityEnderPearl)) continue;
            if (!this.poses.containsKey(e.getUniqueID())) {
                this.poses.put(e.getUniqueID(), new ArrayList<>(Collections.singletonList(e.getPositionVector())));
                this.time.put(e.getUniqueID(), aliveTime.getValue().doubleValue());
            } else {
                this.time.replace(e.getUniqueID(), aliveTime.getValue().doubleValue());
                List<Vec3d> v = this.poses.get(e.getUniqueID());
                v.add(e.getPositionVector());
            }
        }
    }

    @SubscribeEvent public void onRenderWorld(RenderWorldLastEvent event ) {
        if (!render.getValue() && !poses.isEmpty()) return;
        GL11.glPushMatrix();
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(thick.getValue().floatValue());
        for (UUID uuid : poses.keySet()) {
            if (poses.get(uuid).size() <= 2) continue;
            int delay = 0;
            GL11.glBegin(1);
            for (int i = 1; i < poses.get(uuid).size(); ++i) {
                delay += rdelay;
                Color c = color.getValue();
                GL11.glColor4d(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
                List<Vec3d> pos = poses.get(uuid);
                GL11.glVertex3d(pos.get(i).x - mc.getRenderManager().viewerPosX, pos.get(i).y - mc.getRenderManager().viewerPosY, pos.get(i).z - mc.getRenderManager().viewerPosZ);
                GL11.glVertex3d(pos.get(i - 1).x - mc.getRenderManager().viewerPosX, pos.get(i - 1).y - mc.getRenderManager().viewerPosY, pos.get(i - 1).z - mc.getRenderManager().viewerPosZ);
            }
            GL11.glEnd();
        }
        GL11.glEnable(3553);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
}