package com.github.andregpereira.resilientshop.productsapi.util.mock.factory;

import com.github.andregpereira.resilientshop.productsapi.util.json.JsonReader;
import lombok.experimental.UtilityClass;

import static com.github.andregpereira.resilientshop.productsapi.util.constant.CommonConstants.MOCKS_PATH;

@UtilityClass
public class InvalidObjectMockFactory {

    static <T> T readInvalidJson(final Class<T> clazz) {
        return JsonReader.read(MOCKS_PATH.concat("/json-invalido.json"), clazz);
    }

    public static Object readInvalidJson() {
        return JsonReader.read(MOCKS_PATH.concat("/json-invalido.json"), Object.class);
    }

}
