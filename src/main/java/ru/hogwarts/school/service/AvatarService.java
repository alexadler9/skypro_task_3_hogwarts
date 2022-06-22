package ru.hogwarts.school.service;

import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;

import java.io.IOException;
import java.util.Collection;

public interface AvatarService {
    Avatar get(Long studentId);

    Collection<Avatar> getAll(int page, int size);

    byte[] getFileData(Long studentId) throws IOException;

    void upload(Long studentId, MultipartFile avatar) throws IOException;
}
