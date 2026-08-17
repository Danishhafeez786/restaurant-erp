package com.devmasters.restaurant_erp.order.service.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String upload(MultipartFile file, String folder);

    void delete(String fileUrl);
}