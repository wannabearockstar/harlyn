package com.harlyn.web.admin;

import com.harlyn.domain.User;
import com.harlyn.event.UserChangedEvent;
import com.harlyn.exception.UserNotFoundException;
import com.harlyn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by wannabe on 22.11.15.
 */
@Controller
@RequestMapping(value = "/admin/user")
public class AdminUserController {

	@Autowired
	private UserService userService;
	@Autowired
	private ApplicationEventPublisher eventPublisher;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String listUsersPage(Model model) {
		model.addAttribute("users", userService.getAllUsers());
		return "admin/user/list";
	}
	
	@RequestMapping(value = "/{id}/ban/chat", method = RequestMethod.POST)
	public String banUserInChat(@PathVariable("id") Long id) {
		User user = userService.getById(id);
		if (user == null) {
			throw new UserNotFoundException();
		}
		userService.banUser(user);
		eventPublisher.publishEvent(new UserChangedEvent(this, user));
		return "redirect:/admin/user/";
	}
}
