package br.com.myfreelas.service;

import br.com.myfreelas.dao.customer.impl.CustomerDaoImpl;
import br.com.myfreelas.dao.freelancer.impl.FreelancerDaoImpl;
import br.com.myfreelas.dao.project.impl.ProjectDaoImpl;
import br.com.myfreelas.dto.proposal.ProposalDtoRequest;
import br.com.myfreelas.enums.StatusProject;
import br.com.myfreelas.err.exceptions.FreelasException;
import br.com.myfreelas.model.Proposal;
import br.com.myfreelas.model.Skill;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FreelancerServiceTest {

    @Mock
    FreelancerDaoImpl freelancerDao;

    @Mock
    CustomerDaoImpl customerDao;

    @Mock
    ProjectDaoImpl projectDao;

    @InjectMocks
    @Autowired
    FreelancerService freelancerService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Salva skill com sucesso")
    void saveSkillSuccess() {
        List<Skill> skills = Arrays.asList(
                new Skill("Java"),
                new Skill("Angular")
        );
        Long javaId = 1L;
        Long angularId = 2L;

        Long idFreelancer = 12L;

        when(freelancerDao.idBySkillName(skills.get(0).getSkill())).thenReturn(1L);
        when(freelancerDao.idBySkillName(skills.get(1).getSkill())).thenReturn(2L);
        when(freelancerDao.checkSkillIsAlreadySaved(javaId, idFreelancer)).thenReturn(false);
        when(freelancerDao.checkSkillIsAlreadySaved(angularId, idFreelancer)).thenReturn(false);

        freelancerService.saveSkill(idFreelancer, skills);

        verify(freelancerDao, times(1)).saveSkill(Arrays.asList(javaId, angularId),idFreelancer);
        verify(freelancerDao, times(1)).idBySkillName(skills.get(0).getSkill());
        verify(freelancerDao, times(1)).idBySkillName(skills.get(1).getSkill());
        verify(freelancerDao, times(1)).checkSkillIsAlreadySaved(javaId, idFreelancer);
        verify(freelancerDao, times(1)).checkSkillIsAlreadySaved(angularId, idFreelancer);
    }

    @Test
    @DisplayName("Skill não existe no banco de dados")
    void saveSkillErrorCase1() {
        List<Skill> skills = Arrays.asList(
                new Skill("Java"),
                new Skill("Angular")
        );
        Long idFreelancer = 12L;

        when(freelancerDao.idBySkillName(skills.get(0).getSkill())).thenReturn(null);

        Exception e = Assertions.assertThrows(FreelasException.class, () ->{
            freelancerService.saveSkill(idFreelancer, skills);
        });

        Assertions.assertEquals("Skill inexistente", e.getMessage());

        verify(freelancerDao, times(0)).saveSkill(Arrays.asList(any(), any()),idFreelancer);
    }

    @Test
    @DisplayName("Skill ja existe para o usuario")
    void saveSkillErrorCase2() {
        List<Skill> skills = Arrays.asList(
                new Skill("Java"),
                new Skill("Angular")
        );
        Long javaId = 1L;

        Long idFreelancer = 12L;

        when(freelancerDao.idBySkillName(skills.get(0).getSkill())).thenReturn(1L);
        when(freelancerDao.checkSkillIsAlreadySaved(javaId, idFreelancer)).thenReturn(true);
        Exception e = Assertions.assertThrows(FreelasException.class, () ->{
            freelancerService.saveSkill(idFreelancer, skills);
        });

        Assertions.assertEquals("Skill ja foi adicionada", e.getMessage());

        verify(freelancerDao, times(0)).saveSkill(Arrays.asList(any(), any()),idFreelancer);
    }

    @Test
    @DisplayName("Deleta skills do usuario com sucesso")
    void deleteSkillSuccess() {
        Long idFreelancer = 13L;
        Long javaId = 1L;
        Long angularId = 2L;
        List<Skill> skills = Arrays.asList(
                new Skill("Java"),
                new Skill("Angular")
        );

        when(freelancerDao.getSkillsByFreelancerId(idFreelancer)).thenReturn(Arrays.asList("Java", "Angular"));
        when(freelancerDao.idBySkillName(skills.get(0).getSkill())).thenReturn(1L);
        when(freelancerDao.idBySkillName(skills.get(1).getSkill())).thenReturn(2L);
        when(freelancerDao.checkSkillIsAlreadySaved(javaId, idFreelancer)).thenReturn(true);
        when(freelancerDao.checkSkillIsAlreadySaved(angularId, idFreelancer)).thenReturn(true);

        freelancerService.deleteSkill(idFreelancer, skills);

        verify(freelancerDao,times(1)).getSkillsByFreelancerId(idFreelancer);
        verify(freelancerDao, times(2)).idBySkillName(any());
        verify(freelancerDao, times(1)).checkSkillIsAlreadySaved(javaId, idFreelancer);
        verify(freelancerDao, times(1)).checkSkillIsAlreadySaved(angularId, idFreelancer);

        verify(freelancerDao, times(1)).deleteSkill(Arrays.asList(javaId,angularId), idFreelancer);
    }

    @Test
    @DisplayName("Usuario não tem nenhuma skill que quer deletar")
    void deleteSkillErrorCase1() {
        Long idFreelancer = 13L;
        Long javaId = 1L;
        Long angularId = 2L;
        List<Skill> skills = Arrays.asList(
                new Skill("Java"),
                new Skill("Angular")
        );

        when(freelancerDao.getSkillsByFreelancerId(idFreelancer)).thenReturn(Collections.emptyList());

        Exception e = Assertions.assertThrows(FreelasException.class, () ->{
            freelancerService.deleteSkill(idFreelancer, skills);
        });

        Assertions.assertEquals("Skill não encontrada para esse freelancer", e.getMessage());

        verify(freelancerDao, times(0)).deleteSkill(Arrays.asList(javaId,angularId), idFreelancer);
    }

    @Test
    @DisplayName("Skill referida nao existe na base de dados")
    void deleteSkillErrorCase2() {
        Long idFreelancer = 13L;
        Long javaId = 1L;
        Long angularId = 2L;
        List<Skill> skills = Arrays.asList(
                new Skill("Java"),
                new Skill("Angular")
        );

        when(freelancerDao.getSkillsByFreelancerId(idFreelancer)).thenReturn(Arrays.asList("Java", "Angular"));
        when(freelancerDao.idBySkillName(skills.get(0).getSkill())).thenReturn(null);

        Exception e = Assertions.assertThrows(FreelasException.class, () ->{
            freelancerService.deleteSkill(idFreelancer, skills);
        });

        Assertions.assertEquals("Skill inexistente", e.getMessage());

        verify(freelancerDao, times(0)).deleteSkill(Arrays.asList(javaId,angularId), idFreelancer);
    }

    @Test
    @DisplayName("Envia propostas com sucesso para o projeto")
    void sendProposalSucess() {
        ProposalDtoRequest proposal = new ProposalDtoRequest(26L,"Teste",new BigDecimal(500));
        Long idFreelancer = 12L;

        when(projectDao.checkProjectExists(proposal.getIdProject())).thenReturn(true);
        when(projectDao.checkStatusProject(proposal.getIdProject())).thenReturn(StatusProject.OPEN);
        when(customerDao.viewProposalsByProjectId(proposal.getIdProject())).thenReturn(Collections.emptyList());

        freelancerService.sendProposal(proposal, idFreelancer);

        verify(projectDao, times(1)).checkProjectExists(proposal.getIdProject());
        verify(projectDao, times(1)).checkStatusProject(proposal.getIdProject());
        verify(customerDao, times(1)).viewProposalsByProjectId(proposal.getIdProject());

        verify(freelancerDao, times(1)).sendProposal(proposal, idFreelancer);
    }

    @Test
    @DisplayName("Envia propostas com sucesso para o projeto")
    void sendProposalErrorCase1() {
        ProposalDtoRequest proposal = new ProposalDtoRequest(26L,"Teste",new BigDecimal(500));
        Long idFreelancer = 12L;

        when(projectDao.checkProjectExists(proposal.getIdProject())).thenReturn(false);

        Exception e = Assertions.assertThrows(FreelasException.class, () ->{
            freelancerService.sendProposal(proposal, idFreelancer);
        });

        Assertions.assertEquals("Projeto não encontrado", e.getMessage());

        verify(freelancerDao, times(0)).sendProposal(proposal, idFreelancer);
    }

    @Test
    @DisplayName("O projeto nao esta em aberto")
    void sendProposalErrorCase2() {
        ProposalDtoRequest proposal = new ProposalDtoRequest(26L,"Teste",new BigDecimal(500));
        Long idFreelancer = 12L;

        when(projectDao.checkProjectExists(proposal.getIdProject())).thenReturn(true);
        when(projectDao.checkStatusProject(proposal.getIdProject())).thenReturn(StatusProject.IN_PROGRESS);
        Exception e = Assertions.assertThrows(FreelasException.class, () ->{
            freelancerService.sendProposal(proposal, idFreelancer);
        });

        Assertions.assertEquals("Apenas projetos abertos podem receber propostas", e.getMessage());

        verify(freelancerDao, times(0)).sendProposal(proposal, idFreelancer);
    }

    @Test
    @DisplayName("Proposta ja foi enviada para esse projeto")
    void sendProposalErrorCase3() {
        ProposalDtoRequest proposal = new ProposalDtoRequest(26L,"Teste",new BigDecimal(500));
        Long idFreelancer = 12L;

        when(projectDao.checkProjectExists(proposal.getIdProject())).thenReturn(true);
        when(projectDao.checkStatusProject(proposal.getIdProject())).thenReturn(StatusProject.OPEN);
        when(customerDao.viewProposalsByProjectId(proposal.getIdProject())).thenReturn(
                Arrays.asList(new Proposal(1L, 12L,"Teste",new BigDecimal(500))));

        Exception e = Assertions.assertThrows(FreelasException.class, () ->{
            freelancerService.sendProposal(proposal, idFreelancer);
        });

        Assertions.assertEquals("Proposta ja enviada para esse projeto", e.getMessage());

        verify(freelancerDao, times(0)).sendProposal(proposal, idFreelancer);
    }
}