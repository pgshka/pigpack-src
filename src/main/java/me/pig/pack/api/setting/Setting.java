package me.pig.pack.api.setting;

import me.pig.pack.impl.module.Module;

import java.util.List;

public class Setting<T> {

    private String name;
    private T value;
    private Type type;
    private Module parent;
    private double min, max, inc;
    private List<String> Clients;
    private boolean rainbow;

    public Setting( String name, Module module, T value ) {
        this.name = name;
        this.parent = module;
        this.value = value;
        this.type = Type.B;
    }

    public Setting( String name, Module module, T value, double min, double max, double inc ) {
        this.name = name;
        this.parent = module;
        this.value = value;
        this.min = min;
        this.max = max;
        this.inc = inc;
        this.type = Type.N;
    }

    public Setting( String name, Module module, T value, List<String> Clients ) {
        this.name = name;
        this.parent = module;
        this.value = value;
        this.Clients = Clients;
        this.type = Type.M;
    }

    public Setting( String name, Module module, T value, boolean rainbow ) {
        this.name = name;
        this.parent = module;
        this.value = value;
        this.rainbow = rainbow;
        this.type = Type.C;
    }

    public String getName( ) {return name;}

    public Module getParent( ) {return parent;}

    public T getValue( ) {
        return value;
    }

    public void setValue( T value ) {this.value = value;}
    public void setMax( double max ) {this.max = max;}

    public double getInc( ) {return inc;}

    public double getMax( ) {return max;}

    public Type getType( ) {return type;}

    public double getMin( ) {return min;}

    public List<String> getClients( ) {return Clients;}

    public boolean isRainbow( ) {return rainbow;}

    public void setRainbow( boolean rainbow ) {this.rainbow = rainbow;}

    public enum Type {
        B, N, M, C
    }
}
