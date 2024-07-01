package br.com.myfrilas.dao.customer.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import br.com.myfrilas.dao.customer.CustomerDao;
import br.com.myfrilas.model.Proposal;

@Repository
public class CustomerDaoImpl implements CustomerDao{

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
            e.printStackTrace();
            throw new RuntimeException("Erro interno ao buscar propostas", e);
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
        e.printStackTrace();
        throw new RuntimeException("Erro interno ao aceitar proposta", e);
       }
    }

    @Override
    public void rejectProposal(Long idProposal) {

        try{
            String query = "DELETE FROM TB_PROPOSTA WHERE NR_ID_PROPOSTA = :id";
            SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("id", idProposal);
            namedParameterJdbcTemplate.update(query, sqlParameterSource);
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Erro interno ao rejeitar proposta", e);
        }
    }

    @Override
    public void completeProject(Long idProject) {
        String query = "SELECT COMPLETE_PROJECT(:idProject)";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("idProject", idProject);

        try{
            namedParameterJdbcTemplate.execute(query, sqlParameterSource, (PreparedStatementCallback<Object>) ps ->{
                ps.execute();
                return null;
            });
        }catch(Exception e){
            e.printStackTrace();
            throw new RuntimeException("Erro interno ao concluir projeto", e);
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
            e.printStackTrace();
            throw new RuntimeException("Erro interno ao buscar proposta", e);
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
            e.printStackTrace();
            throw new RuntimeException("Erro checar saldo", e);
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
            e.printStackTrace();
            throw new RuntimeException("Erro interno ao buscar proposta", e);
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
            e.printStackTrace();
            throw new RuntimeException("Erro interno ao depositar saldo", e);
        }
    }
    
}
