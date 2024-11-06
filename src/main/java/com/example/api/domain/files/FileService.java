package com.example.api.domain.files;

import com.example.api.repositories.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileService {

    private final String uploadDir = "uploads/";
    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    @Autowired
    private FileRepository fileRepository;

    // Helper method to get the file extension
    private String getFileExtension(String fileName) {
        int lastIndexOfDot = fileName.lastIndexOf(".");
        if (lastIndexOfDot == -1) {
            return ""; // Empty extension
        }
        return fileName.substring(lastIndexOfDot + 1);
    }

    public List<File> saveFiles(MultipartFile[] files) throws IOException {
        List<File> savedFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            assert fileName != null;
            String fileExtension = getFileExtension(fileName); // Get the file extension
            String uuid = UUID.randomUUID().toString(); // Generate a UUID
            String uuidFileName = uuid + (fileExtension.isEmpty() ? "" : "." + fileExtension); // Append file extension if it exists

            Path filePath = Paths.get(uploadDir + uuidFileName);

            // Save file to the server with UUID as the file name
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            // Save file metadata to the database
            File fileEntity = new File();
            fileEntity.setName(fileName);  // Original file name
            fileEntity.setPath(uuidFileName);  // UUID as the file path
            savedFiles.add(fileRepository.save(fileEntity));
        }
        return savedFiles;
    }

    public void deleteFile(Long id) {
        try {
            // Find the file in the database
            var file = fileRepository.findById(id).orElse(null);
            if (file == null) {
                return; // File not found in the database
            }

            // Delete the file from the server
            Path filePath = fileStorageLocation.resolve(file.getPath()).normalize();
            Files.deleteIfExists(filePath);

            // Delete the file entry from the database
            fileRepository.delete(file);

        } catch (IOException ex) {
            // Handle the error (log it, rethrow it, etc.)
        }
    }

    public Resource loadFileAsResource(String uuidFileName) throws MalformedURLException {
        Path filePath = Paths.get(uploadDir).resolve(uuidFileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("File not found: " + uuidFileName);
        }
    }


}
