package cn.eflo.managewatermeter.event;

import com.squareup.otto.Bus;

public class EventBus {
    private final static Bus bus = new Bus();

    private EventBus(){

    }

    public static void post(Object obj){
        bus.post(obj);
    }

    public static void register(Object obj){
        bus.register(obj);
    }

    public static void unregister(Object obj){
        bus.unregister(obj);
    }
}
