package edu.ucsd;

import javax.lang.model.element.TypeElement;

/**
 * Created by xinxin on 3/14/16.
 */
public class AnnotatedClass {
    private TypeElement annotatedClassElement;
    private String qualifiedGroupClassName;
    private String simpleFactoryGroupName;
    private int timeout, retry;



    public AnnotatedClass(TypeElement classElement) {
        this.annotatedClassElement = classElement;
        SetReq annotation = classElement.getAnnotation(SetReq.class);
        timeout = annotation.timeout();
        retry = annotation.retry();
    }

    public int getTimeout(){return timeout;}

    public int getRetry() {return retry;}


}
