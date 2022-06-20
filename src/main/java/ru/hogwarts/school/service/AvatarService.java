package ru.hogwarts.school.service;

import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;

import java.io.IOException;

public interface AvatarService {
    Avatar get(Long studentId);

    byte[] getFileData(Long studentId) throws IOException;

    void upload(Long studentId, MultipartFile avatar) throws IOException;
}
