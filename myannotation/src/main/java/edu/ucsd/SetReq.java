package edu.ucsd;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.CLASS;

@Retention(CLASS) @Target(ElementType.TYPE)
public @interface SetReq {
    int timeout();
    int retry();
}
