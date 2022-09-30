package me.pig.pack.impl.command.impl;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import me.pig.pack.impl.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.pig.pack.utils.ChatUtil;
import net.minecraft.util.Session;
import java.net.Proxy;
import java.util.Arrays;

public class HelpCommand extends Command {
    public HelpCommand() {
        super("help", "h");
    }

    @Override
    public void exec(String[] str) {
        ChatUtil.sendMessage("\n\n" +
            ChatFormatting.RED + " - Prefix: $"
            + "\n\n"
            + ChatFormatting.LIGHT_PURPLE + ChatFormatting.BOLD + "$friend add|del <name> " + ChatFormatting.LIGHT_PURPLE + ": add or delete from your friend manager\n"
            + ChatFormatting.LIGHT_PURPLE + ChatFormatting.BOLD +" $name <your_nick> " + ChatFormatting.LIGHT_PURPLE + ": changes your nickname right in the game\n"
            + ChatFormatting.LIGHT_PURPLE + ChatFormatting.BOLD +" $rpc off|on " + ChatFormatting.LIGHT_PURPLE + ": disable or enable DiscordRPC\n"
            + "\n"
        );
    }
}