package ru.hogwarts.school.service.implementation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.StudentService;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarServiceImpl implements AvatarService {
    private final AvatarRepository avatarRepository;
    private final StudentService studentService;

    @Value("${path.to.students.avatars.folder}")
    private String avatarsDir;

    public AvatarServiceImpl(AvatarRepository avatarRepository,
                             StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private byte[] generateAvatarThumbnailImage(Path avatarImagePath) throws IOException {
        try (InputStream is = Files.newInputStream(avatarImagePath);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             BufferedInputStream bis = new BufferedInputStream(is)) {
            BufferedImage avatarImage = ImageIO.read(bis);

            int newWidth = 100;
            int newHeight = avatarImage.getHeight() / (avatarImage.getWidth() / newWidth);
            BufferedImage avatarThumbnailImage = new BufferedImage(newWidth, newHeight, avatarImage.getType());
            Graphics2D graphics = avatarThumbnailImage.createGraphics();
            graphics.drawImage(avatarImage, 0, 0, newWidth, newHeight, null);
            graphics.dispose();

            ImageIO.write(avatarThumbnailImage, getExtensions(avatarImagePath.getFileName().toString()), baos);

            return baos.toByteArray();
        }
    }

    @Override
    public Avatar get(Long studentId) {
        return avatarRepository.findAvatarByStudentId(studentId).orElse(new Avatar());
    }

    @Override
    public void upload(Long studentId, MultipartFile avatarFile) throws IOException {
        Student student = studentService.get(studentId);
        Path filePath = Path.of(avatarsDir, student.getId() + "." + getExtensions(Objects.requireNonNull(avatarFile.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = avatarFile.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is);
             BufferedOutputStream bos = new BufferedOutputStream(os)) {
            bis.transferTo(bos);
        }

        Avatar avatar = get(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(generateAvatarThumbnailImage(filePath));
        avatarRepository.save(avatar);
    }
}
