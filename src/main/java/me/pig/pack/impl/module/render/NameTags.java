package me.pig.pack.impl.module.render;

import me.pig.pack.PigPack;
import me.pig.pack.utils.RenderUtil;
import me.pig.pack.utils.font.FontUtil;
import loading.mixins.AccessorRenderManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import me.pig.pack.api.setting.Setting;
import me.pig.pack.impl.module.Module;
import me.pig.pack.utils.EntityUtil;
import me.pig.pack.utils.MathUtil;

import java.awt.*;

@Module.Manifest( name = "NameTags", desc = "", cat = Module.Category.RENDER )
public class NameTags extends Module {

    private final Setting<Number> range = register( "Range", 80, 8, 160, 1 );
    private final Setting<Number> size = register( "Size", 0.5, 0.1, 1, 0.1 );
    private final Setting<Boolean> self = register( "Self", false );

    private final Setting<Boolean> fill = register( "Fill", true );
    private final Setting<Boolean> outline = register( "Outline", true );
    private final Setting<Number> thickness = register( "Thickness", 1, 0.1, 3.0, 1.0 );

    private final Setting<Color> fillColor = register( "Fill Color", new Color( 0x55000000, true ), false );
    private final Setting<Color> outlineColor = register( "Outline Color", new Color( 0, 0, 0, 255 ), false );
    private final Setting<Color> textColor = register( "Text Color", new Color( 255, 255, 255, 255 ), false );

    private final Setting<Boolean> name = register( "Name", true );
    private final Setting<Boolean> friend = register( "Friend", true );
    private final Setting<Boolean> ping = register( "Ping", true );
    private final Setting<Boolean> healthColor = register( "Colored Health", true );
    private final Setting<Boolean> gamemode = register( "Gamemode", false );
    private final Setting<Boolean> items = register( "Items", true );
    private final Setting<Boolean> mainhand = register( "MainHand", true );
    private final Setting<Boolean> offhand = register( "Offhand", true );
    private final Setting<Boolean> armor = register( "Armor", false );
    AccessorRenderManager renderManager = ( AccessorRenderManager ) mc.getRenderManager( );

    @SubscribeEvent public void onRender3D( RenderWorldLastEvent event ) {
        for ( Entity player : mc.world.loadedEntityList ) {
            if ( !( player instanceof EntityPlayer ) || player.isDead || !( ( ( EntityPlayer ) player ).getHealth( ) > 0.0f ) || mc.player.getDistance( player ) > range.getValue( ).doubleValue( ) )
                continue;
            if ( player == mc.player ) {
                if ( self.getValue( ) ) renderNameTage( ( EntityPlayer ) player );
                continue;
            }
            renderNameTage( ( EntityPlayer ) player );
        }
    }

