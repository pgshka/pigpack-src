package loading.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class MoveEvent extends Event {

    public double motionX, motionY, motionZ;

    public MoveEvent( final double motionX, final double motionY, final double motionZ ) {
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
    }
}