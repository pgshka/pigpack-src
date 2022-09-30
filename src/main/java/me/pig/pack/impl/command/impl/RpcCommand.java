package me.pig.pack.impl.command.impl;

import me.pig.pack.impl.Discord;
import me.pig.pack.impl.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.pig.pack.utils.ChatUtil;

import java.util.Arrays;

public class RpcCommand extends Command {
    public RpcCommand() {
        super("rpc <on|off>", "rpc");
    }

    @Override
    public void exec(String[] str) {
        str = Arrays.copyOfRange(str, 1, str.length);
        if (str.length < 0) {
            printUsage();
        } else {
            if (str[0].equalsIgnoreCase("on")){
                ChatUtil.sendMessage(ChatFormatting.LIGHT_PURPLE + "DiscordRPC started");
                Discord.start();
            }
            if (str[0].equalsIgnoreCase("off")){
                ChatUtil.sendMessage(ChatFormatting.LIGHT_PURPLE + "DiscordRPC stopped");
                Discord.stop();
            }
        }
    }
}