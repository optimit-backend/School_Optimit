<<<<<<< HEAD
/*
=======
>>>>>>> 67ccb880a99b336fb6ab7fc42bff89f882b33348
package com.example.service;

import com.example.entity.Attachment;
import com.example.exception.FileUploadException;
import com.example.exception.OriginalFileNameNullException;
import com.example.exception.RecordNotFoundException;
import com.example.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.example.enums.Constants.*;


@Service
@RequiredArgsConstructor
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;

    @Value("${attach.upload.folder}")
    public String attachUploadFolder;

    public String getYearMonthDay() {
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        return year + "/" + month; // 2022/03
    }


    public String getExtension(String fileName) {
        if (fileName == null) {
            throw new OriginalFileNameNullException(FILE_NAME_NULL);
        }
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

    //    Bitta fileni sestamaga saqlab beradi
    public Attachment saveToSystem(MultipartFile file) {
        try {
            String pathFolder = getYearMonthDay();
            File folder = new File(attachUploadFolder + pathFolder);
            if (!folder.exists()) folder.mkdirs();
            String fileName = UUID.randomUUID().toString();
            String extension = getExtension(file.getOriginalFilename());

            byte[] bytes = file.getBytes();
            Path path = Paths.get(attachUploadFolder + pathFolder + "/" + fileName + "." + extension);
            Files.write(path, bytes).toFile();

            Attachment entity = new Attachment();
            entity.setNewName(fileName);
            entity.setOriginName(file.getOriginalFilename());
            entity.setType(extension);
            entity.setPath(pathFolder);
            entity.setSize(file.getSize());
            entity.setContentType(file.getContentType());

            return attachmentRepository.save(entity);
        } catch (IOException e) {
            throw new FileUploadException(FILE_COULD_NOT_UPLOADED);
        }

    }

    //    Ko'plab filelar kelsa saqlab beradi
    public List<Attachment> saveToSystemListFile(List<MultipartFile> fileList) {
        List<Attachment> attachments = new ArrayList<>();
        fileList.forEach((file) -> {
            attachments.add(saveToSystem(file));
        });
        return attachments;
    }

    //    Rasmni fileda joylashgan joyini linkini beradi
    public String getUrl(UUID imageId) {
        Attachment attachment = attachmentRepository.findById(imageId).orElseThrow(() -> new RecordNotFoundException(FILE_NOT_FOUND));
        return attachUploadFolder + attachment.getPath() + "/" + attachment.getNewName() + "." + attachment.getType();
    }

    public String getUrl(Attachment attachment) {
        if (attachment != null) {
            return attachUploadFolder + attachment.getPath() + "/" + attachment.getNewName() + "." + attachment.getType();
        } else {
            return attachUploadFolder + "avatar.png";
        }
    }

    public List<String> getUrlList(List<Attachment> attachmentList) {
        List<String> urlList = new ArrayList<>();
        if (!attachmentList.isEmpty()) {
            attachmentList.forEach(attachment ->{
                if (attachment != null) {
                    urlList.add(attachUploadFolder + attachment.getPath() + "/" + attachment.getNewName() + "." + attachment.getType());
                }
            });
            return urlList;
        }else {
            return null;
        }


    }

    // Rasmni byte qilib beradi
    public byte[] open(String fileName) {
        try {
            Attachment attachment = getAttachment(fileName);
            Path file = Paths.get(attachUploadFolder + attachment.getPath() + "/" + attachment.getNewName() + "." + attachment.getType());
            return Files.readAllBytes(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Attachment getAttachment(String fileName) {
        String newName = fileName.split("\\.")[0];
        return attachmentRepository.findByNewName(newName).orElseThrow(() -> new RecordNotFoundException(FILE_NOT_FOUND));
    }

    //    File systendan ochirib tashlaydi
    public boolean deleteNewName(Attachment attachment) {
        try {

            Path file = Paths.get(attachUploadFolder + attachment.getPath() + "/" + attachment.getNewName() + "." + attachment.getType());
            Files.delete(file);
//            attachmentRepository.deleteById(attachment.getId());
            return true;
        } catch (IOException e) {
            throw new RecordNotFoundException(FILE_NOT_FOUND);
        }
    }

    public boolean deleteListFilesByNewName(List<Attachment> attachmentList) {

        attachmentList.forEach(attachment -> {
            try {
                Path file = Paths.get(attachUploadFolder + attachment.getPath() + "/" + attachment.getNewName() + "." + attachment.getType());
                Files.delete(file);
                attachmentRepository.deleteById(attachment.getId());
            } catch (IOException e) {
                throw new RecordNotFoundException(FILE_NOT_FOUND);
            }
        });
        return true;
    }

}

<<<<<<< HEAD
*/
=======
>>>>>>> 67ccb880a99b336fb6ab7fc42bff89f882b33348
