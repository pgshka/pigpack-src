package loading;

import me.pig.pack.PigPack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod( name = ".", modid = ".", version = "." )
public class Main {
    @Mod.EventHandler public void onInitialize(FMLInitializationEvent e) {
        PigPack.load();
    }
}
