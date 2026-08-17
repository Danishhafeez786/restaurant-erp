package com.devmasters.restaurant_erp.order.service.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {

    @Value("${file.storage.location:uploads}")
    private String storageLocation;

    @Override
    public String upload(MultipartFile file, String folder) {

        try {
            Path directory = Paths.get(storageLocation, folder);

            Files.createDirectories(directory);

            String extension = "";

            if (file.getOriginalFilename() != null &&
                    file.getOriginalFilename().contains(".")) {

                extension = file.getOriginalFilename()
                        .substring(
                                file.getOriginalFilename().lastIndexOf(".")
                        );
            }

            String fileName =
                    UUID.randomUUID() + extension;

            Path target =
                    directory.resolve(fileName);

            Files.copy(
                    file.getInputStream(),
                    target
            );

            return "/uploads/" + folder + "/" + fileName;

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to upload file",
                    e
            );
        }
    }

    @Override
    public void delete(String fileUrl) {

        try {

            String relativePath =
                    fileUrl.replaceFirst("^/uploads/", "");

            Path file =
                    Paths.get(storageLocation)
                            .resolve(relativePath);

            Files.deleteIfExists(file);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to delete file",
                    e
            );
        }
    }
}