    private void renderNameTage( EntityPlayer player ) {
        if ( mc.getRenderViewEntity( ) == null ) return;
        double x = MathUtil.interpolate( player.lastTickPosX, player.posX, mc.getRenderPartialTicks( ) ) - renderManager.getRenderPosX( );
        double y = MathUtil.interpolate( player.lastTickPosY, player.posY, mc.getRenderPartialTicks( ) ) - renderManager.getRenderPosY( ) + ( player.isSneaking( ) ? 0.5 : 0.7 );
        double z = MathUtil.interpolate( player.lastTickPosZ, player.posZ, mc.getRenderPartialTicks( ) ) - renderManager.getRenderPosZ( );
        double delta = mc.getRenderPartialTicks( );
        Entity localPlayer = mc.getRenderViewEntity( );
        double originalPositionX = localPlayer.posX;
        double originalPositionY = localPlayer.posY;
        double originalPositionZ = localPlayer.posZ;
        localPlayer.posX = MathUtil.interpolate( localPlayer.prevPosX, localPlayer.posX, delta );
        localPlayer.posY = MathUtil.interpolate( localPlayer.prevPosY, localPlayer.posY, delta );
        localPlayer.posZ = MathUtil.interpolate( localPlayer.prevPosZ, localPlayer.posZ, delta );
        String tag = getTagString( player );
        double distance = localPlayer.getDistance( x + mc.getRenderManager( ).viewerPosX, y + mc.getRenderManager( ).viewerPosY, z + mc.getRenderManager( ).viewerPosZ );
        int width = FontUtil.getStringWidth( tag ) >> 1;
        double scale = ( float ) ( ( ( distance / 5 <= 2 ? 2.0F : ( distance / 5 ) * ( ( size.getValue( ).floatValue( ) ) + 1 ) ) * 2.5f ) * ( size.getValue( ).floatValue( ) / 100 ) );
        if ( distance <= 8.0 ) {
            scale = 0.0245;
        }

        GlStateManager.pushMatrix( );
        RenderHelper.enableStandardItemLighting( );
        GlStateManager.enablePolygonOffset( );
        GlStateManager.doPolygonOffset( 1.0f, -1500000f );
        GlStateManager.disableLighting( );
        GlStateManager.translate( x, y + 1.4f, z );
        GlStateManager.rotate( -mc.getRenderManager( ).playerViewY, 0.0f, 1.0f, 0.0f );
        GlStateManager.rotate( mc.getRenderManager( ).playerViewX, mc.gameSettings.thirdPersonView == 2 ? -1.0f : 1.0f, 0.0f, 0.0f );
        GlStateManager.scale( -scale, -scale, scale );
        GL11.glDepthRange( 0, 0.1 );

        if ( fill.getValue( ) ) {
            RenderUtil.drawRect( -width - 2, -( this.mc.fontRenderer.FONT_HEIGHT + 1 ), ( float ) width + 2.0f, 1.5f, fillColor.getValue( ).getRGB( ) );
        }
        if ( outline.getValue( ) ) {
            RenderUtil.drawOutline( -width - 2, -( this.mc.fontRenderer.FONT_HEIGHT + 1 ), ( float ) width + 2.0f, 1.5f, thickness.getValue( ).floatValue( ), outlineColor.getValue( ).getRGB( ) );
        }

        if ( items.getValue( ) ) {
            GlStateManager.pushMatrix( );
            int xOffset = -8;
            for ( int i = 0; i < 4; ++i ) {
                xOffset -= 8;
            }
            ItemStack renderOffhand = player.getHeldItemOffhand( ).copy( );
            xOffset -= 8;
            if ( offhand.getValue( ) ) {
                renderItemStack( renderOffhand, xOffset );
                renderDurabilityLabel( renderOffhand, xOffset, -50 );
            }
            xOffset += 16;
            for ( int i = 0; i < 4; ++i ) {
                if ( armor.getValue( ) ) renderItemStack( player.inventory.armorInventory.get( i ).copy( ), xOffset );
                xOffset += 16;
            }
            if ( mainhand.getValue( ) ) {
                renderItemStack( player.getHeldItemMainhand( ).copy( ), xOffset );
                renderDurabilityLabel( player.getHeldItemMainhand( ).copy( ), xOffset, -50 );
            }
            GlStateManager.popMatrix( );
        }

        FontUtil.drawStringWithShadow( tag, -width, -8.0f, textColor.getValue( ).getRGB( ) );
        localPlayer.posX = originalPositionX;
        localPlayer.posY = originalPositionY;
        localPlayer.posZ = originalPositionZ;
        GL11.glDepthRange( 0, 1 );
        GlStateManager.disableBlend( );
        GlStateManager.disablePolygonOffset( );
        GlStateManager.doPolygonOffset( 1.0f, 1500000f );
        GlStateManager.popMatrix( );
    }

    private void renderItemStack( ItemStack stack, int x ) {
        GlStateManager.depthMask( true );
        GlStateManager.clear( 256 );
        RenderHelper.enableStandardItemLighting( );
        mc.getRenderItem( ).zLevel = -150.0f;
        GlStateManager.disableAlpha( );
        GlStateManager.enableDepth( );
        GlStateManager.disableCull( );
        mc.getRenderItem( ).renderItemAndEffectIntoGUI( stack, x, -27 );
        mc.getRenderItem( ).renderItemOverlays( this.mc.fontRenderer, stack, x, -27 );
        mc.getRenderItem( ).zLevel = 0.0f;
        RenderHelper.disableStandardItemLighting( );
        GlStateManager.enableCull( );
        GlStateManager.enableAlpha( );
        GlStateManager.scale( 0.5f, 0.5f, 0.5f );
        GlStateManager.disableDepth( );
        renderEnchantmentLabel( stack, x, -27 );
        GlStateManager.enableDepth( );
        GlStateManager.scale( 2.0f, 2.0f, 2.0f );
    }

