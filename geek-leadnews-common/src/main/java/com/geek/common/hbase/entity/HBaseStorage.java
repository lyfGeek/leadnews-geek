//package com.geek.common.HBase.entity;
//
//import com.geek.common.common.storage.StorageData;
//import com.geek.common.common.storage.StorageEntity;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * HBase 存储对象 继承 StorageEntity。
// * 用于存储各种对象。
// */
//@Setter
//@Getter
//public class HBaseStorage extends StorageEntity {
//
//    /**
//     * 主键 key。
//     */
//    private String rowKey;
//    /**
//     * HBase 的回调接口，用于将回调方法。
//     */
//    private com.geek.common.HBase.entity.IHBaseInvoke HBaseInvoke;
//
//    /**
//     * 获取类簇数组。
//     *
//     * @return
//     */
//    public List<String> getColumnFamily() {
//        return getDataList().stream().map(StorageData::getTargetClassName).collect(Collectors.toList());
//    }
//
//    /**
//     * 进行回调。
//     */
//    public void invoke() {
//        if (null != HBaseInvoke) {
//            HBaseInvoke.invoke();
//        }
//    }
//
//}
