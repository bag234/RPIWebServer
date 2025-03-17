package org.bag.WebServer.Annotation;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bag.WebServer.Reqwest.Method;

@Retention(RetentionPolicy.RUNTIME)
@Target(TYPE)
public @interface EndPoint {

	String[] path();
	
	Method[] method() default Method.GET; 
	
}
