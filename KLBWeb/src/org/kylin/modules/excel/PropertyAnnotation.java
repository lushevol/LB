package org.kylin.modules.excel;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PropertyAnnotation {
	public abstract String PropertyName();

	public abstract int PropertySortKey();
}
