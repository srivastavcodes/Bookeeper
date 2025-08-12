package com.project.bookbackend.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
public class FileUtils {

	public static byte[] readCoverFromLocation(String reqUrl) {
		if (StringUtils.isBlank(reqUrl)) {
			return null;
		}
		try {
			Path filePath = new File(reqUrl).toPath();
			return Files.readAllBytes(filePath);
		} catch (IOException ioExp) {
			log.warn("No file found in path::{}", reqUrl);
		}
		return null;
	}
}
