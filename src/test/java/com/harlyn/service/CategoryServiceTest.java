package com.harlyn.service;

import com.harlyn.HarlynApplication;
import com.harlyn.domain.problems.Category;
import com.harlyn.repository.CategoryRepository;
import org.flywaydb.core.Flyway;
import org.flywaydb.test.junit.FlywayTestExecutionListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by wannabe on 08.12.15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = HarlynApplication.class)
@TransactionConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, FlywayTestExecutionListener.class})
@WebAppConfiguration
@ActiveProfiles({"test"})
public class CategoryServiceTest {
    @Autowired
    private Flyway flyway;
    @Autowired
    private CategoryRepository categoryRepository;
    private CategoryService categoryService;

    @Before
    public void setUp() throws Exception {
        flyway.clean();
        flyway.migrate();
        categoryService = new CategoryService()
                .setCategoryRepository(categoryRepository);
    }

    @After
    public void tearDown() throws Exception {
        flyway.clean();
    }

    @Test
    public void testFindById() throws Exception {
        Category category = categoryRepository.saveAndFlush(new Category("name"));

        assertEquals("name", categoryService.findById(category.getId()).getName());
    }

    @Test
    public void testCreateCategory() throws Exception {
        Long categoryId = categoryService.createCategory(new Category("name"));

        assertEquals("name", categoryRepository.findOne(categoryId).getName());
    }

    @Test
    public void testUpdateCategory() throws Exception {
        Category category = categoryRepository.saveAndFlush(new Category("name"));
        Category categoryData = new Category("update_name");

        categoryService.updateCategory(category, categoryData);

        assertEquals("update_name", categoryRepository.findOne(category.getId()).getName());
    }

    @Test
    public void testFindAll() throws Exception {
        Category category = categoryRepository.saveAndFlush(new Category("name"));
        Category categoryData = categoryRepository.saveAndFlush(new Category("name1"));

        List<Category> categories = categoryService.findAll();

        assertEquals(2, categories.size());
        assertEquals("name1", categories.get(0).getName());
        assertEquals("name", categories.get(1).getName());
    }
}