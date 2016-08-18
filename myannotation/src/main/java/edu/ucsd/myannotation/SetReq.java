package edu.ucsd.myannotation;

//@Retention(RetentionPolicy.CLASS)
public @interface SetReq {
    int timeout();
    int retry();
}
