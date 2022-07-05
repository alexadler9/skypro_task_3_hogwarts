package ru.hogwarts.school.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.exception.InvalidRequestParameterException;
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
import java.util.Collection;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarServiceImpl implements AvatarService {
    private final AvatarRepository avatarRepository;
    private final StudentService studentService;

    @Value("${path.to.students.avatars.folder}")
    private String avatarsDir;

    Logger logger = LoggerFactory.getLogger(AvatarServiceImpl.class);

    public AvatarServiceImpl(AvatarRepository avatarRepository,
                             StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }

    private String getExtensions(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private Avatar getOrCreate(Long studentId) {
        return avatarRepository.findAvatarByStudentId(studentId).orElse(new Avatar());
    }

    private byte[] generateAvatarThumbnailImage(Path avatarImagePath) throws IOException {
        logger.debug("Was invoked method for generate avatar thumbnail");
        try (InputStream is = Files.newInputStream(avatarImagePath);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             BufferedInputStream bis = new BufferedInputStream(is, 1024)) {
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
        logger.info("Was invoked method for get avatar for student {}", studentId);
        return avatarRepository.findAvatarByStudentId(studentId).orElse(null);
    }

    @Override
    public Collection<Avatar> getAll(int page, int size) {
        logger.info("Was invoked method for get all avatars; page: {}, size: {}", page, size);
        if (page < 1) {
            logger.error("Invalid page number");
            throw new InvalidRequestParameterException("Invalid page number");
        }
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return avatarRepository.findAll(pageRequest).getContent();
    }

    @Override
    public byte[] getFileData(Long studentId) throws IOException {
        logger.info("Was invoked method for get avatar file data for student {}", studentId);
        Avatar avatar = get(studentId);
        if (avatar == null) {
            logger.warn("Avatar is missing");
            return null;
        }

        try (InputStream is = Files.newInputStream(Path.of(avatar.getFilePath()));
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             BufferedInputStream bis = new BufferedInputStream(is, 1024)) {
            bis.transferTo(baos);
            return baos.toByteArray();
        }
    }

    @Override
    @Transactional
    public void upload(Long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("Was invoked method for update avatar for student {}", studentId);
        Student student = studentService.get(studentId);
        Path filePath = Path.of(avatarsDir, student.getId() + "." + getExtensions(Objects.requireNonNull(avatarFile.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (InputStream is = avatarFile.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024)) {
            bis.transferTo(bos);
        }

        Avatar avatar = getOrCreate(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(generateAvatarThumbnailImage(filePath));
        avatarRepository.save(avatar);
    }
}
