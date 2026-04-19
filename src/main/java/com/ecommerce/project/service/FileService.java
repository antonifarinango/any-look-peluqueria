package com.ecommerce.project.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {
    String uploadImage(String folder, MultipartFile file) throws IOException;

    // Implementado para eliminar una img de Cloudinary
    boolean deleteFile(String folder, String url) throws IOException;
}

