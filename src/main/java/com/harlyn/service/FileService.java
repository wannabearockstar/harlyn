package com.harlyn.service;

import com.harlyn.domain.problems.Problem;
import com.harlyn.domain.problems.ProblemFile;
import com.harlyn.exception.InvalidFileException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * Created by wannabe on 05.12.15.
 */
@Service
public class FileService {
    @Resource
    private Set<String> acceptableTypes;
    @Autowired
    private String problemFilesFolder;

    public void validateImage(MultipartFile file) {
        if (!acceptableTypes.contains(file.getContentType())) {
            throw new InvalidFileException();
        }
    }

    public ProblemFile uploadProblemFile(MultipartFile file, final Problem problem, String name) throws IOException {
        validateImage(file);
        String randomFilename = RandomStringUtils.randomAlphanumeric(8) + "_" + file.getOriginalFilename();
        File uploadedFile = new File(problemFilesFolder + randomFilename);
        FileUtils.writeByteArrayToFile(uploadedFile, file.getBytes());
        return new ProblemFile(randomFilename, problem, name, file.getContentType(), file.getSize());
    }


    public File getFileForProblem(Problem problem) {
        return new File(
                problemFilesFolder + problem.getFile().getPath()
        );
    }

}
