package io.vlingo.xoom.examples.ecommerce.model;

import com.google.gson.Gson;
import io.vlingo.xoom.http.resource.Mapper;

public class GsonMapper implements Mapper {
    static Gson gson = new Gson();

    @Override
    public <T> T from(String data, Class<T> type) {
        return gson.fromJson(data, type);
    }

    @Override
    public <T> String from(T data) {
        return gson.toJson(data);
    }
}
