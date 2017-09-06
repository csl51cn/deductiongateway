package org.starlightfinancial.deductiongateway.service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sili.chen on 2017/9/5
 */
public class EventHandler {

    private List<Event> objects;

    public EventHandler() {
        objects = new ArrayList<>();
    }

    public void addEvent(Object object, String methodName, Object... args) {
        objects.add(new Event(object, methodName, args));
    }

    public void notifyX() throws Exception {
        for (Event e : objects) {
            e.invoke();
        }
    }
}
