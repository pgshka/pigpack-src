package me.pig.pack.impl.command.impl;

import me.pig.pack.PigPack;
import me.pig.pack.impl.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;

import java.util.Arrays;

public class FriendCommand extends Command {
    public FriendCommand() {
        super("friend <add|del> <name>", "f");
    }

    @Override
    public void exec(String[] str) {
        str = Arrays.copyOfRange(str, 1, str.length);
        if (str.length < 2) {
             printUsage();
        } else {
            if (str[0].equalsIgnoreCase("add")){
                if (!PigPack.getFriendManager().is(str[1])){
                    say(String.format("%s%s%s %sadded %sto friends manager", ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD, str[1], ChatFormatting.GREEN, ChatFormatting.LIGHT_PURPLE));
                    PigPack.getFriendManager().add(str[1]);
                } else say(ChatFormatting.LIGHT_PURPLE + "already friended");
            }
            if (str[0].equalsIgnoreCase("del")){
                if (PigPack.getFriendManager().is(str[1])){
                    say(String.format("%s%s%s %sdeleted %sfrom friends manager", ChatFormatting.LIGHT_PURPLE, ChatFormatting.BOLD, str[1], ChatFormatting.RED, ChatFormatting.LIGHT_PURPLE));
                    PigPack.getFriendManager().del(str[1]);
                } else say(ChatFormatting.LIGHT_PURPLE + "not a friend anymore");
            }
        }
    }
}