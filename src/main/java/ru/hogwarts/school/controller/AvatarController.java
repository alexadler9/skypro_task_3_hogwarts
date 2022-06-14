package ru.hogwarts.school.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/avatar")
public class AvatarController {
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @GetMapping("/{studentId}/db")
    public ResponseEntity<byte[]> getAvatar(@PathVariable Long studentId) {
        Avatar avatar = avatarService.get(studentId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping("/{studentId}/file")
    public void getAvatar(@PathVariable Long studentId, HttpServletResponse response) throws IOException {
        Avatar avatar = avatarService.get(studentId);
        Path filePath = Path.of(avatar.getFilePath());

        try (InputStream is = Files.newInputStream(filePath);
             OutputStream os = response.getOutputStream()) {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int)avatar.getFileSize());
            is.transferTo(os);
        }
    }

    @PostMapping(value = "/{studentId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> uploadAvatar(@PathVariable Long studentId, @RequestParam MultipartFile avatar) throws IOException {
        avatarService.upload(studentId, avatar);
        return ResponseEntity.ok().build();
    }
}
