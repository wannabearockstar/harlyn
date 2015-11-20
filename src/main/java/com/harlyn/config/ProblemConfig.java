package com.harlyn.config;

import com.harlyn.domain.problems.Problem;
import com.harlyn.domain.problems.handlers.FlagProblemHandler;
import com.harlyn.domain.problems.handlers.InfoEmailProblemHandler;
import com.harlyn.domain.problems.handlers.InfoWebProblemHandler;
import com.harlyn.domain.problems.handlers.ProblemHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wannabe on 20.11.15.
 */
@Configuration
public class ProblemConfig {
    @Bean
    public ProblemHandler flagProblemHandler() {
        return new FlagProblemHandler();
    }

    @Bean
    public ProblemHandler infoEmailProblemHandler() {
        return new InfoEmailProblemHandler();
    }

    @Bean
    public ProblemHandler infoWebProblemHandler() {
        return new InfoWebProblemHandler();
    }

    @Bean(name = "problemHandlers")
    public Map<Problem.ProblemType, ProblemHandler> handlers() {
        final Map<Problem.ProblemType, ProblemHandler> handlers = new HashMap<>();

        handlers.put(Problem.ProblemType.FLAG, flagProblemHandler());
        handlers.put(Problem.ProblemType.INFO_EMAIL, infoEmailProblemHandler());
        handlers.put(Problem.ProblemType.INFO_WEB, infoWebProblemHandler());

        return handlers;
    }
}