    private void renderEnchantmentLabel( ItemStack stack, int x, int y ) {
        int enchantmentY = y - 8;
        NBTTagList enchants = stack.getEnchantmentTagList( );
        for ( int index = 0; index < enchants.tagCount( ); ++index ) {
            short id = enchants.getCompoundTagAt( index ).getShort( "id" );
            short level = enchants.getCompoundTagAt( index ).getShort( "lvl" );
            Enchantment enc = Enchantment.getEnchantmentByID( ( int ) id );
            if ( enc == null || enc.getName( ).contains( "fall" ) || !enc.getName( ).contains( "all" ) && !enc.getName( ).contains( "explosion" ) )
                continue;
            FontUtil.drawStringWithShadow( enc.isCurse( ) ? TextFormatting.RED + enc.getTranslatedName( ( int ) level ).substring( 11 ).substring( 0, 1 ).toLowerCase( ) : enc.getTranslatedName( ( int ) level ).substring( 0, 1 ).toLowerCase( ) + level, ( float ) ( x * 2 ), ( float ) enchantmentY, -1 );
            enchantmentY -= 8;
        }
    }

    private void renderDurabilityLabel( ItemStack stack, int x, int y ) {
        GlStateManager.scale( 0.5f, 0.5f, 0.5f );
        GlStateManager.disableDepth( );
        if ( stack.getItem( ) instanceof ItemArmor || stack.getItem( ) instanceof ItemSword || stack.getItem( ) instanceof ItemTool ) {
            //Definitely not pasted from xulu
            float green = ( ( float ) stack.getMaxDamage( ) - ( float ) stack.getItemDamage( ) ) / ( float ) stack.getMaxDamage( );
            float red = 1 - green;
            int dmg = 100 - ( int ) ( red * 100 );
            // ^^^
            FontUtil.drawStringWithShadow( dmg + "%", x * 2 + 4, y - 10, new Color( ( int ) ( red * 255 ), ( int ) ( green * 255 ), 0 ).getRGB( ) );
        }
        GlStateManager.enableDepth( );
        GlStateManager.scale( 2.0f, 2.0f, 2.0f );
    }

    private String getTagString( EntityPlayer player ) {
        StringBuilder sb = new StringBuilder( );

        if ( PigPack.getFriendManager( ).is( player.getName( ) ) && friend.getValue( ) ) sb.append( ChatFormatting.AQUA );

        if ( ping.getValue( ) ) {
            try {
                NetworkPlayerInfo npi = mc.player.connection.getPlayerInfo( player.getGameProfile( ).getId( ) );
                sb.append( npi.getResponseTime( ) );
            } catch ( Exception e ) {
                sb.append("0");
            }
            sb.append( "ms" );
        }
        NameProtect nameProtect = PigPack.getModuleManager().get(NameProtect.class);
        if ( name.getValue( ) ) {
            sb.append( " " ).append( nameProtect.isToggled() ? "pig_protect" : player.getName());
        }


        if ( healthColor.getValue( ) ) {
            sb.append( getHealthColor( EntityUtil.getHealth( player ) ) );
        }
        sb.append( " " ).append( ( int ) EntityUtil.getHealth( player ) ).append( ChatFormatting.RESET );


        if ( PigPack.getFriendManager( ).is( player.getName( ) ) && friend.getValue( ) ) sb.append( ChatFormatting.AQUA );

        if ( gamemode.getValue( ) ) {
            sb.append( " [" );
            try {
                String sus = getShortName( mc.player.connection.getPlayerInfo( player.getGameProfile( ).getId( ) ).getGameType( ).getName( ) );
                sb.append( sus );
            } catch ( Exception ignored ) {
                sb.append( "S" );
            }
            sb.append( "]" );
        }


        return sb.toString( );
    }

    private ChatFormatting getHealthColor( double health ) {
        if ( health >= 20 ) return ChatFormatting.GREEN;
        else if ( health >= 16 ) return ChatFormatting.DARK_GREEN;
        else if ( health >= 10 ) return ChatFormatting.GOLD;
        else if ( health >= 4 ) return ChatFormatting.RED;
        else return ChatFormatting.DARK_RED;
    }

    private String getShortName( String gameType ) {
        if ( gameType.equalsIgnoreCase( "survival" ) ) return "S";
        else if ( gameType.equalsIgnoreCase( "creative" ) ) return "C";
        else if ( gameType.equalsIgnoreCase( "adventure" ) ) return "A";
        else if ( gameType.equalsIgnoreCase( "spectator" ) ) return "SP";
        else return "NONE";
    }

}