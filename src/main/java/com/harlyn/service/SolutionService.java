package com.harlyn.service;

import com.harlyn.domain.problems.Solution;
import com.harlyn.repository.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wannabe on 21.11.15.
 */
@Service
public class SolutionService {
    @Autowired
    private SolutionRepository solutionRepository;

    public Solution getById(Long id) {
        return solutionRepository.findOne(id);
    }
}
