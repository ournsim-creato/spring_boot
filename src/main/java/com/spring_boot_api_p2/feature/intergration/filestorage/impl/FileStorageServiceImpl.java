package com.spring_boot_api_p2.feature.intergration.filestorage.impl;

import com.spring_boot_api_p2.feature.intergration.filestorage.FileStorageService;
import com.spring_boot_api_p2.property.MinioProperties;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

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
        // The filename decides first: it is what the object key has to end in
        // anyway, and a client that forgets the per-part content type still
        // names its file photo.png.
        String extension = extensionOf(file.getOriginalFilename());
        String contentType = extension == null ? null : EXTENSION_TYPES.get(extension);

        // No usable extension — fall back to the type the client declared.
        // Never null here: an absent header becomes "", which matches nothing.
        String declaredType = file.getContentType() == null
                ? ""
                : file.getContentType().toLowerCase(Locale.ROOT).trim();
        if (contentType == null && EXTENSION_TYPES.containsValue(declaredType)) {
            contentType = declaredType;
            // "image/jpeg" -> "jpeg", which is itself a key of the map above.
            extension = declaredType.substring(declaredType.indexOf('/') + 1);
        }

        // Both are client-supplied strings, so this says the upload claims to be
        // an image — not that its bytes actually are one.
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
//            log.error("MinIO upload failed for key {}: {}", objectKey, e.getMessage());
            throw new ValidationException("Could not upload the image. Please try again.");
        }
//        log.info("stored image: key={}, size={}B", objectKey, file.getSize());
        return objectKey;
    }

    /**
     * Lower-cased extension of a filename ("photo.PNG" -> "png"), or null when
     * there is no name, no dot, or nothing after it.
     */
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
//            log.debug("deleted object: key={}", key);
        } catch (Exception e) {
//            log.warn("Could not delete object {}: {}", key, e.getMessage());
        }
    }

    @Override
    public String getFileUrl(String objectKeyOrUrl) {
        String key = minioProperties.toObjectKey(objectKeyOrUrl);
        if (key == null || key.isBlank()) {
            return null;
        }
        return minioProperties.resolvedEndpoint() + "/" + minioProperties.getBucket() + "/" + key;
    }
}