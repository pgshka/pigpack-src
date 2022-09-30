package me.pig.pack.impl.module.movement;

import loading.events.MoveEvent;
import loading.events.PacketEvent;
import me.pig.pack.PigPack;
import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.module.Module;
import me.pig.pack.utils.RenderUtil;
import me.pig.pack.utils.RotationUtil;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.LinkedList;

@Module.Manifest(name = "AutoPeek", cat = Module.Category.MOVEMENT)
public class AutoPeek extends Module {
    private final Setting<Number> speed = register("Speed", 1, 1, 10, 0.01);
    private final Setting<Boolean> useTimer = register("Use Timer", false);
    private final Setting<Number> timer = register("Timer", 1., 0.1, 10., 0.1);
    private final Setting<Number> safeDistance = register("Safe Distance", 4, 1, 32, .1);
    private final Setting<Boolean> fakelag = register("FakeLag", false);

    private final LinkedList<Packet<?>> packets = new LinkedList<>();
    private Vec3d start;
    private boolean back;

    @Override protected void onEnable() {
        if (nullCheck() || !mc.player.onGround) {
            setToggled(false);
            return;
        }
        back = false;
        start = mc.player.getPositionVector();
        packets.clear();
    }

    @Override protected void onDisable() {
        PigPack.TIMER_VALUE = 1;
    }

    @Override public void onTick() {
        if (back) {
            if (mc.player.getDistanceSq(start.x, start.y, start.z) <= 0.05) {
                setToggled(false);
                return;
            }
            if (useTimer.getValue()) {
                PigPack.TIMER_VALUE = timer.getValue().floatValue();
            }
            mc.player.moveRelative(0, 0, 1, 0);
        }
    }

    @SubscribeEvent public void onMove(MoveEvent e) {
        float[] rot = RotationUtil.calc(start);
        if (back) {
            if (!packets.isEmpty() && fakelag.getValue()) {
                while (!packets.isEmpty()) {
                    mc.player.connection.sendPacket(packets.getFirst()); ///< front
                    packets.removeFirst(); ///< pop
                }
            }
            e.motionX = speed.getValue().doubleValue() * .1 * -Math.sin(Math.toRadians(rot[0]));
            e.motionZ = speed.getValue().doubleValue() * .1 * Math.cos(Math.toRadians(rot[0]));
        } else if (mc.player.getDistanceSq(start.x, start.y, start.z) > safeDistance.getValue().doubleValue() * safeDistance.getValue().doubleValue()) {
            back = true;
        }
    }

    @SubscribeEvent public void onPacketSend(PacketEvent.Send event) {
        if (event.getPacket() instanceof CPacketAnimation) {
            back = true;
        }

        if (fakelag.getValue() && (event.getPacket() instanceof CPacketPlayer || event.getPacket() instanceof CPacketConfirmTeleport) && !back) {
            event.setCanceled(true);
            packets.add(event.getPacket());
        }

    }

    @Override public void onRender3D(float partialTicks) {
        if (start != null) {
            RenderUtil.drawBoxESP(new AxisAlignedBB(start.add(-.5, 0, -.5), start.add(.5, 0.1, .5)), back ? new Color(0, 255, 0) : new Color(255, 0, 0), 1, true, true, 120, 255);
        }
    }
}
