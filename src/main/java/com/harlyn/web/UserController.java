package com.harlyn.web;

import com.harlyn.domain.User;
import com.harlyn.event.UserChangedEvent;
import com.harlyn.exception.UserNotFoundException;
import com.harlyn.service.TeamService;
import com.harlyn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by wannabe on 16.11.15.
 */
@Controller
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private TeamService teamService;
	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@RequestMapping(value = "/me", method = RequestMethod.GET)
	public String meAction(Model model) {
		return "user/me";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String userAction(Model model, @PathVariable(value = "id") Long id) {
		User user = userService.getById(id);
		if (user == null) {
			throw new UserNotFoundException();
		}
		model.addAttribute(user);
		return "user/show";
	}

	@RequestMapping(value = "/invite", method = RequestMethod.POST)
	public String inviteAction(@RequestParam(value = "email") String email, Model model) {
		User user = userService.getByEmail(email);
		User me = (User) model.asMap().get("me");

		if (user == null || user.getTeam() != null || me.getTeam() == null) {
			return "redirect:/users/me?invalid_user";
		}
		teamService.sendInvite(me, user);
		eventPublisher.publishEvent(new UserChangedEvent(this, user));
		return "redirect:/users/me?user_invited";
	}

	@RequestMapping(value = "/reset/ask", method = RequestMethod.GET)
	public String sendResetLink() {
		return "security/reset_ask";
	}

	@RequestMapping(value = "/reset/ask", method = RequestMethod.POST)
	public String sendResetLink(@RequestParam(value = "email") String email) {
		User user = userService.getByEmail(email);
		if (user != null) {
			userService.sendResetLink(user);
		}
		return "redirect:/login/form?reset";
	}

	@RequestMapping(value = "/reset", method = RequestMethod.GET)
	public String resetPasswordPage(@RequestParam(value = "token") String token,
																	Model model
	) {
		User user = userService.getByResetToken(token);
		if (user == null) {
			return "redirect:/login/form";
		}
		model.addAttribute("token", token);
		return "security/reset";
	}

	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public String resetPassword(
		@RequestParam(value = "token") String token,
		@RequestParam(value = "password") String password,
		@RequestParam(value = "password_again") String passwordAgain
	) {
		User user = userService.getByResetToken(token);
		if (user == null) {
			return "redirect:/login/form";
		}
		if (!password.equals(passwordAgain)) {
			return "redirect:/users/reset?password_mistmatch=1&token=" + token;
		}
		userService.resetPassword(user, password);
		return "redirect:/login/form?password_updated";
	}
}
