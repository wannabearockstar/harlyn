package com.harlyn.domain.problems.handlers;

import com.harlyn.domain.User;
import com.harlyn.domain.problems.Problem;
import com.harlyn.domain.problems.SubmitData;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wannabe on 20.11.15.
 */

/**
 * Email implementation of {@link ProblemHandler}
 * Do not providing final answer about solution status, just pass solution query param by email
 */
public class InfoEmailProblemHandler implements ProblemHandler {
    private JavaMailSender mailSender;
    private SimpleMailMessage templateMessage;
    private VelocityEngine velocityEngine;

    public InfoEmailProblemHandler(JavaMailSender mailSender, SimpleMailMessage templateMessage, VelocityEngine velocityEngine) {
        this.mailSender = mailSender;
        this.templateMessage = templateMessage;
        this.velocityEngine = velocityEngine;
    }

    @Override
    public boolean isManual() {
        return true;
    }

    @Override
    public boolean checkSolution(Problem problem, SubmitData solution, User solver) {
        MimeMessagePreparator preparator = mimeMessage -> {
            Map<String, Object> model = new HashMap<>();
            model.put("solution", solution);
            model.put("problem", problem);
            String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/mail/main_query_solution.vm", "UTF-8", model);
            mimeMessage.setContent(text, "text/html; charset=utf-8");
            mimeMessage.setHeader("Content-Type", "text/html; charset=UTF-8");
            mimeMessage.setSubject(getMailSubject(problem, solution, solver), "UTF-8");

            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
            message.setTo(templateMessage.getTo());
            message.setFrom(templateMessage.getFrom());
        };
        mailSender.send(preparator);
        return false;
    }

    protected String getMailSubject(Problem problem, SubmitData solution, User solver) {
        return String.format("Solution of problem #%d: %s. %s, team %s",
                problem.getId(),
                problem.getName(),
                solver.getUsername(),
                solver.getTeam().getName()
                );
    }
}
