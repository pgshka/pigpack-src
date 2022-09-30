package me.pig.pack.impl.module.movement;

import me.pig.pack.PigPack;
import me.pig.pack.impl.module.Module;
import me.pig.pack.api.setting.Setting;
import me.pig.pack.utils.Timer;

@Module.Manifest( name = "TickShift", cat = Module.Category.MOVEMENT)
public class TickShift extends Module {
    Timer timer = new Timer();
    private final Setting<Number> delay = register( "Delay", 0.1,0,5,0.1 );
    private final Setting<Number> speed = register( "Speed", 1,1,10,0.1 );
    private final Setting<Boolean> skipTick = register( "SkipTick", false);

    @Override
    public void onEnable(){

        timer.reset();

    }

    @Override
    public void onTick(){
        if (nullCheck()) return;

        if (timer.passed((long) (delay.getValue().floatValue() * 500))){
            PigPack.TIMER_VALUE = 1;
            setToggled(false);
        } else {
            PigPack.TIMER_VALUE = skipTick.getValue() ? 100 : speed.getValue().floatValue();
        }
    }
}
