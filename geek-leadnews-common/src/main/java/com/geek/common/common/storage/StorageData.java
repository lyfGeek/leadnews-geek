package com.geek.common.common.storage;

import com.geek.utils.common.DataConvertUtils;
import com.geek.utils.common.ReflectUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 存储数据 Data。
 */
@Setter
@Getter
@ToString
public class StorageData {

    /**
     * 目标 class 类名称。
     */
    private String targetClassName;
    /**
     * 存储的字段列表。
     */
    private List<StorageEntry> entryList = new ArrayList<StorageEntry>();

    /**
     * 将一个 Bean 转换为 StorageData。
     *
     * @param bean
     * @return
     */
    public static StorageData getStorageData(Object bean) {
        StorageData HBaseData = null;
        if (null != bean) {
            HBaseData = new StorageData();
            HBaseData.setTargetClassName(bean.getClass().getName());
            HBaseData.setEntryList(getStorageEntryList(bean));

        }
        return HBaseData;
    }

    /**
     * 根据 bean 获取 entry 列表。
     *
     * @param bean
     * @return
     */
    private static List<StorageEntry> getStorageEntryList(Object bean) {
        PropertyDescriptor[] propertyDescriptorArray = ReflectUtils.getPropertyDescriptorArray(bean);
        return Arrays.asList(propertyDescriptorArray).stream().map(propertyDescriptor -> {
            String key = propertyDescriptor.getName();
            Object value = ReflectUtils.getPropertyDescriptorValue(bean, propertyDescriptor);
            value = DataConvertUtils.unConvert(value, ReflectUtils.getFieldAnnotations(bean, propertyDescriptor));
            return new StorageEntry(key, DataConvertUtils.toString(value));
        }).collect(Collectors.toList());
    }

    /**
     * 添加一个 entry。
     *
     * @param entry
     */
    public void addStorageEntry(StorageEntry entry) {
        entryList.add(entry);
    }

    /**
     * 添加一个 entry。
     *
     * @param key
     * @param value
     */
    public void addStorageEntry(String key, String value) {
        entryList.add(new StorageEntry(key, value));
    }

    /**
     * 根据 Map 添加 entry。
     *
     * @param map
     */
    public void putHBaseEntry(Map<String, String> map) {
        if (null != map && !map.isEmpty()) {
            map.forEach((k, v) -> addStorageEntry(new StorageEntry(k, v)));
        }
    }

    /**
     * 获取所有的 Column 数组。
     *
     * @return
     */
    public String[] getColumns() {
        List<String> columnList = entryList.stream().map(StorageEntry::getKey).collect(Collectors.toList());
        if (null != columnList && !columnList.isEmpty()) {
            return columnList.toArray(new String[columnList.size()]);
        }
        return null;
    }

    /**
     * 获取所有的值数字。
     *
     * @return
     */
    public String[] getValues() {
        List<String> valueList = entryList.stream().map(StorageEntry::getValue).collect(Collectors.toList());
        if (null != valueList && !valueList.isEmpty()) {
            return valueList.toArray(new String[valueList.size()]);
        }
        return null;
    }

    /**
     * 获取一个 Map。
     *
     * @return
     */
    public Map<String, Object> getMap() {
        Map<String, Object> entryMap = new HashMap<String, Object>();
        entryList.forEach(entry -> entryMap.put(entry.getKey(), entry.getValue()));
        return entryMap;
    }

    /**
     * 将当前的 StorageData 转换为具体的对象。
     *
     * @return
     */
    public Object getObjectValue() {
        Object bean = null;
        if (StringUtils.isNotEmpty(targetClassName) && null != entryList && !entryList.isEmpty()) {
            bean = ReflectUtils.getClassForBean(targetClassName);
            if (null != bean) {
                for (StorageEntry entry : entryList) {
                    Object value = DataConvertUtils.convert(entry.getValue(), ReflectUtils.getFieldAnnotations(bean, entry.getKey()));
                    ReflectUtils.setProperties(bean, entry.getKey(), value);
                }
            }
        }
        return bean;
    }

}
