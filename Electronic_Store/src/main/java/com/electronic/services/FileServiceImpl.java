package com.electronic.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.electronic.exception.BadApiRequestException;

@Service
public class FileServiceImpl implements FileService {

	private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	@Override
	public String uploadFile(MultipartFile file, String path) throws IOException {

		String originalFilename = file.getOriginalFilename();
		logger.info("Filename: {}", originalFilename);

		String fileName = UUID.randomUUID().toString();

		String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

		String fileNameWithExtension = fileName + extension;

		// String fullPathWithFileName = path + File.separator + fileNameWithExtension;

		String fullPathWithFileName = path + fileNameWithExtension;

		if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpeg")
				|| extension.equalsIgnoreCase(".jpg")) {

			// file save
			File folder = new File(path);
			if (!folder.exists()) {
				folder.mkdirs();
			}

			// upload

			Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));

			return fileNameWithExtension;

		} else {
			throw new BadApiRequestException("File with this " + extension + " not allowed ");
		}

	}

	@Override
	public InputStream getFile(String path, String name) throws FileNotFoundException {

		String fullPath = path + File.separator + name;

		InputStream ios = new FileInputStream(fullPath);
		return ios;

	}

}
