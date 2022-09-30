package loading.mixins;

import me.pig.pack.PigPack;
import me.pig.pack.impl.module.render.Chams;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import me.pig.pack.api.Globals;

import static org.lwjgl.opengl.GL11.*;


@Mixin(value={RenderLivingBase.class})
public abstract class MixinRenderLivingBase<T extends EntityLivingBase>
        extends Render<T> implements Globals {

    private static ResourceLocation RES_ITEM_GLINT;

    @Shadow protected boolean renderMarker;

    public MixinRenderLivingBase(RenderManager renderManagerIn, ModelBase ModelBaseIn, float shadowSizeIn) {
        super(renderManagerIn);
    }

    @Redirect(method={"renderModel"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/model/ModelBase;render(Lnet/minecraft/entity/Entity;FFFFFF)V"))
    private void renderClientlHook(ModelBase ClientlBase, Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
        Chams chams = PigPack.getModuleManager().get(Chams.class);
        if (chams.isToggled() && entityIn instanceof EntityPlayer) {
            if (!chams.hidePlayer.getValue()) ClientlBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            float[] rgba = chams.color.getValue().getRGBComponents(null);
            float[] outRgba = chams.outColor.getValue().getRGBComponents(null);
            float[] glintRgba = chams.glintColor.getValue().getRGBComponents(null);
            float[] xqzRgba = chams.xqzColor.getValue().getRGBComponents(null);
            glPushAttrib(1048575);
            glDisable(3008);
            glDisable(3553);
            glDisable(2896);
            glEnable(3042);
            glBlendFunc(770, 771);
            glLineWidth(1.5f);
            glEnable(2960);
            if (chams.xqz.getValue()) {
                glDisable(2929);
                glDepthMask(false);
                glEnable(10754);
                glColor4f(xqzRgba[0], xqzRgba[1], xqzRgba[2], xqzRgba[3]);
                ClientlBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            }
            if (chams.visible.getValue()) {
                glEnable(2929);
                glDepthMask(true);
                glColor4f(rgba[0], rgba[1], rgba[2], rgba[3]);
                ClientlBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
            }
            glDisable(2929);
            glDepthMask(false);
            glEnable(10754);

            if (chams.outline.getValue()) {
                glEnable(GL_LINE_SMOOTH);
                glHint(GL_LINE_SMOOTH_HINT, GL_FASTEST);
                glLineWidth(chams.outWidth.getValue().floatValue());
                glColor4f(outRgba[0], outRgba[1], outRgba[2], outRgba[3]);
                glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                ClientlBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
                glDisable(GL_LINE_SMOOTH);
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            }

            if (chams.glint.getValue()) {
                glPushMatrix();
                glPushAttrib(1048575);
                glDepthRange(0, 0.1);
                glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                glDisable(GL_LIGHTING);
                glEnable(GL_BLEND);
                glColor4f(glintRgba[0], glintRgba[1], glintRgba[2], glintRgba[3]);

                GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE);

                mc.getTextureManager().bindTexture(getResItemGlint());
                ClientlBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

                glDepthRange(0, 1);
                glDisable(GL_BLEND);
                glEnable(GL_LIGHTING);

                glPopAttrib();
                glPopMatrix();
            }

            glEnable(3042);
            glEnable(2896);
            glEnable(3553);
            glEnable(3008);
            glPopAttrib();
        } else {
            ClientlBase.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    private static ResourceLocation getResItemGlint() {
        if (RES_ITEM_GLINT == null) {
            RES_ITEM_GLINT =  new ResourceLocation("textures/misc/enchanted_item_glint.png");
        }
        return RES_ITEM_GLINT;
    }
}