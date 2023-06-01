package ua.alisasira.thymeleaf.bean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;

public abstract class BaseBean {

    public String toJson() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        return gson.toJson(this);
    }

    public static <E extends BaseBean> E fromJson(String json, Class<E> clazz) {
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        Gson gson = new GsonBuilder().create();
        E o = gson.fromJson(json, clazz);
        return o;
    }
}