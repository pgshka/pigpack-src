package me.pig.pack.api.managment;

import me.pig.pack.impl.command.Command;
import me.pig.pack.impl.command.impl.*;
import me.pig.pack.utils.ChatUtil;

import java.util.Arrays;
import java.util.List;

public class CommandManager {

    public static String prefix = "$";
    private List<Command> commands;

    public CommandManager ( )
    {
        commands = Arrays.asList(
                new FriendCommand(),
                new ConfigCommand(),
                new DestructCommand(),
                new NameCommand(),
                new HelpCommand(),
                new RpcCommand()

        );
    }

    public List<Command> getCommands ( ) {
        return commands;
    }

    public void parseCommand ( String string ) {
        for ( Command command : getCommands( ) ) {
            for ( String name : command.getAlias() ) {
                if ( string.startsWith( name ) ) {
                    try {
                        command.exec( string.trim( ).split( " " ) );
                    } catch ( Exception e ) {
                        ChatUtil.sendMessage( String.format( "Usage: %s", command.getSyntax( ) ) );
                    }
                    return;
                }
            }
        }
        ChatUtil.sendMessage( "Unknown command.", true );
    }

    public String getPrefix( )
    {
        return prefix;
    }
}

