package io.kimmking.rpcfx.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author guozq
 * @date 2020-12-18-2:40 上午
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
public @interface RpcfxService {
    
    Class<?> interfaceClass() default void.class;
    
    String interfaceName() default "";
}
