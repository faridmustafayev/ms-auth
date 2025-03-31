package az.ingress.ms.auth.util;

import java.io.IOException;

import static az.ingress.ms.auth.mapper.ObjectMapperFactory.OBJECT_MAPPER_FACTORY;

public enum MapperUtil {
    MAPPER_UTIL;

    public <T> T map(String body, Class<T> clazz) {
        try {
            return OBJECT_MAPPER_FACTORY.createObjectMapper().readValue(body, clazz);
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex.getMessage());
        }
    }
}
