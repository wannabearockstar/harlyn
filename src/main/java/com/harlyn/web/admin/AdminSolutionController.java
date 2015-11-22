package com.harlyn.web.admin;

import com.harlyn.service.SolutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by wannabe on 22.11.15.
 */
@Controller
@RequestMapping(value = "/admin/solution")
public class AdminSolutionController {
    @Autowired
    private SolutionService solutionService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String listSolutionPage(Model model) {
        model.addAttribute("solutions", solutionService.getAllSolutions());
        return "admin/solution/list";
    }
}
