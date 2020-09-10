//package com.geek.common.HBase;
//
//import com.geek.common.common.storage.StorageData;
//import lombok.extern.log4j.Log4j2;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.hadoop.HBase.Cell;
//import org.apache.hadoop.HBase.CompareOperator;
//import org.apache.hadoop.HBase.TableName;
//import org.apache.hadoop.HBase.client.*;
//import org.apache.hadoop.HBase.filter.*;
//import org.apache.hadoop.HBase.util.Bytes;
//import org.apache.hadoop.conf.Configuration;
//
//import java.io.IOException;
//import java.math.BigInteger;
//import java.text.MessageFormat;
//import java.util.*;
//import java.util.stream.Collectors;
//
///**
// * HBase 相关的基本操作。
// */
//@Log4j2
//public class HBaseClient {
//
//    /**
//     * 声明静态配置。
//     */
//    private Configuration conf;
//    private Connection connection = null;
//
//    public HBaseClient(Configuration conf) {
//
//        this.conf = conf;
//        try {
//            connection = ConnectionFactory.createConnection(conf);
//        } catch (IOException e) {
//            log.error("获取 HBase 连接失败。");
//        }
//    }
//
//    /**
//     * 按 startKey 和 endKey 分区数获取分区。
//     */
//    public static byte[][] getHexSplits(String startKey, String endKey, int numRegions) {
//        byte[][] splits = new byte[numRegions - 1][];
//        BigInteger lowestKey = new BigInteger(startKey, 16);
//        BigInteger highestKey = new BigInteger(endKey, 16);
//        BigInteger range = highestKey.subtract(lowestKey);
//        BigInteger regionIncrement = range.divide(BigInteger.valueOf(numRegions));
//        lowestKey = lowestKey.add(regionIncrement);
//        for (int i = 0; i < numRegions - 1; i++) {
//            BigInteger key = lowestKey.add(regionIncrement.multiply(BigInteger.valueOf(i)));
//            byte[] b = String.format("%016x", key).getBytes();
//            splits[i] = b;
//        }
//        return splits;
//    }
//
//    public boolean tableExists(String tableName) {
//        if (StringUtils.isNotEmpty(tableName)) {
//            Admin admin = null;
//            try {
//                admin = connection.getAdmin();
//                return admin.tableExists(TableName.valueOf(tableName));
//            } catch (Exception e) {
//                log.debug("检查表是否存在异常。", e);
//            } finally {
//                close(admin, null, null);
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 创建表。
//     *
//     * @param tableName    表名。
//     * @param columnFamily 列族名。
//     * @return
//     */
//    public boolean creatTable(String tableName, List<String> columnFamily) {
//        Admin admin = null;
//        try {
//            admin = connection.getAdmin();
//
//            List<ColumnFamilyDescriptor> familyDescriptors = new ArrayList<ColumnFamilyDescriptor>(columnFamily.size());
//
//            columnFamily.forEach(cf -> {
//                familyDescriptors.add(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(cf)).build());
//            });
//
//            TableDescriptor tableDescriptor = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName))
//                    .setColumnFamilies(familyDescriptors)
//                    .build();
//
//            if (admin.tableExists(TableName.valueOf(tableName))) {
//                log.debug("table Exists!");
//            } else {
//                admin.createTable(tableDescriptor);
//                log.debug("create table Success!");
//            }
//        } catch (IOException e) {
//            log.error(MessageFormat.format("创建表{0}失败", tableName), e);
//            return false;
//        } finally {
//            close(admin, null, null);
//        }
//        return true;
//    }
//
//    /**
//     * 预分区创建表。
//     *
//     * @param tableName    表名。
//     * @param columnFamily 列族名的集合。
//     * @param splitKeys    预分期 region。
//     * @return 是否创建成功。
//     */
//    public boolean createTableBySplitKeys(String tableName, List<String> columnFamily, byte[][] splitKeys) {
//        Admin admin = null;
//        try {
//            if (StringUtils.isBlank(tableName) || columnFamily == null
//                    || columnFamily.size() == 0) {
//                log.error("===Parameters tableName|columnFamily should not be null, Please check!===");
//                return false;
//            }
//            admin = connection.getAdmin();
//            if (admin.tableExists(TableName.valueOf(tableName))) {
//                return true;
//            } else {
//                List<ColumnFamilyDescriptor> familyDescriptors = new ArrayList<>(columnFamily.size());
//
//                columnFamily.forEach(cf -> {
//                    familyDescriptors.add(ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(cf)).build());
//                });
//
//                TableDescriptor tableDescriptor = TableDescriptorBuilder.newBuilder(TableName.valueOf(tableName))
//                        .setColumnFamilies(familyDescriptors)
//                        .build();
//
//                // 指定 splitkeys。
//                admin.createTable(tableDescriptor, splitKeys);
//                log.info("===Create Table " + tableName
//                        + " Success!columnFamily:" + columnFamily.toString()
//                        + "===");
//            }
//        } catch (IOException e) {
//            log.error("", e);
//            return false;
//        } finally {
//            close(admin, null, null);
//        }
//
//        return true;
//    }
//
//    /**
//     * 自定义获取分区 splitKeys。
//     *
//     * @param keys
//     * @return
//     */
//    public byte[][] getSplitKeys(String[] keys) {
//        if (keys == null) {
//            // 默认为 10 个分区。
//            keys = new String[]{"1|", "2|", "3|", "4|",
//                    "5|", "6|", "7|", "8|", "9|"};
//        }
//        byte[][] splitKeys = new byte[keys.length][];
//
//        // 升序排序。
//        TreeSet<byte[]> rows = new TreeSet<byte[]>(Bytes.BYTES_COMPARATOR);
//        for (String key : keys) {
//            rows.add(Bytes.toBytes(key));
//        }
//
//        Iterator<byte[]> rowKeyIter = rows.iterator();
//        int i = 0;
//        while (rowKeyIter.hasNext()) {
//            byte[] tempRow = rowKeyIter.next();
//            rowKeyIter.remove();
//            splitKeys[i] = tempRow;
//            i++;
//        }
//        return splitKeys;
//    }
//
//    /**
//     * 获取 table。
//     *
//     * @param tableName 表名。
//     * @return
//     * @throws IOException
//     */
//    private Table getTable(String tableName) throws IOException {
//        return connection.getTable(TableName.valueOf(tableName));
//    }
//
//    /**
//     * 查询库中所有表的表名。
//     *
//     * @return
//     */
//    public List<String> getAllTableNames() {
//        List<String> result = new ArrayList<>();
//
//        Admin admin = null;
//        try {
//            admin = connection.getAdmin();
//            TableName[] tableNames = admin.listTableNames();
//
//            for (TableName tableName : tableNames) {
//                result.add(tableName.getNameAsString());
//            }
//        } catch (IOException e) {
//            log.error("获取所有表的表名失败。", e);
//        } finally {
//            close(admin, null, null);
//        }
//        return result;
//    }
//
//
//    /**
//     * 查询库中所有表的表名。
//     */
//    public List<String> getAllFamilyNames(String tableName) {
//        ColumnFamilyDescriptor[] familyDescriptorList = null;
//        List<String> familyNameList = new ArrayList<String>();
//        try {
//            Table table = getTable(tableName);
//            familyDescriptorList = table.getDescriptor().getColumnFamilies();
//
//        } catch (IOException e) {
//            log.error("获取表的所有的列簇失败。", e);
//        } finally {
//            //    close(admin, null, null);
//        }
//        if (null != familyDescriptorList && familyDescriptorList.length > 0) {
//            familyNameList = Arrays.stream(familyDescriptorList).map(ColumnFamilyDescriptor::getNameAsString).collect(Collectors.toList());
//        }
//        return familyNameList;
//    }
//
//    /**
//     * 遍历查询指定表中的所有数据。遍历查询指定表中的所有数据。
//     *
//     * @param tableName
//     * @return
//     */
//    public Map<String, Map<String, String>> getResultScanner(String tableName) {
//        Scan scan = new Scan();
//        return this.queryData(tableName, scan);
//    }
//
//    /**
//     * 根据 startRowKey 和 stopRowKey 遍历查询指定表中的所有数据。
//     *
//     * @param tableName
//     * @param startRowKey
//     * @param stopRowKey
//     * @return
//     */
//    public Map<String, Map<String, String>> getResultScanner(String tableName, String startRowKey, String stopRowKey) {
//        Scan scan = new Scan();
//
//        if (StringUtils.isNoneBlank(startRowKey) && StringUtils.isNoneBlank(stopRowKey)) {
//            scan.withStartRow(Bytes.toBytes(startRowKey));
//            scan.withStopRow(Bytes.toBytes(stopRowKey));
//        }
//
//        return this.queryData(tableName, scan);
//    }
//
//    /**
//     * 通过行前缀过滤器查询数据。
//     *
//     * @param tableName 表名。
//     * @param prefix    以 prefix 开始的列名。
//     * @return
//     */
//    public Map<String, Map<String, String>> getResultScannerPrefixFilter(String tableName, String prefix) {
//        Scan scan = new Scan();
//
//        if (StringUtils.isNoneBlank(prefix)) {
//            Filter filter = new PrefixFilter(Bytes.toBytes(prefix));
//            scan.setFilter(filter);
//        }
//
//        return this.queryData(tableName, scan);
//    }
//
//    /**
//     * 通过列前缀过滤器查询数据。
//     *
//     * @param tableName
//     * @param prefix
//     * @return
//     */
//    public Map<String, Map<String, String>> getResultScannerColumnPrefixFilter(String tableName, String prefix) {
//        Scan scan = new Scan();
//
//        if (StringUtils.isNoneBlank(prefix)) {
//            Filter filter = new ColumnPrefixFilter(Bytes.toBytes(prefix));
//            scan.setFilter(filter);
//        }
//
//        return this.queryData(tableName, scan);
//    }
//
//    /**
//     * 查询行键中包含特定字符的数据。
//     *
//     * @param tableName
//     * @param keyword   包含指定关键词的行键。
//     * @return
//     */
//    public Map<String, Map<String, String>> getResultScannerRowFilter(String tableName, String keyword) {
//        Scan scan = new Scan();
//
//        if (StringUtils.isNoneBlank(keyword)) {
//            Filter filter = new RowFilter(CompareOperator.GREATER_OR_EQUAL, new SubstringComparator(keyword));
//            scan.setFilter(filter);
//        }
//
//        return this.queryData(tableName, scan);
//    }
//
//    /**
//     * 查询列名中包含特定字符的数据。
//     *
//     * @param tableName
//     * @param keyword   包含指定关键词的列名。
//     * @return
//     */
//    public Map<String, Map<String, String>> getResultScannerQualifierFilter(String tableName, String keyword) {
//        Scan scan = new Scan();
//
//        if (StringUtils.isNoneBlank(keyword)) {
//            Filter filter = new QualifierFilter(CompareOperator.GREATER_OR_EQUAL, new SubstringComparator(keyword));
//            scan.setFilter(filter);
//        }
//
//        return this.queryData(tableName, scan);
//    }
//
//
//    /**
//     * 通过表名以及过滤条件查询数据。
//     *
//     * @param tableName
//     * @param scan      过滤条件。
//     * @return
//     */
//    private Map<String, Map<String, String>> queryData(String tableName, Scan scan) {
//        // <rowKey, 对应的行数据>。
//        Map<String, Map<String, String>> result = new HashMap<>();
//
//        ResultScanner resultScanner = null;
//        // 获取表。
//        Table table = null;
//        try {
//            table = getTable(tableName);
//            resultScanner = table.getScanner(scan);
//            for (Result r : resultScanner) {
//                //每一行数据
//                Map<String, String> columnMap = new HashMap<>();
//                String rowKey = null;
//                for (Cell cell : r.listCells()) {
//                    if (rowKey == null) {
//                        rowKey = Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength());
//                    }
//                    columnMap.put(Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()), Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
//                }
//
//                if (rowKey != null) {
//                    result.put(rowKey, columnMap);
//                }
//            }
//        } catch (IOException e) {
//            log.error(MessageFormat.format("遍历查询指定表中的所有数据失败，tableName：{0}"
//                    , tableName), e);
//        } finally {
//            close(null, resultScanner, table);
//        }
//
//        return result;
//    }
//
//    /**
//     * 根据 tableName 和 rowKey 精确查询一行的数据。
//     *
//     * @param tableName
//     * @param rowKey    行键。
//     * @return
//     */
//    public Map<String, String> getRowData(String tableName, String rowKey) {
//        // 返回的键值对。
//        Map<String, String> result = new HashMap<>();
//
//        Get get = new Get(Bytes.toBytes(rowKey));
//        // 获取表。
//        Table table = null;
//        try {
//            table = getTable(tableName);
//            Result hTableResult = table.get(get);
//            if (hTableResult != null && !hTableResult.isEmpty()) {
//                for (Cell cell : hTableResult.listCells()) {
////                    System.out.println("family:" + Bytes.toString(cell.getFamilyArray(), cell.getFamilyOffset(), cell.getFamilyLength()));
////                    System.out.println("qualifier:" + Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()));
////                    System.out.println("value:" + Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
////                    System.out.println("Timestamp:" + cell.getTimestamp());
////                    System.out.println("～　～　～　～　～　～　～");
//                    result.put(Bytes.toString(cell.getQualifierArray(), cell.getQualifierOffset(), cell.getQualifierLength()), Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
//                }
//            }
//        } catch (IOException e) {
//            log.error(MessageFormat.format("查询一行的数据失败，tableName：{0}，rowKey：{1}", tableName, rowKey), e);
//        } finally {
//            close(null, null, table);
//        }
//
//        return result;
//    }
//
//
//    public Result getHBaseResult(String tableName, String rowKey) {
//        Get get = new Get(Bytes.toBytes(rowKey));
//        // 获取表。
//        Table table = null;
//        Result hTableResult = null;
//        try {
//            table = getTable(tableName);
//            hTableResult = table.get(get);
//        } catch (IOException e) {
//            log.error(MessageFormat.format("查询一行的数据失败,tableName：{0}，rowKey：{1}"
//                    , tableName, rowKey), e);
//        } finally {
//            close(null, null, table);
//        }
//        return hTableResult;
//    }
//
//    /**
//     * 根据 tableName 和 rowKey 精确查询一行的数据。
//     *
//     * @param tableName
//     * @param rowKey
//     * @param familyName
//     * @return
//     */
//    public StorageData getStorageData(String tableName, String rowKey, String familyName) {
//        Result hTableResult = getHBaseResult(tableName, rowKey);
//        return getStorageDataForFamilyName(hTableResult, familyName);
//    }
//
//    public List<StorageData> getStorageDataList(String tableName, String rowKey, List<String> familyNameList) {
//        List<StorageData> HBaseDataList = new ArrayList<StorageData>();
//        Result hTableResult = getHBaseResult(tableName, rowKey);
//        for (String familyName : familyNameList) {
//            StorageData HBaseData = getStorageDataForFamilyName(hTableResult, familyName);
//            if (null != HBaseData) {
//                HBaseDataList.add(HBaseData);
//            }
//        }
//        return HBaseDataList;
//    }
//
//    public StorageData getStorageDataForFamilyName(Result hTableResult, String familyName) {
//        StorageData storageData = null;
//        if (hTableResult != null && !hTableResult.isEmpty()) {
//            Map<byte[], byte[]> familyMap = hTableResult.getFamilyMap(Bytes.toBytes(familyName));
//            if (null != familyMap && !familyMap.isEmpty()) {
//                storageData = new StorageData();
//                for (Map.Entry<byte[], byte[]> entry : familyMap.entrySet()) {
//                    storageData.setTargetClassName(familyName);
//                    storageData.addStorageEntry(Bytes.toString(entry.getKey()), Bytes.toString(entry.getValue()));
//                }
//            }
//        }
//        return storageData;
//    }
//
//    /**
//     * 根据 tableName、rowKey、familyName、column 查询指定单元格的数据。
//     *
//     * @param tableName
//     * @param rowKey
//     * @param familyName
//     * @param columnName
//     * @return
//     */
//    public String getColumnValue(String tableName, String rowKey, String familyName, String columnName) {
//        String str = null;
//        Get get = new Get(Bytes.toBytes(rowKey));
//        // 获取表。
//        Table table = null;
//        try {
//            table = getTable(tableName);
//            Result result = table.get(get);
//            if (result != null && !result.isEmpty()) {
//                Cell cell = result.getColumnLatestCell(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
//                if (cell != null) {
//                    str = Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength());
//                }
//            }
//        } catch (IOException e) {
//            log.error(MessageFormat.format("查询指定单元格的数据失败,tableName：{0}，rowKey：{1}，familyName：{2}，columnName：{3}"
//                    , tableName, rowKey, familyName, columnName), e);
//        } finally {
//            close(null, null, table);
//        }
//
//        return str;
//    }
//
//    /**
//     * 根据 tableName、rowKey、familyName、column 查询指定单元格多个版本的数据。
//     *
//     * @param tableName
//     * @param rowKey
//     * @param familyName
//     * @param columnName
//     * @param versions
//     * @return
//     */
//    public List<String> getColumnValuesByVersion(String tableName, String rowKey, String familyName, String columnName, int versions) {
//        // 返回数据。
//        List<String> result = new ArrayList<>(versions);
//
//        // 获取表。
//        Table table = null;
//        try {
//            table = getTable(tableName);
//            Get get = new Get(Bytes.toBytes(rowKey));
//            get.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
//            // 读取多少个版本。
//            get.readVersions(versions);
//            Result hTableResult = table.get(get);
//            if (hTableResult != null && !hTableResult.isEmpty()) {
//                for (Cell cell : hTableResult.listCells()) {
//                    result.add(Bytes.toString(cell.getValueArray(), cell.getValueOffset(), cell.getValueLength()));
//                }
//            }
//        } catch (IOException e) {
//            log.error(MessageFormat.format("查询指定单元格多个版本的数据失败,tableName：{0}，rowKey：{1}，familyName：{2}，columnName：{3}"
//                    , tableName, rowKey, familyName, columnName), e);
//        } finally {
//            close(null, null, table);
//        }
//
//        return result;
//    }
//
//    /**
//     * 为表添加 or 更新数据。
//     *
//     * @param tableName  表名。
//     * @param rowKey     rowKey。
//     * @param familyName 列族名。
//     * @param columns    列名数组。
//     * @param values     列值得数组。
//     */
//    public void putData(String tableName, String rowKey, String familyName, String[] columns, String[] values) {
//        // 获取表
//        Table table = null;
//        try {
//            table = getTable(tableName);
//
//            putData(table, rowKey, tableName, familyName, columns, values);
//        } catch (Exception e) {
//            log.error(MessageFormat.format("为表添加 or 更新数据失败,tableName：{0}，rowKey：{1},familyName:{2}"
//                    , tableName, rowKey, familyName), e);
//        } finally {
//            close(null, null, table);
//        }
//    }
//
//    /**
//     * 为表添加 or 更新数据。
//     *
//     * @param table      Table。
//     * @param rowKey     rowKey。
//     * @param tableName  表名。
//     * @param familyName 列族名。
//     * @param columns    列名数组。
//     * @param values     列值得数组。
//     */
//    private void putData(Table table, String rowKey, String tableName, String familyName, String[] columns, String[] values) {
//        try {
//            // 设置 rowkey。
//            Put put = new Put(Bytes.toBytes(rowKey));
//
//            if (columns != null && values != null && columns.length == values.length) {
//                for (int i = 0; i < columns.length; i++) {
//                    if (columns[i] != null && values[i] != null) {
//                        put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(columns[i]), Bytes.toBytes(values[i]));
//                    } else {
//                        throw new NullPointerException(MessageFormat.format("列名和列数据都不能为空。column：{0}，value：{1}"
//                                , columns[i], values[i]));
//                    }
//                }
//            }
//
//            table.put(put);
//            log.debug("putData add or update data Success,rowKey:" + rowKey);
//            table.close();
//        } catch (Exception e) {
//            log.error(MessageFormat.format("为表添加 or 更新数据失败,tableName：{0}，rowKey：{1}，familyName：{2}", tableName, rowKey, familyName), e);
//        }
//    }
//
//    /**
//     * 为表的某个单元格赋值。
//     *
//     * @param tableName  表名。
//     * @param rowKey     rowKey。
//     * @param familyName 列族名。
//     * @param column1    列名。
//     * @param value1     列值。
//     */
//    public void setColumnValue(String tableName, String rowKey, String familyName, String column1, String value1) {
//        Table table = null;
//        try {
//            // 获取表。
//            table = getTable(tableName);
//            // 设置 rowKey。
//            Put put = new Put(Bytes.toBytes(rowKey));
//            put.addColumn(Bytes.toBytes(familyName), Bytes.toBytes(column1), Bytes.toBytes(value1));
//
//            table.put(put);
//            log.debug("add data Success!");
//        } catch (IOException e) {
//            log.error(MessageFormat.format("为表的某个单元格赋值失败,tableName：{0}，rowKey：{1},familyName:{2},column:{3}"
//                    , tableName, rowKey, familyName, column1), e);
//        } finally {
//            close(null, null, table);
//        }
//    }
//
//    /**
//     * 删除指定的单元格。
//     *
//     * @param tableName  表名。
//     * @param rowKey     rowKey。
//     * @param familyName 列族名。
//     * @param columnName 列名。
//     * @return
//     */
//    public boolean deleteColumn(String tableName, String rowKey, String familyName, String columnName) {
//        Table table = null;
//        Admin admin = null;
//        try {
//            admin = connection.getAdmin();
//
//            if (admin.tableExists(TableName.valueOf(tableName))) {
//                // 获取表
//                table = getTable(tableName);
//                Delete delete = new Delete(Bytes.toBytes(rowKey));
//                // 设置待删除的列
//                delete.addColumns(Bytes.toBytes(familyName), Bytes.toBytes(columnName));
//
//                table.delete(delete);
//                log.debug(MessageFormat.format("familyName({0}):columnName({1})is deleted!", familyName, columnName));
//            }
//
//        } catch (IOException e) {
//            log.error(MessageFormat.format("删除指定的列失败,tableName：{0}，rowKey：{1},familyName:{2},column:{3}"
//                    , tableName, rowKey, familyName, columnName), e);
//            return false;
//        } finally {
//            close(admin, null, table);
//        }
//        return true;
//    }
//
//    /**
//     * 根据 rowKey 删除指定的行。
//     *
//     * @param tableName
//     * @param rowKey
//     * @return
//     */
//    public boolean deleteRow(String tableName, String rowKey) {
//        Table table = null;
//        Admin admin = null;
//        try {
//            admin = connection.getAdmin();
//
//            if (admin.tableExists(TableName.valueOf(tableName))) {
//                // 获取表。
//                table = getTable(tableName);
//                Delete delete = new Delete(Bytes.toBytes(rowKey));
//
//                table.delete(delete);
//                log.debug(MessageFormat.format("row({0}) is deleted!", rowKey));
//            }
//        } catch (IOException e) {
//            log.error(MessageFormat.format("删除指定的行失败。tableName：{0}，rowKey：{1}"
//                    , tableName, rowKey), e);
//            return false;
//        } finally {
//            close(admin, null, table);
//        }
//        return true;
//    }
//
//    /**
//     * 根据 columnFamily 删除指定的列族。
//     *
//     * @param tableName    表名。
//     * @param columnFamily 列族。
//     * @return
//     */
//    public boolean deleteColumnFamily(String tableName, String columnFamily) {
//        Admin admin = null;
//        try {
//            admin = connection.getAdmin();
//
//            if (admin.tableExists(TableName.valueOf(tableName))) {
//                admin.deleteColumnFamily(TableName.valueOf(tableName), Bytes.toBytes(columnFamily));
//                log.debug(MessageFormat.format("familyName({0}) is deleted!", columnFamily));
//            }
//        } catch (IOException e) {
//            log.error(MessageFormat.format("删除指定的列族失败,tableName：{0}，columnFamily：{1}"
//                    , tableName, columnFamily), e);
//            return false;
//        } finally {
//            close(admin, null, null);
//        }
//        return true;
//    }
//
//    /**
//     * 删除表。
//     *
//     * @param tableName
//     * @return
//     */
//    public boolean deleteTable(String tableName) {
//        Admin admin = null;
//        try {
//            admin = connection.getAdmin();
//
//            if (admin.tableExists(TableName.valueOf(tableName))) {
//                admin.disableTable(TableName.valueOf(tableName));
//                admin.deleteTable(TableName.valueOf(tableName));
//                log.debug(tableName + "is deleted!");
//            }
//        } catch (IOException e) {
//            log.error(MessageFormat.format("删除指定的表失败,tableName:{0}"
//                    , tableName), e);
//            return false;
//        } finally {
//            close(admin, null, null);
//        }
//        return true;
//    }
//
//    /**
//     * 关闭流。
//     *
//     * @param admin
//     * @param resultScanner
//     * @param table
//     */
//    private void close(Admin admin, ResultScanner resultScanner, Table table) {
//        if (admin != null) {
//            try {
//                admin.close();
//            } catch (IOException e) {
//                log.error("关闭 Admin 失败。", e);
//            }
//        }
//
//        if (resultScanner != null) {
//            resultScanner.close();
//        }
//
//        if (table != null) {
//            try {
//                table.close();
//            } catch (IOException e) {
//                log.error("关闭 Table 失败。", e);
//            }
//        }
//    }
//
//    public Configuration getConf() {
//        return conf;
//    }
//
//    public void setConf(Configuration conf) {
//        this.conf = conf;
//    }
//
//}
