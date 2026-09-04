package com.spring_boot_api_p2.feature.intergration.filestorage.impl;

import com.spring_boot_api_p2.feature.intergration.filestorage.FileStorageService;
import com.spring_boot_api_p2.property.MinioProperties;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final MinioClient minioClient;
    private final MinioProperties minioProperties;

    /**
     * The only images we accept: file extension -> the content type stored on
     * the object. Read forwards for the filename, and by value for a declared
     * content type, so this one map answers both questions.
     */
    private static final Map<String, String> EXTENSION_TYPES = Map.of(
            "png", "image/png",
            "jpg", "image/jpeg",
            "jpeg", "image/jpeg",
            "webp", "image/webp",
            "gif", "image/gif");

    @Override
    public String storeImage(MultipartFile file, String subDir) {
        if (file == null || file.isEmpty()) {
            throw new ValidationException("Image file is required");
        }

        String extension = extensionOf(file.getOriginalFilename());
        String contentType = extension == null ? null : EXTENSION_TYPES.get(extension);

        String declaredType = file.getContentType() == null
                ? ""
                : file.getContentType().toLowerCase(Locale.ROOT).trim();
        if (contentType == null && EXTENSION_TYPES.containsValue(declaredType)) {
            contentType = declaredType;
            extension = declaredType.substring(declaredType.indexOf('/') + 1);
        }

        if (contentType == null) {
            throw new ValidationException(
                    "Unsupported image type: " + (declaredType.isBlank() ? "unknown" : declaredType));
        }

        long maxBytes = (long) minioProperties.getMaxSizeMb() * 1024 * 1024;
        if (file.getSize() > maxBytes) {
            throw new ValidationException(
                    "Image is larger than " + minioProperties.getMaxSizeMb() + "MB");
        }

        String objectKey = subDir + "/" + UUID.randomUUID() + "." + extension;
        try (InputStream in = file.getInputStream()) {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(objectKey)
                    .stream(in, file.getSize(), -1)
                    .contentType(contentType)
                    .build());
        } catch (Exception e) {
            throw new ValidationException("Could not upload the image. Please try again.");
        }
        return objectKey;
    }

    private String extensionOf(String filename) {
        if (filename == null) {
            return null;
        }
        int dot = filename.lastIndexOf('.');
        if (dot < 0 || dot == filename.length() - 1) {
            return null;
        }
        return filename.substring(dot + 1).toLowerCase(Locale.ROOT);
    }

    @Override
    public void deleteObject(String objectKeyOrUrl) {
        String key = minioProperties.toObjectKey(objectKeyOrUrl);
        if (key == null || key.isBlank()) {
            return;
        }
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .object(key)
                    .build());
        } catch (Exception ignored) {
        }
    }
    @Override
    public String getFileUrl(String objectKeyOrUrl) {
        String key = minioProperties.toObjectKey(objectKeyOrUrl);
        if (key == null || key.isBlank()) {
            return null;
        }
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET) // <--- ប្តូរពី Method.GET មកជា Method.PUT ទីនេះ
                            .bucket(minioProperties.getBucket())
                            .object(key)
                            .expiry(1, TimeUnit.HOURS)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate image URL", e);
        }
    }


    @Override
    public String replaceImage(String oldObjectKey, MultipartFile newFile, String subDir) {
        // 1. Upload រូបភាពថ្មីជាមុនសិន ដើម្បីធានាថាបើ upload បរាជ័យ រូបចាស់មិនត្រូវបាត់បង់
        String newObjectKey = storeImage(newFile, subDir);

        // 2. លុបរូបភាពចាស់ចេញពី MinIO ប្រសិនបើមាន objectKey ចាស់
        if (oldObjectKey != null && !oldObjectKey.isBlank()) {
            deleteObject(oldObjectKey);
        }

        // 3. ផ្ដល់ត្រឡប់មកវិញនូវ key នៃរូបភាពថ្មី
        return newObjectKey;
    }
}
