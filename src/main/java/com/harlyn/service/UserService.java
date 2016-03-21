package com.harlyn.service;

import com.harlyn.domain.User;
import com.harlyn.exception.NonUniqueUserDataException;
import com.harlyn.repository.RoleRepository;
import com.harlyn.repository.UserRepository;
import com.harlyn.security.SessionIdentifierGenerator;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by wannabe on 15.11.15.
 */
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private SessionIdentifierGenerator sessionIdentifierGenerator;
	@Autowired
	private JavaMailSender mailSender;
	@Autowired
	private SimpleMailMessage templateConfirmCodeMessage;
	@Autowired
	private VelocityEngine velocityEngine;
	@Autowired
	private String currentHost;

	public User findUserByEmail(String email) {
		return userRepository.findUserByEmail(email);
	}

	public User createUser(User user, boolean isAdmin) {
		Set<ConstraintViolation<User>> errors = Validation.buildDefaultValidatorFactory().getValidator().validate(user);
		if (!errors.isEmpty()) {
			throw new ConstraintViolationException(errors);
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		if (isAdmin) {
			user.getRoles().add(roleRepository.findOneByName("ROLE_ADMIN"));
		}
		return userRepository.saveAndFlush(user);
	}

	public void validateUniqueData(final User user) {
		if (userRepository.userExistByEmail(user.getEmail())) {
			throw new NonUniqueUserDataException("Email already taken", user);
		}
		if (userRepository.userExistByUsername(user.getUsername())) {
			throw new NonUniqueUserDataException("Username already taken", user);
		}

	}

	public UserService setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
		return this;
	}

	public UserService setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
		return this;
	}

	public UserService setRoleRepository(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
		return this;
	}

	public User getById(Long id) {
		return userRepository.findOne(id);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User getByEmail(String email) {
		return userRepository.findOneByEmail(email);
	}

	@Transactional
	public void sendResetLink(User user) {
		String resetToken = sessionIdentifierGenerator.nextSessionId();
		userRepository.updateResetToken(user.getId(), resetToken);
		sendResetLink(user, resetToken);
	}

	private void sendResetLink(User user, String resetToken) {
		MimeMessagePreparator preparator = mimeMessage -> {
			Map<String, Object> model = new HashMap<>();
			model.put("resetToken", resetToken);
			model.put("currentHost", currentHost);
			String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, "templates/mail/reset_password_link.vm", "UTF-8", model);
			mimeMessage.setContent(text, "text/html; charset=utf-8");
			mimeMessage.setHeader("Content-Type", "text/html; charset=UTF-8");
			mimeMessage.setSubject(templateConfirmCodeMessage.getSubject(), "UTF-8");

			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
			message.setTo(user.getEmail());
			message.setFrom(templateConfirmCodeMessage.getFrom());
		};
		mailSender.send(preparator);
	}

	public User getByResetToken(String token) {
		return userRepository.findOneByResetToken(token);
	}

	@Transactional
	public void resetPassword(User user, String password) {
		userRepository.updatePassword(user.getId(), passwordEncoder.encode(password));
	}
}
