package me.pig.pack.impl.module.movement;


import loading.events.MoveEvent;
import loading.events.PacketEvent;
import loading.events.UpdateEvent;
import me.pig.pack.PigPack;
import me.pig.pack.api.Globals;
import me.pig.pack.impl.module.Module;
import me.pig.pack.utils.TimeVec3d;
import me.pig.pack.utils.Timer;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiDownloadTerrain;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import me.pig.pack.api.setting.Setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

@Module.Manifest( name = "Clipper", desc = "", cat = Module.Category.MOVEMENT  )
public class PacketFly extends Module {
    public PacketFly(){
    }

    public final Setting<String> packetMode = register( "Mode", "DOWN", Arrays.asList("DOWN") );
    public final Setting<String> phase = register( "Phase", "NCP", Arrays.asList( "NCP" ) );
    private final Setting<Boolean> luckyDayz = register("LuckyDayz", false);

    private int teleportId;
    private CPacketPlayer.Position startingOutOfBoundsPos;
    private ArrayList<CPacketPlayer> packets = new ArrayList<CPacketPlayer>();;
    private Map<Integer, TimeVec3d> posLooks = new ConcurrentHashMap<Integer, TimeVec3d>();
    private int antiKickTicks;
    private int vDelay;
    private int hDelay;
    private boolean limitStrict;
    private int limitTicks;
    private int jitterTicks;
    private boolean oddJitter;
    double speedX;
    double speedY;
    double speedZ;
    private float postYaw;
    private float postPitch;
    private int factorCounter;
    private Timer intervalTimer = new Timer();
    private static final Random random;

    @Override
    public void onTick() {
        if (Globals.mc.currentScreen instanceof GuiDisconnected || Globals.mc.currentScreen instanceof GuiMainMenu || Globals.mc.currentScreen instanceof GuiMultiplayer || Globals.mc.currentScreen instanceof GuiDownloadTerrain) {
            toggle();
        }


        PigPack.TIMER_VALUE = 1.0f;

    }

