package edu.ucsd.myannotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.CLASS)
public @interface SetReq {
    int timeout();
    int retry();
}
