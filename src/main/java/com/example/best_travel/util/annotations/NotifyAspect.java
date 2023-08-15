package com.example.best_travel.util.annotations;

import com.example.best_travel.util.BestTravelUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@Aspect
public class NotifyAspect {
    private static final String LINE_FORMAT= "At %s new request with size page %s and order %s";
    //Al hacer aspectos los metodos deben ser vacios
    @After(value = "@annotation(com.example.best_travel.util.annotations.Notify)")
    public void notifyInFile(JoinPoint joinPoint) throws IOException {
        var args = joinPoint.getArgs();
        var size = args[1];
        var order = args[2]==null?"NONE":args[2];
        var text = String.format(LINE_FORMAT, LocalDateTime.now(), size.toString(), order.toString());

        var signature = (MethodSignature)joinPoint.getSignature();
        var method = signature.getMethod();
        var annotation = method.getAnnotation(Notify.class);
        BestTravelUtil.writeNotifications(text, annotation.value());
    }
}
