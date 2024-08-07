package br.com.myfreelas.dao.customer.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;

import br.com.myfreelas.dao.customer.CustomerDao;
import br.com.myfreelas.err.exceptions.FreelasException;
import br.com.myfreelas.model.Proposal;

@Slf4j
@Repository
public class CustomerDaoImpl implements CustomerDao{

    private final BigDecimal TAXA = new BigDecimal(3);
    private final Long ID_ADMIN = 10L;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public CustomerDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

     @Override
    public List<Proposal> viewProposalsByProjectId(Long idProject) {
        String query = "SELECT * FROM TB_PROPOSTA WHERE FK_ID_PROJETO = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("id", idProject);
        
        try{
            var result = namedParameterJdbcTemplate.queryForList(query, sqlParameterSource);
            List<Proposal> proposals = new ArrayList<>();
            for(var r: result){
                Proposal proposal = new Proposal();
                proposal.setIdProposal(((Number) r.get("nr_id_proposta")).longValue());
                proposal.setComment((String) r.get("nm_comentario"));
                proposal.setValue((BigDecimal) r.get("vl_valor"));
                proposal.setIdFreelancer(((Number) r.get("fk_id_freelancer")).longValue());

                proposals.add(proposal);
            }
            return proposals;
        }catch(Exception e){
            log.error("Erro ao buscar propostas", e.getMessage());
            throw new FreelasException("Erro interno ao buscar propostas", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    @Override
    public void acceptProposal(Long idProposal) {
       String query = "SELECT ACCEPT_PROPOSAL(:idProposal)";

       SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("idProposal", idProposal);

       try{
           namedParameterJdbcTemplate.execute(query, sqlParameterSource, (PreparedStatementCallback<Object>) ps ->{
            ps.execute();
            return null;
           });
       }catch(Exception e){
        log.error("Erro ao aceitar proposta", e.getMessage());
        throw new FreelasException("Erro interno ao aceitar proposta", HttpStatus.INTERNAL_SERVER_ERROR.value());
       }
    }

    @Override
    public void rejectProposal(Long idProposal) {

        try{
            String query = "DELETE FROM TB_PROPOSTA WHERE NR_ID_PROPOSTA = :id";
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("id", idProposal);
            namedParameterJdbcTemplate.update(query, sqlParameterSource);
        }catch(Exception e){
            log.error("Erro ao rejeitar proposta", e.getMessage());
            throw new FreelasException("Erro interno ao rejeitar proposta", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public void completeProject(Long idProject) {
        String query = "SELECT COMPLETE_PROJECT(:idProject, :idAdmin, :taxa)";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("idProject", idProject)
                .addValue("idAdmin", ID_ADMIN)
                .addValue("taxa", TAXA);

        try{
            namedParameterJdbcTemplate.execute(query, sqlParameterSource, (PreparedStatementCallback<Object>) ps ->{
                ps.execute();
                return null;
            });
        }catch(Exception e){
            log.error("Erro ao concluir projeto", e.getMessage());
            throw new FreelasException("Erro interno ao concluir projeto", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public boolean checkProposalExists(Long idProposal) {
    
        String query = "SELECT COUNT(1) FROM TB_PROPOSTA WHERE NR_ID_PROPOSTA = :id";
        try{
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("id", idProposal);
            Integer count = namedParameterJdbcTemplate.queryForObject(query, sqlParameterSource, Integer.class);
            return count != null && count > 0;
        }catch(Exception e){
            log.error("Erro ao buscar proposta", e);
            throw new FreelasException("Erro interno ao buscar proposta", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public boolean checkBalanceCustomer(Long idCustomer, BigDecimal valueProject) {

        String query = "SELECT VL_SALDO FROM TB_USUARIO WHERE NR_ID_USUARIO = :id";
        
        try{
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("id", idCustomer);
            BigDecimal balance = namedParameterJdbcTemplate.queryForObject(query, sqlParameterSource, BigDecimal.class);

            if(balance == null){ 
                return false;
            }

            return balance.compareTo(valueProject) >= 0;
        
        }catch(Exception e){
            log.error("Erro ao checar saldo", e.getMessage());
            throw new FreelasException("Erro checar saldo", HttpStatus.INTERNAL_SERVER_ERROR.value());    
        }
    }

    @Override
    public Long idProjectByIdProposal(Long idProposal) {

        String query = "SELECT FK_ID_PROJETO FROM TB_PROPOSTA WHERE NR_ID_PROPOSTA = :id";
        try{
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("id", idProposal);
            Long idProject = namedParameterJdbcTemplate.queryForObject(query, sqlParameterSource, Long.class);
            return idProject;
        }catch(Exception e){
            log.error("Erro ao buscar proposta", e.getMessage());
            throw new FreelasException("Erro interno ao buscar proposta", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public void depositBalance(Long idCustomer, BigDecimal value) {
        String query = "UPDATE TB_USUARIO SET VL_SALDO = VL_SALDO + :value WHERE NR_ID_USUARIO = :id";
        try{
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                    .addValue("id", idCustomer)
                    .addValue("value", value);
                    
            namedParameterJdbcTemplate.update(query, sqlParameterSource);
        }catch(Exception e){
            log.error("Erro ao depositar saldo", e.getMessage());
            throw new FreelasException("Erro interno ao depositar saldo", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    
}
