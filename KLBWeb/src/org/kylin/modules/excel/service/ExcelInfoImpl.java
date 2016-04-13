package org.kylin.modules.excel.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kylin.modules.excel.ExcelInfo;
import org.kylin.modules.excel.annotation.PropertyAnnotation;

public class ExcelInfoImpl implements ExcelInfo {
	private Class clazz = null;

	private int num = 0;

	private List<String> propertys = new ArrayList();

	List<String> propertyNames = new ArrayList();

	List<String> fieldsName = new ArrayList();

	public ExcelInfoImpl(String className, List<String> fieldsName) {
		this.fieldsName = fieldsName;
		try {
			this.clazz = Class.forName(className);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getContent(Object obj, String methodName) throws Exception {
		String realMethodName = "get"
				+ methodName.substring(0, 1).toUpperCase()
				+ methodName.substring(1);
		Method method = this.clazz.getMethod(realMethodName, new Class[0]);
		if ((method.invoke(obj, new Object[0]) == null)
				|| ("".equals(method.invoke(obj, new Object[0])))) {
			return "";
		}
		return method.invoke(obj, new Object[0]).toString();
	}

	public List<String> getHeaders() {
		Field[] fields = this.clazz.getDeclaredFields();

		String propertyName = "";

		int PropertySortKey = 0;

		Map mapPropertys = new HashMap();

		Map mapPropertyNames = new HashMap();

		boolean flag = false;

		if (this.fieldsName.size() != 0) {
			for (int i = 0; i < fields.length; ++i) {
				if (fields[i].getAnnotation(PropertyAnnotation.class) == null) {
					continue;
				}
				propertyName = ((PropertyAnnotation) fields[i]
						.getAnnotation(PropertyAnnotation.class))
						.PropertyName();
				for (String str : this.fieldsName)
					if (str.equals(propertyName)) {
						this.propertys.add(fields[i].getName());
						this.propertyNames.add(propertyName);
					}
			}
		} else {
			for (int i = 0; i < fields.length; ++i) {
				if (fields[i].getAnnotation(PropertyAnnotation.class) == null) {
					continue;
				}

				propertyName = ((PropertyAnnotation) fields[i]
						.getAnnotation(PropertyAnnotation.class))
						.PropertyName();
				PropertySortKey = ((PropertyAnnotation) fields[i]
						.getAnnotation(PropertyAnnotation.class))
						.PropertySortKey();

				if (propertyName != null) {
					if (!flag) {
						if (PropertySortKey == -1) {
							this.propertys.add(fields[i].getName());
							if ("Unknown".equals(propertyName)) {
								this.propertyNames.add(fields[i].getName());
							} else {
								this.propertys.add(fields[i].getName());
								this.propertyNames.add(propertyName);
							}
						} else {
							flag = true;
						}
					}
					if (flag) {
						if ("Unknown".equals(propertyName)) {
							mapPropertys.put(Integer.valueOf(PropertySortKey),
									fields[i].getName());
							mapPropertyNames.put(Integer
									.valueOf(PropertySortKey), fields[i]
									.getName());
						} else {
							mapPropertys.put(Integer.valueOf(PropertySortKey),
									fields[i].getName());
							mapPropertyNames.put(Integer
									.valueOf(PropertySortKey), propertyName);
						}
					}
				}
			}

			if (flag) {
				sortPropertys(mapPropertys, mapPropertyNames);
			}
		}
		return this.propertyNames;
	}

	public void sortPropertys(Map mapPropertys, Map mapPropertyNames) {
		for (int i = 1; i <= mapPropertys.size(); ++i) {
			this.propertys.add((String) mapPropertys.get(Integer.valueOf(i)));
			this.propertyNames.add((String) mapPropertyNames.get(Integer
					.valueOf(i)));
		}
	}

	public List<String> getPropertys() {
		return this.propertys;
	}

	public List<String> getPropertyNames() {
		return this.propertyNames;
	}

	public int getNum() {
		this.num = getPropertys().size();
		if (this.num == 0) {
			getHeaders();
			this.num = getPropertys().size();
		}
		return this.num;
	}
}
