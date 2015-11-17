package com.harlyn.service;

import com.harlyn.domain.ConfirmCode;
import com.harlyn.domain.User;
import com.harlyn.exception.InvalidConfirmCodeException;
import com.harlyn.repository.UserRepository;
import com.harlyn.repository.СonfirmCodeRepository;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by wannabe on 16.11.15.
 */
@Service
public class ConfirmCodeService {
    @Autowired
    private СonfirmCodeRepository confirmCodeRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private SimpleMailMessage templateConfirmCodeMessage;
    @Autowired
    private VelocityEngine velocityEngine;
    @Autowired
    private UserRepository userRepository;

    public ConfirmCode createConfirmCode(final User user) {
        ConfirmCode confirmCode = new ConfirmCode(UUID.randomUUID().toString(), user);
        confirmCodeRepository.saveAndFlush(confirmCode);
        sendConfirmCodeEmailLink(confirmCode);
        return confirmCode;
    }

    private void sendConfirmCodeEmailLink(final ConfirmCode confirmCode) {
        MimeMessagePreparator preparator = mimeMessage -> {
            Map<String, Object> model = new HashMap<>();
            model.put("confirmCode", confirmCode);
            String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/mail/confirm_code.vm", "UTF-8", model);
            mimeMessage.setContent(text, "text/html; charset=utf-8");
            mimeMessage.setHeader("Content-Type", "text/html; charset=UTF-8");
            mimeMessage.setSubject(templateConfirmCodeMessage.getSubject(), "UTF-8");

            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
            message.setTo(confirmCode.getUser().getEmail());
            message.setFrom(templateConfirmCodeMessage.getFrom());
        };
        mailSender.send(preparator);
    }

    @Transactional
    public void confirmUserByCode(String code) {
        ConfirmCode confirmCode = confirmCodeRepository.findOneByCode(code);
        if (confirmCode == null) {
            throw new InvalidConfirmCodeException();
        }
        userRepository.enableUserById(confirmCode.getUser().getId());
        confirmCodeRepository.delete(confirmCode);
        confirmCodeRepository.flush();
    }

    public ConfirmCodeService setConfirmCodeRepository(СonfirmCodeRepository confirmCodeRepository) {
        this.confirmCodeRepository = confirmCodeRepository;
        return this;
    }

    public ConfirmCodeService setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        return this;
    }

    public ConfirmCodeService setTemplateConfirmCodeMessage(SimpleMailMessage templateConfirmCodeMessage) {
        this.templateConfirmCodeMessage = templateConfirmCodeMessage;
        return this;
    }

    public ConfirmCodeService setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
        return this;
    }

    public ConfirmCodeService setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
        return this;
    }
}
