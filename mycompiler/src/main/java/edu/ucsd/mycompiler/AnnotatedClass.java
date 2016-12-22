package edu.ucsd.mycompiler;

import javax.lang.model.element.TypeElement;

import edu.ucsd.myannotation.Anel_property;

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
        Anel_property annotation = classElement.getAnnotation(Anel_property.class);
        //timeout = annotation.timeout();
        //retry = annotation.retry();
    }

    public int getTimeout(){return timeout;}

    public int getRetry() {return retry;}


}
