package com.spring_boot_api_p2.feature.core;

import com.spring_boot_api_p2.feature.intergration.filestorage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class TestImageServiceImpl implements TestImageService {
     private final FileStorageService fileStorageService;
     private static final  String PROFILE_IMAGE_DIR ="profiles";


    @Override
    public void testImage(MultipartFile file) {
        fileStorageService.storeImage(file, PROFILE_IMAGE_DIR);

    }
}
