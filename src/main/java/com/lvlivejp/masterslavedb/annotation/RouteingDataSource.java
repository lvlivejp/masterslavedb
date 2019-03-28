package com.lvlivejp.masterslavedb.annotation;

import com.lvlivejp.masterslavedb.config.DataSources;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface RouteingDataSource {
    String value() default DataSources.MASTER_DB;
}
