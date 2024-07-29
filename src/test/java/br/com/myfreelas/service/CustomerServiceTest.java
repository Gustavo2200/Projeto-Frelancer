package br.com.myfreelas.service;

import br.com.myfreelas.dao.customer.impl.CustomerDaoImpl;
import br.com.myfreelas.dao.project.impl.ProjectDaoImpl;
import br.com.myfreelas.enums.StatusProject;
import br.com.myfreelas.err.exceptions.FreelasException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @InjectMocks
    @Autowired
    CustomerService service;

    @Mock
    CustomerDaoImpl customerDao;

    @Mock
    ProjectDaoImpl projectDao;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Sucesso ao contatar o banco")
    void viewProposalsByProjectIdTestSuccess(){
        Long idProject = 26L;
        Long idCustomer = 13L;

        Mockito.when(projectDao.checkProjectExists(idProject)).thenReturn(true);
        Mockito.when(projectDao.customerIdByProjectId(idProject)).thenReturn(13L);

        service.viewProposalsByProjectId(idProject, idCustomer);

        Mockito.verify(customerDao, Mockito.times(1)).viewProposalsByProjectId(idProject);
        Mockito.verify(projectDao, Mockito.times(1)).checkProjectExists(idProject);
        Mockito.verify(projectDao, Mockito.times(1)).customerIdByProjectId(idProject);
    }

    @Test
    @DisplayName("Caso projeto não encontrado lanca a excecao")
    void viewProposalsByProjectIdTestErrorCase1(){
        Long idProject = 26L;
        Long idCustomer = 13L;

        Mockito.when(projectDao.checkProjectExists(idProject)).thenReturn(false);

        Exception e = Assertions.assertThrows(FreelasException.class, () ->{
            service.viewProposalsByProjectId(idProject, idCustomer);
        });

        Assertions.assertEquals("Projeto nao encontrado", e.getMessage());
    }

    @Test
    @DisplayName("Caso o usuario logado não seja dono do projeto lanca uma excecao")
    void viewProposalsByProjectIdTestErrorCase2(){
        Long idProject = 26L;
        Long idCustomer = 12L;

        Mockito.when(projectDao.checkProjectExists(idProject)).thenReturn(true);
        Mockito.when(projectDao.customerIdByProjectId(idProject)).thenReturn(13L);

        Exception e = Assertions.assertThrows(FreelasException.class, () ->{
            service.viewProposalsByProjectId(idProject, idCustomer);
        });

        Assertions.assertEquals("Voce so pode gerenciar propostas do seu projeto", e.getMessage());
    }

    @Test
    @DisplayName("Recusa uma proposta com sucesso")
    void rejectProposalTestSuccess(){
        Long idProposal = 1L;
        Long idCustomer = 13L;
        Long idProject = 26L;

        Mockito.when(customerDao.checkProposalExists(idProposal)).thenReturn(true);
        Mockito.when(customerDao.idProjectByIdProposal(idProposal)).thenReturn(26L);
        Mockito.when(projectDao.checkProjectExists(idProject)).thenReturn(true);
        Mockito.when(projectDao.customerIdByProjectId(idProject)).thenReturn(13L);

        service.rejectProposal(idProposal, idCustomer);

        Mockito.verify(customerDao, Mockito.times(1)).rejectProposal(idProposal);

        Mockito.verify(customerDao, Mockito.times(1)).checkProposalExists(idProposal);
        Mockito.verify(customerDao, Mockito.times(1)).idProjectByIdProposal(idProposal);
        Mockito.verify(projectDao, Mockito.times(1)).checkProjectExists(idProject);
        Mockito.verify(projectDao, Mockito.times(1)).customerIdByProjectId(idProject);

    }

    @Test
    @DisplayName("Caso proposta não existir, lança uma excecao")
    void rejectProposalTestErrorCase1(){
        Long idProposal = 1L;
        Long idCustomer = 13L;

        Mockito.when(customerDao.checkProposalExists(idProposal)).thenReturn(false);

        Exception e = Assertions.assertThrows(FreelasException.class, () ->{
            service.rejectProposal(idProposal, idCustomer);
        });

        Assertions.assertEquals("Proposta nao encontrada", e.getMessage());
    }
    @Test
    @DisplayName("Aceita uma proposata com sucesso")
    void acceptProposalSuccess(){

        Long idProposal = 12L;
        Long idCustomer = 13L;

        Mockito.when(customerDao.checkProposalExists(any())).thenReturn(true);
        Mockito.when(customerDao.idProjectByIdProposal(any())).thenReturn(14L);
        Mockito.when(projectDao.checkProjectExists(any())).thenReturn(true);
        Mockito.when(projectDao.customerIdByProjectId(any())).thenReturn(idCustomer);

        service.acceptProposal(idProposal, idCustomer);

        Mockito.verify(customerDao, Mockito.times(1)).acceptProposal(any());
    }
    @Test
    @DisplayName("Proposta não encontrada")
    void acceptProposalErrorCase1(){

        Long idProposal = 12L;
        Long idCustomer = 13L;

        Mockito.when(customerDao.checkProposalExists(any())).thenReturn(false);

        Exception e = Assertions.assertThrows(FreelasException.class, () ->{
            service.acceptProposal(idProposal, idCustomer);
        });

        Assertions.assertEquals("Proposta nao encontrada", e.getMessage());
    }

    @Test
    @DisplayName("Teste depositar saldo")
    void depositBalance(){
        service.depositBalance(any(), any());

        Mockito.verify(customerDao, Mockito.times(1)).depositBalance(any(), any());
    }

    @Test
    @DisplayName("Completa um projeto com sucesso")
    void completeProjectSuccess(){
        Long idProject = 26L;
        Long idCustomer = 13L;
        BigDecimal valueProject = new BigDecimal(500);

        Mockito.when(projectDao.checkProjectExists(idProject)).thenReturn(true);
        Mockito.when(projectDao.customerIdByProjectId(idProject)).thenReturn(13L);
        Mockito.when(projectDao.checkStatusProject(idProject)).thenReturn(StatusProject.IN_PROGRESS);
        Mockito.when(projectDao.priceByProjectId(idProject)).thenReturn(new BigDecimal(500));

        Mockito.when(customerDao.checkBalanceCustomer(idCustomer, valueProject)).thenReturn(true);

        service.completeProject(idProject, idCustomer);

        Mockito.verify(customerDao, Mockito.times(1)).completeProject(idProject);

        Mockito.verify(projectDao, Mockito.times(1)).checkProjectExists(idProject);
        Mockito.verify(projectDao, Mockito.times(1)).customerIdByProjectId(idProject);
        Mockito.verify(projectDao, Mockito.times(1)).checkStatusProject(idProject);
        Mockito.verify(projectDao, Mockito.times(1)).priceByProjectId(idProject);

        Mockito.verify(customerDao, Mockito.times(1)).checkBalanceCustomer(idCustomer, valueProject);
    }

    @Test
    @DisplayName("Projeto nao existe")
    void completeProjectErrorCase1() {
        Long idProject = 26L;
        Long idCustomer = 13L;

        Mockito.when(projectDao.checkProjectExists(idProject)).thenReturn(false);

        Exception e = Assertions.assertThrows(FreelasException.class, () ->{
            service.completeProject(idProject, idCustomer);
        });
        Assertions.assertEquals("Projeto nao encontrado", e.getMessage());
        Mockito.verify(customerDao, Mockito.times(0)).completeProject(idProject);
    }

    @Test
    @DisplayName("Cliente nao e dono do projeto")
    void completeProjectErrorCase2() {
        Long idProject = 26L;
        Long idCustomer = 13L;

        Mockito.when(projectDao.checkProjectExists(idProject)).thenReturn(true);
        Mockito.when(projectDao.customerIdByProjectId(idProject)).thenReturn(12L);

        Exception e = Assertions.assertThrows(FreelasException.class, () ->{
            service.completeProject(idProject, idCustomer);
        });
        Assertions.assertEquals("Voce so pode gerenciar propostas do seu projeto", e.getMessage());
        Mockito.verify(customerDao, Mockito.times(0)).completeProject(idProject);
    }

    @Test
    @DisplayName("Projeto nao esta em progresso")
    void completeProjectErrorCase3() {
        Long idProject = 26L;
        Long idCustomer = 13L;

        Mockito.when(projectDao.checkProjectExists(idProject)).thenReturn(true);
        Mockito.when(projectDao.customerIdByProjectId(idProject)).thenReturn(13L);
        Mockito.when(projectDao.checkStatusProject(idProject)).thenReturn(StatusProject.OPEN);

        Exception e = Assertions.assertThrows(FreelasException.class, () ->{
            service.completeProject(idProject, idCustomer);
        });
        Assertions.assertEquals("Voce so pode concluir projetos que estejam em andamento", e.getMessage());
        Mockito.verify(customerDao, Mockito.times(0)).completeProject(idProject);
    }

    @Test
    @DisplayName("Saldo insuficiente do cliente")
    void completeProjectErrorCase4() {
        Long idProject = 26L;
        Long idCustomer = 13L;
        BigDecimal valueProject = new BigDecimal(1000);

        Mockito.when(projectDao.checkProjectExists(idProject)).thenReturn(true);
        Mockito.when(projectDao.customerIdByProjectId(idProject)).thenReturn(13L);
        Mockito.when(projectDao.checkStatusProject(idProject)).thenReturn(StatusProject.IN_PROGRESS);
        Mockito.when(projectDao.priceByProjectId(idProject)).thenReturn(new BigDecimal(1000));

        Mockito.when(customerDao.checkBalanceCustomer(idCustomer, valueProject)).thenReturn(false);

        Exception e = Assertions.assertThrows(FreelasException.class, () ->{
            service.completeProject(idProject, idCustomer);
        });
        Assertions.assertEquals("Seu saldo é insuficiente", e.getMessage());
        Mockito.verify(customerDao, Mockito.times(0)).completeProject(idProject);
    }
}
