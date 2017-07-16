package org.shijia4j.framework.annotation;

import java.lang.annotation.*;

/**
 * Created by shijia on 7/16/17.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Aspect {
  Class<? extends Annotation> value();
}
