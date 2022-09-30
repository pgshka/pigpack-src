package loading.mixins;

import com.mojang.realmsclient.gui.ChatFormatting;
import loading.events.CameraEvent;
import me.pig.pack.PigPack;
import me.pig.pack.impl.module.misc.FreeCam;
import me.pig.pack.impl.module.render.Ambience;
import me.pig.pack.impl.module.render.NoRender;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import me.pig.pack.impl.module.render.NameTags;

import javax.vecmath.Vector3f;
import java.awt.*;

@Mixin( EntityRenderer.class )
public class MixinEntityRenderer {

    @Shadow
    @Final
    private int[] lightmapColors;

    @Inject( method = "drawNameplate", at = @At( "HEAD" ), cancellable = true )
    private static void drawNameplate(FontRenderer fontRendererIn, String str, float x, float y, float z, int verticalShift, float viewerYaw, float viewerPitch, boolean isThirdPersonFrontal, boolean isSneaking, CallbackInfo callbackInfo ) {
        if ( PigPack.getModuleManager( ).get( NameTags.class ).isToggled( ) ) {
            String cope = ChatFormatting.stripFormatting(str);
            if (cope.equalsIgnoreCase("\u2739") || cope.equalsIgnoreCase("\u2718")) return;
            callbackInfo.cancel( );
        }
    }

    @ModifyArg( method = "renderWorldPass(IFJ)V", at = @At( value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderGlobal;setupTerrain(Lnet/minecraft/entity/Entity;DLnet/minecraft/client/renderer/culling/ICamera;IZ)V" ) )
    public boolean isAlsoSpectator( boolean b ) {
        return b || PigPack.getModuleManager().get(FreeCam.class).isToggled();
    }

    @Inject( method = "updateLightmap", at = @At( value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/DynamicTexture;updateDynamicTexture()V", shift = At.Shift.BEFORE ) )
    private void updateTextureHook(float partialTicks, CallbackInfo ci) {
        Ambience ambience = ( Ambience ) PigPack.getModuleManager().get(Ambience.class);
        if (ambience.isToggled()) {
            for (int i = 0; i < this.lightmapColors.length; ++i) {
                Color ambientColor = ambience.color.getValue();
                int alpha = ambientColor.getAlpha();
                float modifier = ( float ) alpha / 255.0f;
                int color = this.lightmapColors[ i ];
                int[] bgr = toRGBAArray(color);
                Vector3f values = new Vector3f(( float ) bgr[ 2 ] / 255.0f, ( float ) bgr[ 1 ] / 255.0f, ( float ) bgr[ 0 ] / 255.0f);
                Vector3f newValues = new Vector3f(( float ) ambientColor.getRed() / 255.0f, ( float ) ambientColor.getGreen() / 255.0f, ( float ) ambientColor.getBlue() / 255.0f);
                Vector3f finalValues = mix(values, newValues, modifier);
                int red = ( int ) (finalValues.x * 255.0f);
                int green = ( int ) (finalValues.y * 255.0f);
                int blue = ( int ) (finalValues.z * 255.0f);
                this.lightmapColors[ i ] = 0xFF000000 | red << 16 | green << 8 | blue;
            }
        }
    }

    @Inject( method = { "hurtCameraEffect" }, at = { @At( value = "HEAD" ) }, cancellable = true )
    public void hurtCameraEffectHook(float ticks, CallbackInfo info) {
        NoRender nr = PigPack.getModuleManager().get(NoRender.class);
        if (nr.isToggled()) info.cancel();
    }

    private int[] toRGBAArray(int colorBuffer) {
        return new int[] { colorBuffer >> 16 & 0xFF, colorBuffer >> 8 & 0xFF, colorBuffer & 0xFF };
    }

    private Vector3f mix(Vector3f first, Vector3f second, float factor) {
        return new Vector3f(first.x * (1.0f - factor) + second.x * factor, first.y * (1.0f - factor) + second.y * factor, first.z * (1.0f - factor) + first.z * factor);
    }

    @ModifyVariable( method = "orientCamera", at = @At( "STORE" ), ordinal = 3 )
    private double orientCameraX(double distance) {
        CameraEvent event = new CameraEvent(distance);
        MinecraftForge.EVENT_BUS.post(event);
        return event.distance;
    }

    @ModifyVariable( method = "orientCamera", at = @At( "STORE" ), ordinal = 7 )
    private double orientCameraZ(double distance) {
        CameraEvent event = new CameraEvent(distance);
        MinecraftForge.EVENT_BUS.post(event);
        return event.distance;
    }
}
