package  me.pig.pack.impl.module.render;

import me.pig.pack.PigPack;
import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.module.Module;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.projectile.EntityEgg;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;


import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Module.Manifest( name = "Bullets", desc = "Draws arrow's trajectory", cat = Module.Category.RENDER )
public class BulletTracker extends Module {

    private final Setting<Number> aliveTime = register( "Time", 5, 0, 20, 1 );
    private final Setting<Number> thickness = register( "Thickness", 1f, 0.1f, 2f, 0.1 );
    private final Setting<Boolean> fade = register( "Fade", true );
    private final Setting<Color> color = register( "Color", new Color( 255, 255, 255 ), false );
    private final List<Bullet> bullets = new CopyOnWriteArrayList<>( );

    @Override public void onTick( ) {
        if ( nullCheck( ) ) return;
        info = String.valueOf( bullets.size( ) );
        bullets.forEach( bullet -> {
            if ( bullet.time <= 0 ) {
                if ( !fade.getValue( ) ) {
                    bullets.remove( bullet );
                } else {
                    bullet.update( );
                }
            }
            bullet.time -= 0.05;
        } );

        mc.world.loadedEntityList.stream( ).filter( EntityEgg.class::isInstance ).forEach( e -> {
            Bullet bullet;
            if ( ( bullet = getByEntity( ( EntityEgg ) e ) ) != null ) {
                bullet.vec3d.add( e.getPositionVector( ) );
            } else {
                bullets.add( new Bullet( ( EntityEgg ) e, aliveTime.getValue( ).doubleValue( ), e.getPositionVector( ) ) );
            }
        } );
    }

    @SubscribeEvent public void onRenderWorld( RenderWorldLastEvent event ) {
        GL11.glPushMatrix( );
        GL11.glBlendFunc( 770, 771 );
        GlStateManager.enableBlend( );
        GlStateManager.disableDepth( );
        GL11.glEnable( 3042 );
        GL11.glDisable( 3553 );
        GL11.glDisable( 2929 );
        GL11.glDepthMask( false );
        GL11.glLineWidth( thickness.getValue( ).floatValue( ) );

        bullets.stream( ).filter( bullet -> bullet.vec3d.size( ) > 2 ).forEach( bullet -> {
            GL11.glBegin( 1 );
            for ( int i = 1; i < bullet.vec3d.size( ); ++i ) {
                GL11.glColor4d( color.getValue( ).getRed( ) / 255f, color.getValue( ).getGreen( ) / 255f, color.getValue( ).getBlue( ) / 255f, bullet.alpha / 255f );
                List<Vec3d> pos = bullet.vec3d;
                GL11.glVertex3d( pos.get( i ).x - mc.getRenderManager( ).viewerPosX, pos.get( i ).y - mc.getRenderManager( ).viewerPosY, pos.get( i ).z - mc.getRenderManager( ).viewerPosZ );
                GL11.glVertex3d( pos.get( i - 1 ).x - mc.getRenderManager( ).viewerPosX, pos.get( i - 1 ).y - mc.getRenderManager( ).viewerPosY, pos.get( i - 1 ).z - mc.getRenderManager( ).viewerPosZ );
            }
            GL11.glEnd( );
        } );

        GlStateManager.disableBlend( );
        GlStateManager.enableDepth( );
        GL11.glEnable( 3553 );
        GL11.glEnable( 2929 );
        GL11.glDepthMask( true );
        GL11.glDisable( 3042 );
        GL11.glPopMatrix( );
    }

    class Bullet {

        int alpha;
        EntityEgg entity;
        double time;
        List<Vec3d> vec3d;

        Bullet( EntityEgg entity, double time, Vec3d vec3d ) {
            this.entity = entity;
            this.time = time;
            this.alpha = 255;
            this.vec3d = new ArrayList<>( );
            vec3d.add( vec3d );
        }

        void update( ) {
            if ( alpha <= 0 ) bullets.remove( this );
            else {
                alpha -= Math.min( Math.max( 255f / 0.9f * PigPack.getFpsManager( ).getFrametime( ), 0 ), 255 );
            }
        }

    }

    private Bullet getByEntity( EntityEgg EntityEgg ) {
        return bullets.stream( ).filter( e -> e.entity == EntityEgg ).findFirst( ).orElse( null );
    }

}