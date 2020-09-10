//package com.geek.common.HBase;
//
//import com.geek.common.HBase.entity.HBaseStorage;
//import com.geek.common.common.storage.StorageData;
//import lombok.extern.log4j.Log4j2;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * HBase 存储客户端。
// */
//@Component
//@Log4j2
//public class HBaseStorageClient {
//
//    /**
//     * 注入的 HBaseClient 工具类。
//     */
//    @Autowired
//    private HBaseClient HBaseClient;
//
//    /**
//     * 添加一个存储列表到 HBase。
//     *
//     * @param tableName        表名。
//     * @param HBaseStorageList 存储列表。
//     */
//    public void addHBaseStorage(final String tableName, List<HBaseStorage> HBaseStorageList) {
//        if (null != HBaseStorageList && !HBaseStorageList.isEmpty()) {
//            HBaseStorageList.stream().forEach(HBaseStorage -> {
//                addHBaseStorage(tableName, HBaseStorage);
//            });
//        }
//    }
//
//    /**
//     * 添加一个存储到 HBase。
//     *
//     * @param tableName    表名。
//     * @param HBaseStorage 存储。
//     */
//    public void addHBaseStorage(String tableName, HBaseStorage HBaseStorage) {
//        if (null != HBaseStorage && StringUtils.isNotEmpty(tableName)) {
//            HBaseClient.creatTable(tableName, HBaseStorage.getColumnFamily());
//            String rowKey = HBaseStorage.getRowKey();
//            List<StorageData> storageDataList = HBaseStorage.getDataList();
//            boolean result = addStorageData(tableName, rowKey, storageDataList);
//            if (result) {
//                HBaseStorage.invoke();
//            }
//        }
//    }
//
//    /**
//     * 添加数据到 HBase。
//     *
//     * @param tableName       表名。
//     * @param rowKey          主键。
//     * @param storageDataList 存储数据集合。
//     * @return
//     */
//    public boolean addStorageData(String tableName, String rowKey, List<StorageData> storageDataList) {
//        long currentTime = System.currentTimeMillis();
//        log.info("开始添加StorageData到HBase,tableName：{}，rowKey：{}", tableName, rowKey);
//        if (null != storageDataList && !storageDataList.isEmpty()) {
//            storageDataList.forEach(HBaseData -> {
//                String columnFamliyName = HBaseData.getTargetClassName();
//                String[] columnArray = HBaseData.getColumns();
//                String[] valueArray = HBaseData.getValues();
//                if (null != columnArray && null != valueArray) {
//                    HBaseClient.putData(tableName, rowKey, columnFamliyName, columnArray, valueArray);
//                }
//            });
//        }
//        log.info("添加 StorageData 到 HBase 完成。tableName：{}，rowKey：{}，duration：{}", tableName, rowKey, System.currentTimeMillis() - currentTime);
//        return true;
//    }
//
//    /**
//     * 根据表明以及 rowKey 获取一个对象。
//     *
//     * @param tableName 表名。
//     * @param rowKey    主键。
//     * @param tClass    需要获取的对象类型。
//     * @param <T>       泛型 T。
//     * @return 返回要返回的数。
//     */
//    public <T> T getStorageDataEntity(String tableName, String rowKey, Class<T> tClass) {
//        T tValue = null;
//        if (StringUtils.isNotEmpty(tableName)) {
//            StorageData HBaseData = HBaseClient.getStorageData(tableName, rowKey, tClass.getName());
//            if (null != HBaseData) {
//                tValue = (T) HBaseData.getObjectValue();
//            }
//        }
//        return tValue;
//    }
//
//    /**
//     * 根据类型列表、表名、rowKey 返回一个数据类型的列表。
//     *
//     * @param tableName 表名。
//     * @param rowKey    rowKey。
//     * @param typeList  类型列表。
//     * @return 返回的对象列表。
//     */
//    public List<Object> getStorageDataEntityList(String tableName, String rowKey, List<Class> typeList) {
//        List<Object> entityList = new ArrayList<Object>();
//        List<String> strTypeList = typeList.stream().map(x -> x.getName()).collect(Collectors.toList());
//        List<StorageData> storageDataList = HBaseClient.getStorageDataList(tableName, rowKey, strTypeList);
//        for (StorageData storageData : storageDataList) {
//            entityList.add(storageData.getObjectValue());
//        }
//        return entityList;
//    }
//
//    /**
//     * 获取 HBaseClient 客户端。
//     *
//     * @return
//     */
//    public HBaseClient getHBaseClient() {
//        return HBaseClient;
//    }
//}
