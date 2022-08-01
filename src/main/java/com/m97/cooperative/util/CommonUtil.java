package com.m97.cooperative.util;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

public class CommonUtil {

	public static final String REGEX_ONLY_NUMBER = "^[0-9]+$";

	public static final String PATTERN_DATE = "yyyy-MM-dd";
	public static final String PATTERN_TIMESTAMP = "yyyy-MM-dd'T'HH:mm:ss";

	public static final TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT+7");

	@SuppressWarnings("unchecked")
	public static void updateModel(Object fromModel, Object toModel) {
		if (fromModel instanceof Map) {
			Iterator<Map.Entry<String, Object>> iterator = ((Map<String, Object>) fromModel).entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, Object> entry = iterator.next();
				if (entry.getValue() != null) {
					try {
						Field field = toModel.getClass().getDeclaredField(entry.getKey());
						field.setAccessible(true);
						field.set(toModel, entry.getValue());
					} catch (Exception e) {
						// Ignore
					}
				}
			}
		} else {
			Field[] fields = fromModel.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				try {
					if (field.get(fromModel) != null) {
						Field toField = toModel.getClass().getDeclaredField(field.getName());
						toField.setAccessible(true);
						toField.set(toModel, field.get(fromModel));
					}
				} catch (Exception e) {
					// Ignore
				}

			}
		}
	}

}
