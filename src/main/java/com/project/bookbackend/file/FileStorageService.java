package com.project.bookbackend.file;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.System.currentTimeMillis;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileStorageService {

	@Value("${application.file.upload.photo-output-path}")
	private String fileUploadPath;

	public String saveBookCover(@NonNull Integer userId, @NonNull MultipartFile bookCover) {
		final String fileUploadSubPath = "users" + File.separator + userId;
		return uploadFile(bookCover, fileUploadSubPath);
	}

	private String uploadFile(@NonNull MultipartFile bookCover, @NonNull String fileUploadSubPath) {
		String finalUploadPath = fileUploadPath + File.separator + fileUploadSubPath;
		File targetFolder = new File(finalUploadPath);

		if (!targetFolder.exists()) {
			//mkdirs() creates folder and all it's subfolders if necessary. mkdirs() is recursive, mkdir() is not.
			boolean folderCreated = targetFolder.mkdirs();

			if (!folderCreated) {
				log.warn("Failed to create target folder.");
				return null;
			}
		}
		final String fileExtension = getFileExtension(bookCover.getOriginalFilename());
		// ./uploads/users/1/238949223.jpg -> example format.

		String targetFilePath = finalUploadPath + File.separator + currentTimeMillis() + "." + fileExtension;
		//noinspection JvmTaintAnalysis
		Path targetPath = Paths.get(targetFilePath);

		try {
			Files.write(targetPath, bookCover.getBytes());
			log.info("File saved to::{}", targetFilePath);
			return targetFilePath;
		} catch (IOException ioExp) {
			log.error("File was not saved::", ioExp);
		}
		return null;
	}

	private String getFileExtension(String fileName) {
		if (fileName == null || fileName.isEmpty()) {
			return "";
		}
		int lastDotIndex = fileName.lastIndexOf(".");
		if (lastDotIndex == -1) {
			return "";
		}
		return fileName.substring(lastDotIndex + 1).toLowerCase();
	}
}







