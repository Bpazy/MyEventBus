package src;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SubscriberMethod {
    private Method method;
    private ThreadMode threadMode;

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public ThreadMode getThreadMode() {
        return threadMode;
    }

    public void setThreadMode(ThreadMode threadMode) {
        this.threadMode = threadMode;
    }

    public Class<?>[] getParameterTypes() {
        return method.getParameterTypes();
    }

    public void invoke(Object o, Object... event) throws InvocationTargetException, IllegalAccessException {
        method.invoke(o, event);
    }
}
