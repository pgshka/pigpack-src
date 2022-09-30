package me.pig.pack.api.managment;

import java.util.LinkedHashSet;
import java.util.Set;

public class FriendManager {

    private final Set<String> friends = new LinkedHashSet<>( );

    public Set<String> get( ) {return friends;}

    public void add( String name ) {
        friends.add( name );
    }

    public void del( String name ) {
        friends.remove( name );
    }

    public boolean is( String name ) {
        return friends.stream( ).anyMatch( friend -> friend.equalsIgnoreCase( name ) );
    }

}
