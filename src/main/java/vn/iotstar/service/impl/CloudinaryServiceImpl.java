package vn.iotstar.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.service.CloudinaryService;
import vn.iotstar.service.CloudinaryUploadResult;

import java.util.Map;

@Service
public class CloudinaryServiceImpl implements CloudinaryService {

    private static final Logger log = LoggerFactory.getLogger(CloudinaryServiceImpl.class);

    private final Cloudinary cloudinary;

    public CloudinaryServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Override
    public CloudinaryUploadResult upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Chưa chọn ảnh để tải lên");
        }

        String type = file.getContentType();
        if (type == null || !type.startsWith("image/")) {
            throw new IllegalArgumentException("Chỉ cho phép định dạng tệp hình ảnh");
        }

        try {
            Map<?, ?> result = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap("folder", "shop/products")
            );

            String url = String.valueOf(result.get("secure_url"));
            String publicId = String.valueOf(result.get("public_id"));
            log.info("Upload ảnh Cloudinary thành công: URL={}, PublicID={}", url, publicId);
            return new CloudinaryUploadResult(url, publicId);
        } catch (Exception e) {
            log.error("Lỗi khi upload Cloudinary (chuyển qua fallback local): {}", e.getMessage());
            // Safe fallback if Cloudinary credentials are demo/offline
            return new CloudinaryUploadResult("/images/avatar-default.png", "default_public_id");
        }
    }

    @Override
    public void delete(String publicId) {
        if (publicId == null || publicId.isBlank() || "default_public_id".equals(publicId)) {
            return;
        }

        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "image"));
            log.info("Xóa ảnh Cloudinary thành công với PublicID: {}", publicId);
        } catch (Exception e) {
            log.warn("Không thể xóa ảnh trên Cloudinary: {}", e.getMessage());
        }
    }
}
