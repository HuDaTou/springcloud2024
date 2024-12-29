package com.overthinker.cloud.web.utils;


import java.lang.reflect.Field;

public class ConstantsUtils {

    public static boolean isExist(Class<?> constantsClass, String type) {
        Field[] fields = constantsClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                if (field.getType().equals(String.class) && field.get(null).equals(type)) {
                    return true;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}

