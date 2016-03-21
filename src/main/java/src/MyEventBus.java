package src;

import Utils.MethodHelpers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyEventBus {
    private Map<Object, Method> methodMap; //存放对象和对象的方法
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
     *
     * @param o Example: 在Main.java中MyEventBus().getDefault().register(this);
     */
    public void register(Object o) {
        Class<?> c = o.getClass();  //获取类对象的Class
        Method[] declaredMethods = c.getDeclaredMethods();  //通过反射获取传入的对象的所有方法
        for (Method m : declaredMethods) {
            //遍历所有方法，取出含有注解Subscribe的方法，并将该类对象及方法存入methodMap中，供post(Object)方法invoke调用
            Subscribe annotation = m.getDeclaredAnnotation(Subscribe.class);
            if (annotation != null && annotation.chosen()) {
                methodMap.put(o, m);
                parameterList.add(m.getParameterTypes()[0]);
            }
        }
    }

    public void post(Object event) {
        if (methodMap.isEmpty()) {
            return;
        }

        //遍历methodMap，比较传入的参数event的类型和被注解的方法的形参类型是否相同，如果相同则通过反射调用对应的方法
        for (Map.Entry<Object, Method> entry : methodMap.entrySet()) {
            Object o = entry.getKey();
            Method m = entry.getValue();
            for (Class<?> c : parameterList) {
                if (c.getTypeName().equals(event.getClass().getTypeName())) {
                    try {
                        m.invoke(o, event);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
