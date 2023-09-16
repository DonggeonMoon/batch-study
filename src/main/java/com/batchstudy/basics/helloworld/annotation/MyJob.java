package com.batchstudy.basics.helloworld.annotation;

import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Inherited()
@Qualifier("myJob")
public @interface MyJob {
}
