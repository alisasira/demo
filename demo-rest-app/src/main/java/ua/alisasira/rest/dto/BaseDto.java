package ua.alisasira.rest.dto;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.util.StringUtils;

public abstract class BaseDto {

    public String toJson() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(this);
    }

    public static <E extends BaseDto> E fromJson(String json, Class<E> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        Gson gson = new GsonBuilder().create();
        E o = gson.fromJson(json, clazz);
        return o;
    }
}