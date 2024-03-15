package ua.alisasira.validation.entity;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class BaseEntity {
    public String toJson() {
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(
                    LocalDate.class,
                    (JsonSerializer<Object>) (src, typeOfSrc, context) ->
                            new JsonPrimitive(DateTimeFormatter.ofPattern("yyyy-MM-dd").format((LocalDate)src)))
            .create();

        return gson.toJson(this);}

    public static <E extends BaseEntity> E fromJson(String json, Class<E> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class,
                        (JsonDeserializer<LocalDate>) (jsonInput, typeOfT, context) ->
                                LocalDate.parse(jsonInput.getAsString()))
                .create();
        E o = gson.fromJson(json, clazz);
        return o;
    }

    public static List<User> fromJsonArray(String json) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class,
                        (JsonDeserializer<LocalDate>) (jsonInput, typeOfT, context) ->
                                LocalDate.parse(jsonInput.getAsString()))
                .create();

        Type listType = new TypeToken<List<User>>() {}.getType();

        return gson.fromJson(json, listType);
    }
}