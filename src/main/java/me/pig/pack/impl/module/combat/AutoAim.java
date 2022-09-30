package me.pig.pack.impl.module.combat;

import me.pig.pack.PigPack;
import me.pig.pack.api.Globals;
import me.pig.pack.impl.ui.interwebz.Screen;
import me.pig.pack.utils.MathUtil;
import me.pig.pack.utils.RenderUtil;
import net.minecraft.client.gui.*;
import net.minecraft.entity.Entity;
import me.pig.pack.impl.module.Module;
import me.pig.pack.api.setting.Setting;
import me.pig.pack.utils.RotationUtil;
import me.pig.pack.utils.Timer;
import net.minecraft.client.gui.advancements.GuiScreenAdvancements;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.Arrays;

@Module.Manifest( name = "AutoAim", desc = "", cat = Module.Category.COMBAT )
public class AutoAim extends Module {

    //JDY OBSER MOEGO MEGA CODA

    public EntityPlayer target = null;
    public final Setting<String> mode = register( "Mode", "Head", Arrays.asList( "Head", "Body", "Boots", "Neck" ) );
    public final Setting<String> rotateMode = register( "Rotate Mode", "Normal", Arrays.asList( "Normal", "Silent" ) );
    public final Setting<String> predictMode = register( "Predict Mode", "Old", Arrays.asList( "Old", "New", "Calculate" ) );
    public final Setting<Number> fov = register("Fov", 1, 1, 150, 0.1);
    public final Setting<Number> range = register( "Range", 20, 1, 30, 1);
    public final Setting<Number> predict = register( "Predict", 3, 0, 6, 0.1);
    public final Setting<Number> calculate = register( "calculate", 6, 0, 6, 1);
    public final Setting<Boolean> selfPredict = register( "Self Predict", false );
    public final Setting<Boolean> autoShot = register( "Auto Shot", true );
    public final Setting<Number> shotDelay = register( "Shot Delay", 1, 0, 10, 0.1);
    public final Setting<Boolean> weaponCheck = register( "Weapon Check", true );
    public final Setting<Boolean> fovRender = register( "Fov Render", false );
    public final Setting<Boolean> ridingCheck = register( "Riding Check", false );
    public final Setting<Boolean> distCheck = register( "Distance Check", false );

    public static float[] getPredict(Entity entity, float predict){
        double xD = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * (double)predict;
        double zD = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * (double)predict;
        double predictX = mc.player.posX;
        double predictZ = mc.player.posZ;
        float[] rotate = RotationUtil.getNeededRotations((float)xD, (float)entity.posY + (double)(entity.getEyeHeight() + 0.1f), (float)zD, (float)predictX, (float)mc.player.posY, (float)predictZ);
        float yaw = MathUtil.lerp(mc.player.rotationYaw, rotate[0], 1.0f);
        float pitch = MathUtil.lerp(mc.player.rotationPitch, rotate[1], 1.0f);
        return new float[] {yaw,pitch};
    }
    public Timer timer = new Timer();
    @Override
    public void onEnable(){
        target = null;
        timer.reset();
    }

    public static boolean inFov(Entity en, float angle) {
        double x = en.posX - mc.player.posX;
        double z = en.posZ - mc.player.posZ;
        double yaw = Math.atan2(x, z) * 57.29577951308232D;
        yaw = -yaw;
        angle = (float)(angle * 0.5D);
        double angleDifference = ((mc.player.rotationYaw - yaw) % 360.0D + 540.0D) % 360.0D - 180.0D;
        return ((angleDifference > 0.0D) && (angleDifference < angle)) || ((-angle < angleDifference) && (angleDifference < 0.0D));
    }

    public EntityPlayer get(){
        EntityPlayer target = null;
        for (EntityPlayer entity : mc.world.playerEntities) {
            if (inFov(entity, fov.getValue().intValue()) && mc.player.getDistance(entity) < range.getValue().intValue() * 10 && entity != mc.player && entity.getHealth() > 0.0F && !PigPack.getFriendManager().is(entity.getName()) && !entity.isInvisible() && entity.canEntityBeSeen(mc.player)){
                target = entity;
            }
        }
        return target;
    }

    public float getPoint(Entity entity){
        float point = 0;
        if (mode.getValue().equalsIgnoreCase("Neck")) point = 0.18f;
        if (mode.getValue().equalsIgnoreCase("Head")) point = 0f;
        if (mode.getValue().equalsIgnoreCase("Body")) point = 0.5f;
        if (mode.getValue().equalsIgnoreCase("Boots")) point = 1.3f;
        if (distCheck.getValue()) point = point - (mc.player.getDistance(entity) / 150);
        return point;
    }

    @SubscribeEvent
    public void onRender2D(RenderGameOverlayEvent.Text event ) {
        if (nullCheck()) return;
        ScaledResolution sr = new ScaledResolution(mc);

        if (fovRender.getValue()){
            RenderUtil.drawPolygonOutline(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 3, fov.getValue().intValue(), fov.getValue().intValue(), new Color(255,255,255,255).getRGB());
        }

    }

    @Override
    public void onArtificialTick(){
        if (nullCheck()) setToggled(false);
        target = get();
        if (mc.currentScreen instanceof GuiOptions
                || mc.currentScreen instanceof GuiVideoSettings
                || mc.currentScreen instanceof GuiScreenOptionsSounds
                || mc.currentScreen instanceof GuiContainer
                || mc.currentScreen instanceof GuiIngameMenu
                || mc.currentScreen instanceof Screen
                || mc.currentScreen instanceof GuiScreenAdvancements
        ) return;
         if (target != null) {
             if (ridingCheck.getValue() && target.isRiding()) return;
             if (weaponCheck.getValue() && !(mc.player.getHeldItemMainhand().getItem().equals(Items.DIAMOND_PICKAXE)));
             float[] aim = new float[0];

             switch (predictMode.getValue()){
                 case "Old":
                     aim = RotationUtil.getRotation(target, predict.getValue().floatValue(), getPoint(target), selfPredict.getValue());
                     break;
                 case "New":
                     aim = getPredict(target, predict.getValue().floatValue());
                     break;
                 case "Calculate":
                     aim = getPredict(target, Globals.getPlayerPing() / 100 * calculate.getValue().intValue());
                     break;
             }
             if (rotateMode.getValue().equalsIgnoreCase("Normal")) {
                 mc.player.rotationYaw = aim[0];
                 mc.player.rotationPitch = aim[1];
             } else if (rotateMode.getValue().equalsIgnoreCase("Silent")){
                 mc.player.connection.sendPacket(new CPacketPlayer.Rotation(aim[0], aim[1], mc.player.onGround));
             }
             if (autoShot.getValue()){
                 if (timer.passed(shotDelay.getValue().intValue() * 100)){
                     try {
                         Robot bot = new Robot();
                         bot.mousePress(InputEvent.BUTTON1_MASK);
                         bot.mouseRelease(InputEvent.BUTTON1_MASK);
                     } catch (Exception e){}
                     timer.reset();
                 }
             }
        }
    }
}