package me.pig.pack.impl.command.impl;

import me.pig.pack.PigPack;
import me.pig.pack.impl.command.Command;
import me.pig.pack.impl.module.Module;

public class DestructCommand extends Command {
    public DestructCommand() {
        super("panic", "panic");
    }

    @Override
    public void exec(String[] str) {
        PigPack.getConfigManager().save();
        for (Module module : PigPack.getModuleManager().get()){
            if (module.isToggled()) module.setToggled(false);
        }
    }
}