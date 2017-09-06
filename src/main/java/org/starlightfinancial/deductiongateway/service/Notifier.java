package org.starlightfinancial.deductiongateway.service;

/**
 * Created by sili.chen on 2017/9/5
 */
public abstract class Notifier {
    private EventHandler eventHandler = new EventHandler();

    public EventHandler getEventHandler() {
        return eventHandler;
    }

    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    /**
     * @param object     要执行方法的对象
     * @param methodName 执行方法 的方法名
     * @param args       执行方法的参数
     */
    public abstract void addListener(Object object, String methodName,
                                     Object... args);

    public abstract void notifyX();
}
