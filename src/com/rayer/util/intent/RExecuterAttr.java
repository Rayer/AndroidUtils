/**
 * 
 */
package com.rayer.util.intent;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Killercat
 *
 */
@Target({java.lang.annotation.ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RExecuterAttr {
  
  public static final String DEF_HANDLE_ACTION_NONE = "";
  
  String[] handleAction() default DEF_HANDLE_ACTION_NONE;
}
