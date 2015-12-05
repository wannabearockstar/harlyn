package com.harlyn.service;

import com.harlyn.domain.problems.ProblemFile;
import com.harlyn.exception.InvalidFileException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
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

    public ProblemFile uploadProblemFile(MultipartFile file) {
        validateImage(file);
//        File uploadedFile = problemFilesFolder + file.getOriginalFilename();
        //todo implement
        return null;
    }
}
