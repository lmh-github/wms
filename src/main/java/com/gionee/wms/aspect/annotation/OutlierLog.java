package com.gionee.wms.aspect.annotation;

import java.lang.annotation.*;

/**
 * 单价异常数据注解
 * <p>
 * Created by huyunfan on 2017/10/25.
 */
@Target(value = {ElementType.METHOD})
@Retention(value = RetentionPolicy.RUNTIME)
@Documented
public @interface OutlierLog {

    String description() default "";
}
