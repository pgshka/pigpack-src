package me.pig.pack.impl.module.misc;

import loading.events.PacketEvent;
import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.module.Module;
import me.pig.pack.utils.EntityUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.client.event.PlayerSPPushOutOfBlocksEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Module.Manifest(name = "FreeCam", cat = Module.Category.MISC, exception = true)
public class FreeCam extends Module {
    private final Setting<Number> speed = register("Speed", 1., 0.1, 10., 0.1);

    private EntityPlayer fakePlayer;
    public Setting<Boolean> view;
    public Setting<Boolean> packet;
    public Setting<Boolean> disable;
    private AxisAlignedBB oldBoundingBox;
    private EntityOtherPlayerMP entity;
    private Vec3d position;
    private Entity riding;
    private float yaw;
    private float pitch;
    
    @Override
    public void onEnable() {
        if (nullCheck()) {
            oldBoundingBox = mc.player.getEntityBoundingBox();
            mc.player.setEntityBoundingBox(new AxisAlignedBB(mc.player.posX, mc.player.posY, mc.player.posZ, mc.player.posX, mc.player.posY, mc.player.posZ));
            if (mc.player.getRidingEntity() != null) {
                riding = mc.player.getRidingEntity();
                mc.player.dismountRidingEntity();
            }
            (entity = new EntityOtherPlayerMP((World)mc.world, mc.getSession().getProfile())).copyLocationAndAnglesFrom((Entity)mc.player);
            entity.rotationYaw = mc.player.rotationYaw;
            entity.rotationYawHead = mc.player.rotationYawHead;
            entity.inventory.copyInventory(mc.player.inventory);
            mc.world.addEntityToWorld(69420, (Entity)entity);
            position = mc.player.getPositionVector();
            yaw = mc.player.rotationYaw;
            pitch = mc.player.rotationPitch;
            mc.player.noClip = true;
        }
    }

    @Override
    public void onDisable() {
        if (nullCheck()) {
            mc.player.setEntityBoundingBox(oldBoundingBox);
            if (riding != null) {
                mc.player.startRiding(riding, true);
            }
            if (entity != null) {
                mc.world.removeEntity((Entity)entity);
            }
            if (position != null) {
                mc.player.setPosition(position.x, position.y, position.z);
            }
            mc.player.rotationYaw = yaw;
            mc.player.rotationPitch = pitch;
            mc.player.noClip = false;
        }
    }

    @Override
    public void onTick() {
        mc.player.noClip = true;
        mc.player.setVelocity(0.0, 0.0, 0.0);
        mc.player.jumpMovementFactor = (float) 0.3;
        final double[] dir = EntityUtil.directionSpeed(0.3);
        if (mc.player.movementInput.moveStrafe != 0.0f || mc.player.movementInput.moveForward != 0.0f) {
            mc.player.motionX = dir[0];
            mc.player.motionZ = dir[1];
        }
        else {
            mc.player.motionX = 0.0;
            mc.player.motionZ = 0.0;
        }
        mc.player.setSprinting(false);
        if (mc.gameSettings.keyBindJump.isKeyDown()) {
            final EntityPlayerSP player = mc.player;
            player.motionY += 0.3;
        }
        if (mc.gameSettings.keyBindSneak.isKeyDown()) {
            final EntityPlayerSP player2 = mc.player;
            player2.motionY -= 0.3;
        }
    }

    @SubscribeEvent
    public void onPacketSend(final PacketEvent.Send event) {
        if ((event.getPacket() instanceof CPacketPlayer || event.getPacket() instanceof CPacketInput)) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPush(final PlayerSPPushOutOfBlocksEvent event) {
        event.setCanceled(true);
    }

}
