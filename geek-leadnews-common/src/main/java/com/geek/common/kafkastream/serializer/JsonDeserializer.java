package com.geek.common.kafkastream.serializer;

import com.geek.common.common.constant.Constant;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class JsonDeserializer<T> implements Deserializer<T> {

    Class<T> cla;

    public JsonDeserializer(Class<T> cla) {
        this.cla = cla;
    }

    @Override
    public void configure(Map<String, ?> map, boolean b) {
    }

    @Override
    public T deserialize(String s, byte[] bytes) {
        if (bytes == null)
            return null;

        T data;
        try {
            data = Constant.objectMapper.readValue(bytes, cla);
        } catch (Exception e) {
            throw new SerializationException(e);
        }

        return data;
    }

    @Override
    public void close() {
    }

}
