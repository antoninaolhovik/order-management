package com.amakedon.om.repository;

import com.amakedon.om.data.model.Category;
import com.amakedon.om.data.repository.es.EsOrderRepository;
import com.amakedon.om.data.repository.jpa.CategoryRepository;
import com.amakedon.om.service.OrderServiceImpl;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.*;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.*;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CategoryRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @MockBean
    private EsOrderRepository esOrderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    public void whenFindByName_thenReturnCategory() {
        assertThat(entityManager).isNotNull();
        // given
        Category testCategory = new Category();
        testCategory.setName("testCategory");
        entityManager.persist(testCategory);
        entityManager.flush();

        // when
        Category found = categoryRepository.findByName(testCategory.getName());

        // then
        assertThat(found.getName())
                .isEqualTo(testCategory.getName());
    }
}
