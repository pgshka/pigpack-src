package me.pig.pack.impl.command;

import me.pig.pack.api.managment.CommandManager;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.pig.pack.api.Globals;
import me.pig.pack.utils.ChatUtil;

public abstract class Command implements Globals {

    private final String name, syntax;
    private final String[] alias;

    public Command ( String syntax, String... alias ) {
        this.alias = alias;
        this.syntax = syntax;
        this.name = alias[ 0 ];
    }

    public String getName ( ) { return name; }

    public String[] getAlias ( ) { return alias; }

    public String getSyntax ( ) { return syntax; }

    public abstract void exec ( String[] args );

    protected void say(String text) {
        ChatUtil.sendMessage(ChatFormatting.YELLOW + text);
    }

    protected void printUsage() {
        ChatUtil.sendMessage( String.format( "Usage: %s%s", CommandManager.prefix, syntax ) );
    }

}
