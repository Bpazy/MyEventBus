package Utils;

import src.Subscribe;
import src.SubscriberMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MethodHelpers {
    public static List<SubscriberMethod> findSubscriberMethods(Class<?> c) {
        List<SubscriberMethod> list = new ArrayList<>();
        Method[] declaredMethods = c.getDeclaredMethods();
        for (Method m : declaredMethods) {
            //遍历所有方法，取出含有注解Subscribe的方法，并将该类对象及方法存入methodMap中，供post(Object)方法invoke调用
            Subscribe annotation = m.getDeclaredAnnotation(Subscribe.class);
            if (annotation != null && annotation.chosen()) {
                SubscriberMethod method = new SubscriberMethod();
                method.setMethod(m);
                method.setThreadMode(annotation.threadMode());
                list.add(method);
            }
        }
        return list;
    }
}
