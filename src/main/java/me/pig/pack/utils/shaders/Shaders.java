package me.pig.pack.utils.shaders;

public enum Shaders {
    TRANS("/shaders/trans.fsh"),
    LINES("/shaders/lines.fsh"),
    TOWERS("/shaders/towers.fsh");

    String path;

    Shaders(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
