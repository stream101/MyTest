package edu.ucsd.myannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
//@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE, E})
public @interface Anel_property {
    String[] value();
}
