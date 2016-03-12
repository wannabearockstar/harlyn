package com.harlyn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by wannabe on 05.12.15.
 */
@Configuration
public class FileConfig {

	@Autowired
	private ServletContext servletContext;

	@Bean(name = "acceptableTypes")
	public Set<String> acceptableTypes() {
		Set<String> acceptableTypes = new HashSet<>();
		acceptableTypes.add(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		acceptableTypes.add(MediaType.IMAGE_JPEG_VALUE);
		acceptableTypes.add(MediaType.IMAGE_GIF_VALUE);
		acceptableTypes.add(MediaType.IMAGE_PNG_VALUE);
		acceptableTypes.add(MediaType.TEXT_PLAIN_VALUE);
		acceptableTypes.add("application/zip");
		return acceptableTypes;
	}

	/**
	 * @return Folder for problems files, ends with '/'
	 */
	@Bean(name = "problemFilesFolder")
	public String problemFilesFolder() {
		return "files"
			+ File.separator
			+ "problems"
			+ File.separator;
	}

	/**
	 * @return Folder for problems files, ends with '/'
	 */
	@Bean(name = "solutionFilesFolder")
	public String solutionFilesFolder() {
		return "files"
			+ File.separator
			+ "solutions"
			+ File.separator;
	}
}
