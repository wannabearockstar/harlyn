package com.harlyn.web.admin;

import com.harlyn.domain.problems.Category;
import com.harlyn.exception.CategoryNotFoundException;
import com.harlyn.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by wannabe on 08.12.15.
 */
@Controller
@RequestMapping(value = "/admin/category")
public class AdminCategoryController {

	@Autowired
	private CategoryService categoryService;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String newCategoryPage(Model model) {
		model.addAttribute("category", new Category());
		return "admin/category/new";
	}

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String newCategoryAction(@Valid Category category) {
		return "redirect:/admin/category/" + categoryService.createCategory(category);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String editCategoryPage(@PathVariable(value = "id") Long id, Model model) {
		Category category = categoryService.findById(id);
		if (category == null) {
			throw new CategoryNotFoundException(id);
		}
		model.addAttribute("category", category);
		return "admin/category/edit";
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public String editCategoryAction(@PathVariable(value = "id") Long id,
																	 @Valid Category categoryData,
																	 Model model
	) {
		Category category = categoryService.findById(id);
		if (category == null) {
			throw new CategoryNotFoundException(id);
		}
		categoryService.updateCategory(category, categoryData);
		return "redirect:/admin/category/" + category.getId();
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String listCategoriesPage(Model model) {
		model.addAttribute("categories", categoryService.findAll());
		return "admin/category/list";
	}
}
