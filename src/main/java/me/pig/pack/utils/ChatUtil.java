package me.pig.pack.utils;

import me.pig.pack.api.Globals;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class ChatUtil implements Globals {
    public static String staticName = ChatFormatting.DARK_PURPLE + "[PigPack]";
    public static void sendMessage( String text ) {
        sendMsgEvent(staticName, text, false, 1 );
    }

    public static void sendMessage( String text, Boolean silent ) {
        sendMsgEvent( staticName, text, silent, 1 );
    }
    public static void sendMessageId( String text, Boolean silent, int id ) {
        sendMsgEvent( staticName, text, silent, id );
    }

    public static void sendMsgEvent( String prefix, String text, boolean silent, int id ) {
        if ( mc.player == null ) return;
        if ( !silent ) {
            mc.ingameGUI.getChatGUI( ).printChatMessage( new TextComponentString( prefix + TextFormatting.GRAY + " " + text ) );
        }
        else {
            mc.ingameGUI.getChatGUI( ).printChatMessageWithOptionalDeletion( new TextComponentString( prefix + TextFormatting.GRAY + " " + text ), id );
        }
    }
}
