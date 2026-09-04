package com.spring_boot_api_p2.feature.intergration.filestorage;

import org.springframework.web.multipart.MultipartFile;


public interface FileStorageService {
    String storeImage(MultipartFile file, String subDir);
    void deleteObject(String objectKeyOrUrl);
    String getFileUrl(String objectKeyOrUrl);
    String replaceImage(String oldObjectKey, MultipartFile newFile, String subDir);
}
