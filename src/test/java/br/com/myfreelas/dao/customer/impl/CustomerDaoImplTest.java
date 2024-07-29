package br.com.myfreelas.dao.customer.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(CustomerDaoImpl.class)
class CustomerDaoImplTest {

    @Autowired
    private DataSource dataSource;

    @Autowired
    CustomerDaoImpl customerDao;

    @Test
    void contextLoads() {
        assertNotNull(dataSource);
    }

    @Test
    @DisplayName("Retorna as propostas do projeto do banco caso encontradas")
    void viewProposalsByProjectIdSuccess() {
        Long projectId = 26L;
        List<?> list = customerDao.viewProposalsByProjectId(projectId);
        assertThat(list.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("Retorna uma lista vazia quando nenhuma proposta encontrada")
    void viewProposalsByProjectIdCaseError() {
        Long projectId = 25L;
        List<?> list = customerDao.viewProposalsByProjectId(projectId);
        assertThat(list.isEmpty()).isTrue();
    }
}
