package loading.events;

import net.minecraftforge.fml.common.eventhandler.Event;

public class CameraEvent extends Event
{
    public double distance;

    public CameraEvent( double distance )
    {
        this.distance = distance;
    }
}