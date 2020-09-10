package com.geek.common.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

public class ConfusionModule extends Module {

    public final static String MODULE_NAME = "jackson-confusion-encryption";
    public final static Version VERSION = new Version(1, 0, 0, null, "geek", MODULE_NAME);

    /**
     * 注册当前模块。
     *
     * @return
     */
    public static ObjectMapper registerModule(ObjectMapper objectMapper) {
        // CamelCase 策略，Java 对象属性：personId，序列化后属性：personId。
        // PascalCase 策略，Java 对象属性：personId，序列化后属性：PersonId。
        // SnakeCase 策略，Java 对象属性：personId，序列化后属性：person_id。
        // KebabCase 策略，Java 对象属性：personId，序列化后属性：person-id。
        // 忽略多余字段，抛错。
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        return objectMapper.registerModule(new ConfusionModule());
    }

    @Override
    public String getModuleName() {
        return MODULE_NAME;
    }

    @Override
    public Version version() {
        return VERSION;
    }

    @Override
    public void setupModule(SetupContext context) {
        context.addBeanSerializerModifier(new ConfusionSerializerModifier());
        context.addBeanDeserializerModifier(new ConfusionDeserializerModifier());
    }

}
