package src;

import Utils.MethodHelpers;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyEventBus {
    private Map<Object, SubscriberMethod> methodMap; //存放对象和对象的方法
    private List<Class<?>> parameterList;  //存放methodMap里方法中的形参类型

    private MyEventBus() {
        methodMap = new ConcurrentHashMap<>();
        parameterList = new ArrayList<>();
    }


    private static class MyEventBusLoader {
        private static MyEventBus myEventBus = new MyEventBus();
    }

    //实现EventBus的getDefault()方法，本质上是单例模式
    public static MyEventBus getDefault() {
        return MyEventBusLoader.myEventBus;
    }

    /**
     * @param o Example: 在Main.java中MyEventBus().getDefault().register(this);
     */
    public void register(Object o) {
        Class<?> c = o.getClass();  //获取类对象的Class
        List<SubscriberMethod> subscriberMethods = MethodHelpers.findSubscriberMethods(c);//通过反射获取传入的对象的所有方法
        for (SubscriberMethod m : subscriberMethods) {
            methodMap.put(o, m);
            parameterList.add(m.getParameterTypes()[0]);
        }
    }

    public void post(Object event) {
        if (methodMap.isEmpty()) {
            return;
        }

        //遍历methodMap，比较传入的参数event的类型和被注解的方法的形参类型是否相同，如果相同则通过反射调用对应的方法
        for (Map.Entry<Object, SubscriberMethod> entry : methodMap.entrySet()) {
            Object o = entry.getKey();
            SubscriberMethod m = entry.getValue();
            for (Class<?> c : parameterList) {
                if (c.getTypeName().equals(event.getClass().getTypeName())) {
                    if (m.getThreadMode() == ThreadMode.MainThread) {
                        runInBackgroundThread(m, o, event);
                    } else {
                        runInMainThread(m, o, event);
                    }
                }
            }
        }
    }

    private void runInBackgroundThread(final SubscriberMethod m, final Object o, final Object event) {
        new Thread(() -> {
            try {
                m.invoke(o, event);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void runInMainThread(SubscriberMethod m, Object o, Object... event) {
        try {
            m.invoke(o, event);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