    public static boolean isPlayerMoving() {
        return Globals.mc.gameSettings.keyBindForward.isKeyDown() || Globals.mc.gameSettings.keyBindBack.isKeyDown() || Globals.mc.gameSettings.keyBindRight.isKeyDown() || Globals.mc.gameSettings.keyBindLeft.isKeyDown();
    }
    public static double[] directionSpeed(double speed) {
        float forward = Globals.mc.player.movementInput.moveForward;
        float side = Globals.mc.player.movementInput.moveStrafe;
        float yaw = Globals.mc.player.prevRotationYaw + (Globals.mc.player.rotationYaw - Globals.mc.player.prevRotationYaw) * Globals.mc.getRenderPartialTicks();

        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            } else if (side < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }

        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        final double posX = forward * speed * cos + side * speed * sin;
        final double posZ = forward * speed * sin - side * speed * cos;
        return new double[]{posX, posZ};
    }

    @SubscribeEvent
    public void onUpdate(final UpdateEvent event) {
        if (Globals.mc.player == null || Globals.mc.world == null) {
            toggle();
            return;
        }
        if (Globals.mc.player.ticksExisted % 20 == 0) {
            cleanPosLooks();
        }
        Globals.mc.player.setVelocity(0.0, 0.0, 0.0);
        if (teleportId <= 0) {
            startingOutOfBoundsPos = new CPacketPlayer.Position(randomHorizontal(), 1.0, randomHorizontal(), Globals.mc.player.onGround);
            packets.add((CPacketPlayer) startingOutOfBoundsPos);
            Globals.mc.player.connection.sendPacket((Packet) startingOutOfBoundsPos);
            return;
        }
        final boolean phasing = checkCollisionBox();
        speedX = 0.0;
        speedY = 0.0;
        speedZ = 0.0;
        if (Globals.mc.gameSettings.keyBindJump.isKeyDown() && (hDelay < 1)) {
            if (Globals.mc.player.ticksExisted % 20 == 0) {
                speedY = -0.032;
            } else speedY = 0.062;

            antiKickTicks = 0;
            vDelay = 5;
        } else if (Globals.mc.gameSettings.keyBindSneak.isKeyDown() && (hDelay < 1)) {
            speedY = -0.062;
            antiKickTicks = 0;
            vDelay = 5;
        }
        if (!Globals.mc.gameSettings.keyBindSneak.isKeyDown() || !Globals.mc.gameSettings.keyBindJump.isKeyDown()) {
            if (isPlayerMoving()) {
                final double[] dir = directionSpeed(((phasing && phase.getValue().equalsIgnoreCase("NCP")) ? 0.062 : 0.26) * 1f);
                if ((dir[0] != 0.0 || dir[1] != 0.0) && (vDelay < 1)) {
                    speedX = dir[0];
                    speedZ = dir[1];
                    hDelay = 5;
                }
            }
        }
        if (phasing && ((phase.getValue().equalsIgnoreCase("NCP") && Globals.mc.player.moveForward != 0.0) || (Globals.mc.player.moveStrafing != 0.0 && speedY != 0.0))) {
            speedY /= 2.5;
        }

        float rawFactor = luckyDayz.getValue() ? 1.7f : 1;

        int factorInt = (int) Math.floor(rawFactor);
        ++factorCounter;
        if (factorCounter > (int) (20.0 / ((rawFactor - (double) factorInt) * 20.0))) {
            ++factorInt;
            factorCounter = 0;
        }
        for (int i = 1; i <= factorInt; ++i) {
            Globals.mc.player.setVelocity(speedX * i, speedY * i, speedZ * i);
            sendPackets(speedX * i, speedY * i, speedZ * i, packetMode.getValue(), true, false);
        }
        speedX = Globals.mc.player.motionX;
        speedY = Globals.mc.player.motionY;
        speedZ = Globals.mc.player.motionZ;

        --vDelay;
        --hDelay;
        ++limitTicks;
        ++jitterTicks;
        if (limitTicks > 3) {
            limitTicks = 0;
            limitStrict = !limitStrict;
        }
        if (jitterTicks > 7) {
            jitterTicks = 0;
        }
    }

    private void sendPackets(final double x, final double y, final double z, final String Client, final boolean sendConfirmTeleport, final boolean sendExtraCT) {
        final Vec3d nextPos = new Vec3d(Globals.mc.player.posX + x, Globals.mc.player.posY + y, Globals.mc.player.posZ + z);
        final Vec3d bounds = getBoundsVec(x, y, z, Client);
        final CPacketPlayer nextPosPacket = (CPacketPlayer)new CPacketPlayer.Position(nextPos.x, nextPos.y, nextPos.z, Globals.mc.player.onGround);
        packets.add(nextPosPacket);
        Globals.mc.player.connection.sendPacket((Packet)nextPosPacket);
        final CPacketPlayer boundsPacket = (CPacketPlayer)new CPacketPlayer.Position(bounds.x, bounds.y, bounds.z, Globals.mc.player.onGround);
        packets.add(boundsPacket);
        Globals.mc.player.connection.sendPacket((Packet)boundsPacket);
        if (sendConfirmTeleport) {
            ++teleportId;
            if (sendExtraCT) {
                Globals.mc.player.connection.sendPacket((Packet)new CPacketConfirmTeleport(teleportId - 1));
            }
            Globals.mc.player.connection.sendPacket((Packet)new CPacketConfirmTeleport(teleportId));
            posLooks.put(teleportId, new TimeVec3d(nextPos.x, nextPos.y, nextPos.z, System.currentTimeMillis()));
            if (sendExtraCT) {
                Globals.mc.player.connection.sendPacket((Packet)new CPacketConfirmTeleport(teleportId + 1));
            }
        }
    }

    private Vec3d getBoundsVec(final double x, final double y, final double z, final String Mode) {
        switch (Mode) {
            case "UP":{

            }
            case "ZERO":{

            }
            case "BYPASS":{
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,mc.player.posY,mc.player.posZ, mc.player.onGround));
                mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX,mc.player.posY - 1,mc.player.posZ, mc.player.onGround));
            }
            default: {
                return new Vec3d(Globals.mc.player.posX + x, Globals.mc.player.posY - 1337.0, Globals.mc.player.posZ + z);
            }

        }
    }

    public double randomHorizontal() {
        final int randomValue = random.nextInt(29000000) + 500;
        if (random.nextBoolean()) {
            return randomValue;
        }
        return -randomValue;
    }

    public static double randomLimitedVertical() {
        int randomValue = random.nextInt(22);
        randomValue += 70;
        if (random.nextBoolean()) {
            return randomValue;
        }
        return -randomValue;
    }

    public static double randomLimitedHorizontal() {
        final int randomValue = random.nextInt(10);
        if (random.nextBoolean()) {
            return randomValue;
        }
        return -randomValue;
    }

    private void cleanPosLooks() {
        posLooks.forEach((tp, timeVec3d) -> {
            if (System.currentTimeMillis() - timeVec3d.getTime() > TimeUnit.SECONDS.toMillis(30L)) {
                posLooks.remove(tp);
            }
        });
    }

    @Override
    public void onEnable() {
        if (Globals.mc.player == null || Globals.mc.world == null) {
            toggle();
            return;
        }
        packets.clear();
        posLooks.clear();
        teleportId = 0;
        vDelay = 0;
        hDelay = 0;
        postYaw = -400.0f;
        postPitch = -400.0f;
        antiKickTicks = 0;
        limitTicks = 0;
        jitterTicks = 0;
        speedX = 0.0;
        speedY = 0.0;
        speedZ = 0.0;
        oddJitter = false;
        startingOutOfBoundsPos = null;
        startingOutOfBoundsPos = new CPacketPlayer.Position(randomHorizontal(), 1.0, randomHorizontal(), Globals.mc.player.onGround);
        packets.add((CPacketPlayer)startingOutOfBoundsPos);
        Globals.mc.player.connection.sendPacket((Packet)startingOutOfBoundsPos);
    }

    @Override
    public void onDisable() {
        if (Globals.mc.player != null) {
            Globals.mc.player.setVelocity(0.0, 0.0, 0.0);
        }
        PigPack.TIMER_VALUE = 1.0f;
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event ) {
        if (event.getPacket() instanceof SPacketPlayerPosLook) {
            if (!(Globals.mc.currentScreen instanceof GuiDownloadTerrain)) {
                final SPacketPlayerPosLook packet = event.getPacket();
                if (Globals.mc.player.isEntityAlive()) {
                    if (teleportId <= 0) {
                        teleportId = ((SPacketPlayerPosLook) event.getPacket()).getTeleportId();
                    }
                    else if (Globals.mc.world.isBlockLoaded(new BlockPos(Globals.mc.player.posX, Globals.mc.player.posY, Globals.mc.player.posZ), false)) {
                        if (posLooks.containsKey(packet.getTeleportId())) {
                            final TimeVec3d vec = posLooks.get(packet.getTeleportId());
                            if (vec.x == packet.getX() && vec.y == packet.getY() && vec.z == packet.getZ()) {
                                posLooks.remove(packet.getTeleportId());
                                event.setCanceled(true);

                                return;
                            }
                        }
                    }
                }
                //((ISPacketPlayerPosLook)packet).setYaw(mc.player.rotationYaw);
                //((ISPacketPlayerPosLook)packet).setPitch(mc.player.rotationPitch);
                packet.getFlags().remove(SPacketPlayerPosLook.EnumFlags.X_ROT);
                packet.getFlags().remove(SPacketPlayerPosLook.EnumFlags.Y_ROT);
                teleportId = packet.getTeleportId();
            }
            else {
                teleportId = 0;
            }
        }
    }

    @SubscribeEvent
    public void onMove(final MoveEvent event) {
        if (teleportId <= 0) return;


        event.motionX = speedX;
        event.motionY = speedY;
        event.motionZ = speedZ;

        if ((!phase.getValue().equalsIgnoreCase("NONE") && phase.getValue().equalsIgnoreCase("VANILLA")) || checkCollisionBox()) {
            Globals.mc.player.noClip = true;
        }
    }

    private boolean checkCollisionBox() {
        return !Globals.mc.world.getCollisionBoxes((Entity) Globals.mc.player, Globals.mc.player.getEntityBoundingBox().expand(0.0, 0.0, 0.0)).isEmpty() || !Globals.mc.world.getCollisionBoxes((Entity) Globals.mc.player, Globals.mc.player.getEntityBoundingBox().offset(0.0, 2.0, 0.0).contract(0.0, 1.99, 0.0)).isEmpty();
    }

    @SubscribeEvent public void onPacketSend( PacketEvent.Send event ) {
        if (event.getPacket() instanceof CPacketPlayer && !(event.getPacket() instanceof CPacketPlayer.Position)) {
            event.setCanceled(true);
        }
        if (event.getPacket() instanceof CPacketPlayer) {
            final CPacketPlayer packet = event.getPacket();
            if (packets.contains(packet)) {
                packets.remove(packet);
                return;
            }
            event.setCanceled(true);
        }
    }
    static {
        random = new Random();
    }

//    @SubscribeEvent
//    public void onPush(final PushEvent event) {
//        event.setCanceled(true);
//    }


}