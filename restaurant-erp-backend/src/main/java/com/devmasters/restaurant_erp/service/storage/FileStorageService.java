package com.devmasters.restaurant_erp.service.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String upload(MultipartFile file, String folder);

    void delete(String fileUrl);
}