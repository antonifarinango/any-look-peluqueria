package com.ecommerce.project.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    @Autowired
    private Cloudinary cloudinary;

    @Value("${cloudinary.cloud_name}")
    private String cloudName;

    @Override
    public String uploadImage(String folder, MultipartFile file) throws IOException {
        if (cloudName != null && !cloudName.isEmpty()) {
            // Cloudinary Mode: 'folder' will be used as the folder name in Cloudinary
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    ObjectUtils.asMap("folder", folder));
            return (String) uploadResult.get("secure_url");
        } else {
            // Local Mode
            String originalFileName = file.getOriginalFilename();
            String randomId = UUID.randomUUID().toString();
            String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf(".")));
            
            // Re-use 'folder' as the local path (e.g., "images/")
            String filePath = folder + File.separator + fileName;
            
            File f = new File(folder);
            if (!f.exists()) {
                f.mkdirs();
            }
            
            Files.copy(file.getInputStream(), Paths.get(filePath));
            return fileName;
        }
    }

    @Override
    public boolean deleteFile(String folder, String imageUrl) throws IOException {
        if (imageUrl == null || imageUrl.isEmpty() || imageUrl.equals("default.png")) {
            return false;
        }

        if (imageUrl.startsWith("http")) {
            // Cloudinary Mode
            try {
                String fileNameWithExt = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                String fileName = fileNameWithExt.substring(0, fileNameWithExt.lastIndexOf("."));

                String folderName = folder;
                if (folderName.endsWith("/")) {
                    folderName = folderName.substring(0, folderName.length() - 1);
                }

                String publicId = folderName + "/" + fileName;
                Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                return "ok".equals(result.get("result"));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            // Local Mode
            String filePath = folder + File.separator + imageUrl;
            try {
                File file = new File(filePath);
                if (file.exists()) {
                    return file.delete();
                }
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

}


