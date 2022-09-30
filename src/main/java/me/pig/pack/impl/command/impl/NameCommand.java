package me.pig.pack.impl.command.impl;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import me.pig.pack.impl.command.Command;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.pig.pack.utils.ChatUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.net.Proxy;
import java.util.Arrays;

public class NameCommand extends Command {
    public NameCommand() {
        super("name <nickname>", "name");
    }

    @Override
    public void exec(String[] str) {
        str = Arrays.copyOfRange(str, 1, str.length);
        if (str.length < 0) {
            printUsage();
        } else {
            YggdrasilAuthenticationService service = new YggdrasilAuthenticationService(Proxy.NO_PROXY, "");
            YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication)((Object)service.createUserAuthentication(Agent.MINECRAFT));
            auth.logOut();
            Session session = new Session(str[0], str[0], "0", "legacy");
            try {
                setSession(session);
                ChatUtil.sendMessage(ChatFormatting.LIGHT_PURPLE + "name changed to - " + ChatFormatting.DARK_PURPLE + ChatFormatting.BOLD + str[0]);
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    public void setSession(Session s){
        Class<?> mc = Minecraft.getMinecraft().getClass();
        try {
            AccessibleObject session = null;
            for (Field f : mc.getDeclaredFields()) {
                if (!f.getType().isInstance(s)) continue;
                session = f;
            }
            if (session == null) {
                throw new IllegalStateException("Session Null");
            }
            session.setAccessible(true);
            ((Field)session).set(Minecraft.getMinecraft(), s);
            session.setAccessible(false);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}