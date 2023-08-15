package com.example.best_travel.util.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//Estas dos anotaciones siempre deben ir al crear una nueva anotacion
@Retention(value= RetentionPolicy.RUNTIME)
@Target(value= ElementType.METHOD)
public @interface Notify {
    String value() default "files/notify.txt";
}
