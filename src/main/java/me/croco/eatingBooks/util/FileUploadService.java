package me.croco.eatingBooks.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileUploadService {

    @Value("${file.upload.path}")
    private String fileUploadDir;

    public String uploadFile(MultipartFile file, String type) {
        if(file == null) {
            return null;
        }
        // 정규화된 파일 이름을 생성
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        Path fileStorageLocation = Paths.get(fileUploadDir + type).toAbsolutePath().normalize();

        try {
            Files.createDirectories(fileStorageLocation);

            // 파일명에 부적절한 문자가 있는지 검사
            if (fileName.contains("..")) {
                throw new RuntimeException("파일명 부적합 : " + fileName);
            }

            // 대상 위치에 파일을 저장합니다. 같은 이름의 파일이 있을 경우 덮어씁니다.
            Path targetLocation = fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);


            return targetLocation+"";
        } catch (IOException ex) {
            throw new RuntimeException("파일 저장 실패 :  " + fileName);
        }
    }
}
