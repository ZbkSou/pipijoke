package com.zbk.baselibrary.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by ZBK on 2018-09-09.
 * View 的注解Annotation
 * Target   代表anotation处在的位置,FIELD 属性,TYPE类,CONSTRUCTOR 构造方法
 * Retention 生效时机 RUNTIME VM运行时 CLASS 编译时 SOURCE 源码
 * @function
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewById {
    int value();
}
