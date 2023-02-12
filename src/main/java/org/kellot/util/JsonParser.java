package org.kellot.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.Reader;

public class JsonParser {
    private static Gson myGson = new Gson();

    // Convert JSON element to Java Object
    public static <T> T toObject(Reader reader, Class<T> tClass) {
        return myGson.fromJson(reader, tClass);
    }

    // Convert Java Object to JSON element
    public static JsonElement toJson(Object obj) {
        return myGson.toJsonTree(obj);
    }
}
