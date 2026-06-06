package com.devmasters.restaurant_erp.common.model.file;

import com.devmasters.restaurant_erp.common.enums.FileType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileUploadResponseModel {

    private String fileId;

    private String url;

    private FileType fileType;

    private long size;
}
