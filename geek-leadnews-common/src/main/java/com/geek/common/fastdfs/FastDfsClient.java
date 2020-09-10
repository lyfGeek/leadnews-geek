package com.geek.common.fastdfs;

import com.luhuiguo.fastdfs.domain.StorePath;
import com.luhuiguo.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * dfs 客户端。
 */
@Component
public class FastDfsClient {

    @Autowired
    FastFileStorageClient storageClient;

    /**
     * 上传文件方法。
     *
     * @param fileName 文件全路径。
     * @param extName  文件扩展名，不包含（.）。
     * @return
     * @throws Exception
     */
    public String uploadFile(String fileName, String extName) throws Exception {
        StorePath s = storageClient.uploadFile(FileUtils.readFileToByteArray(new File(fileName)), extName);
        String result = s.getFullPath();
        return result;
    }

    public String uploadFile(String fileName) throws Exception {
        return uploadFile(fileName, null);
    }

    /**
     * 上传文件方法。
     *
     * @param fileContent 文件的内容，字节数组。
     * @param extName     文件扩展名。
     * @return
     */
    public String uploadFile(byte[] fileContent, String extName) {
        StorePath s = storageClient.uploadFile(fileContent, extName);
        String result = s.getFullPath();
        return result;
    }

    public String uploadFile(byte[] fileContent) {
        return uploadFile(fileContent, null);
    }

    /**
     * 文件下载方法。
     *
     * @param fileId
     * @return
     */
    public byte[] downFile(String fileId) {
        return storageClient.downloadFile("", fileId);
    }

    /**
     * 文件下载方法。
     *
     * @param group
     * @param fileId
     * @return
     */
    public byte[] downGroupFile(String group, String fileId) {
        return storageClient.downloadFile(group, fileId);
    }

    public int delFile(String fileId) {
        storageClient.deleteFile(fileId);
        return 1;
    }

}
