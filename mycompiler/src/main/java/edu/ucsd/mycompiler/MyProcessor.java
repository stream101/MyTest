package edu.ucsd.mycompiler;

import com.google.auto.service.AutoService;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import edu.ucsd.myannotation.SetReq;

/**
 * about Element:
 package com.example;	// PackageElement

 public class Foo {		// TypeElement
     private int a;		// VariableElement
     private Foo other; 	// VariableElement
     public Foo () {} 	// ExecuteableElement
     public void setA () 	// ExecuteableElement
     int newA	// TypeElement
 ) {}
 }
 */
/*AutoService will generate the file META-INF/services/javax.annotation.processing.
Processor in the output classes folder.*/
@AutoService(Processor.class)
public final class MyProcessor extends AbstractProcessor {

    private Types typeUtils;
    private Elements elementUtils;
    private Filer filer;
    private Messager messager;
    String TAG = "MyProcessor";

    @Override public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        typeUtils = processingEnv.getTypeUtils(); // A utils class to work with TypeMirror
        elementUtils = processingEnv.getElementUtils();//A utils class to work with Element classes
        filer = processingEnv.getFiler(); //with Filer you can create files
        messager = processingEnv.getMessager();
    }

    //for which annotations this annotation processor should be registered
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(SetReq.class.getCanonicalName());
        debug ("add annotation type: " + SetReq.class.getCanonicalName() );
        return types;
    }

    //specify which java version you use
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        debug("jin xinxin enter process !!\n");
        // iterate over all @SetReq annotated
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(SetReq.class)) {
            debug(annotatedElement, "asType: " + annotatedElement.asType().toString() + '\n'
                  + "getKind: " + annotatedElement.getKind() + '\n'
                  +  "getEnclosingElement: " + annotatedElement.getEnclosingElement() + '\n');

            //currently on process class
            //if (annotatedElement.getKind() != ElementKind.CLASS) {
            //    error(annotatedElement, "annotation type not supported");
            //}

            // We can cast it, because we know that it of ElementKind.CLASS
            //TypeElement typeElement = (TypeElement) annotatedElement;

            //get annotated var and annotation information
            //AnnotatedClass annotatedClass= new AnnotatedClass(typeElement);

            //TBD: check validity


            //add to generated class. create a class if doesn't exist
              

            //gen code


        }

        return true;
    }

    /**
     * Prints an error message
     *
     * @param e The element which has caused the error. Can be null
     * @param msg The error message
     */
    public void error(Element e, String msg) {
        messager.printMessage(Diagnostic.Kind.ERROR,  msg, e);
    }

    public void debug(Element e, String msg) {
        messager.printMessage(Diagnostic.Kind.OTHER, TAG + msg, e);
    }

    public void debug(String msg) {
        messager.printMessage(Diagnostic.Kind.OTHER, msg);
    }
}
