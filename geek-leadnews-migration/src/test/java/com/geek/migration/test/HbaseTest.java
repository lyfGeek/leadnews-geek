package com.geek.migration.test;


import com.geek.common.HBase.HBaseClient;
import com.geek.common.HBase.HBaseStorageClient;
import com.geek.common.HBase.constants.HBaseConstants;
import com.geek.common.HBase.entity.HBaseStorage;
import com.geek.common.common.storage.StorageData;
import com.geek.common.common.storage.StorageEntry;
import com.geek.model.article.pojos.ApArticle;
import org.apache.hadoop.HBase.client.Result;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class HBaseTest {

    @Autowired
    private HBaseClient hBaseClient;
    @Autowired
    private HBaseStorageClient storageClient;

    @Test
    public void testCreateTable() {
        List<String> columnFamily = new ArrayList<>();
        columnFamily.add("test_cloumn_family1");
        columnFamily.add("test_cloumn_family2");
        boolean ret = hBaseClient.creatTable("HBase_test_table_name", columnFamily);
    }

    @Test
    public void testDelTable() {
        hBaseClient.deleteTable(HBaseConstants.APARTICLE_QUANTITY_TABLE_NAME);
    }

    @Test
    public void testSaveData() {
        //String tableName, String rowKey, String familyName, String[] columns, String[] values
        String[] columns = {"name", "age"};
        String[] values = {"zhangsan", "28"};
        hBaseClient.putData("HBase_test_table_name", "test_row_key_001", "test_cloumn_family1", columns, values);
    }

    @Test
    public void testFindByRowKey() {
        Result hBaseResult = hBaseClient.getHBaseResult("HBase_test_table_name", "test_row_key_001");
        System.out.println(hBaseResult);
    }

    @Test
    public void testStorageSaveData() {
        HBaseStorage storage = new HBaseStorage();
        storage.setRowKey("storage_row_key_00001");

        List<StorageData> dataList = new ArrayList<StorageData>();
        StorageData storageData = new StorageData();
        storageData.setTargetClassName("ApArticle");

        List<StorageEntry> entryList = new ArrayList<StorageEntry>();
        StorageEntry entry = new StorageEntry();
        entry.setKey("title");
        entry.setValue("数据迁移");
        StorageEntry entry2 = new StorageEntry();
        entry2.setKey("id");
        entry2.setValue("123456");
        entryList.add(entry);
        entryList.add(entry2);
        storageData.setEntryList(entryList);

        dataList.add(storageData);
        storage.setDataList(dataList);

        storageClient.addHBaseStorage("HBase_storage_test_table_name", storage);
    }

    @Test
    public void testStorageGet() {
        ApArticle apArticle = storageClient.getStorageDataEntity("HBase_storage_test_table_name", "storage_row_key_00001", ApArticle.class);
        System.out.println(apArticle);
    }

}
