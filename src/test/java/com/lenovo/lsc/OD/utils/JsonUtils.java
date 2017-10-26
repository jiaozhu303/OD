package com.lenovo.lsc.OD.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.ArrayUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;

public final class JsonUtils {
    // http://stackoverflow.com/questions/10380835/is-it-ok-to-use-gson-instance-as-a-static-field-in-a-model-bean-reuse
    private static final Gson gson = new Gson();

    private JsonUtils() {
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    public static String toJson(Object src) {
        return gson.toJson(src);
    }

    public static <T> T fromFile(String filePath, Class<T> classOfT) throws IOException {
        try (Reader json = new InputStreamReader(new FileInputStream(filePath), "UTF-8")) {
            return gson.fromJson(json, classOfT);
        }
    }

    public static String toJson(Object src, String... ignoredFields) {
        Gson customGson = new GsonBuilder()
            .addSerializationExclusionStrategy(new CustomExclusionStrategy(ignoredFields)).create();
        return customGson.toJson(src);
    }

    /**
     * 在转成json时，可以忽略某些字段的自定义策略。
     *
     * @author wangzb4
     * @see http://stackoverflow.com/questions/4802887/gson-how-to-exclude-specific-fields-from-serialization-without-annotations
     */
    private static class CustomExclusionStrategy implements ExclusionStrategy {
        private String[] ignoredFields;

        public CustomExclusionStrategy(String[] ignoredFields) {
            this.ignoredFields = ignoredFields;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return ArrayUtils.contains(ignoredFields, f.getName());
        }
    }
}
