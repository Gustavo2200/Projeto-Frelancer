package br.com.myfreelas.dao.customer.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import lombok.extern.slf4j.Slf4j;
import br.com.myfreelas.contantutils.ErrorDatabaseMessageConstants;
import br.com.myfreelas.contantutils.FunctionsName;
import br.com.myfreelas.contantutils.SqlFunctionCall;
import br.com.myfreelas.dao.customer.CustomerDao;
import br.com.myfreelas.err.exceptions.DataBaseException;
import br.com.myfreelas.model.Proposal;

@Slf4j
@Repository
public class CustomerDaoImpl implements CustomerDao{

    private final BigDecimal TAXA = new BigDecimal(3);
    private final Long ID_ADMIN = 10L;

    private JdbcTemplate jdbcTemplate;

    public CustomerDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

     @Override
    public List<Proposal> viewProposalsByProjectId(Long idProject) { 
        try{
            var result = jdbcTemplate.queryForList(SqlFunctionCall.GET_ALL_PROPOSALS_FROM_PROJECT, idProject);
            List<Proposal> proposals = new ArrayList<>();
            for(var r: result){
                Proposal proposal = new Proposal();
                proposal.setIdProposal(((Number) r.get("p_nr_id_proposta")).longValue());
                proposal.setComment((String) r.get("p_nm_comentario"));
                proposal.setValue((BigDecimal) r.get("p_vl_valor"));
                proposal.setIdFreelancer(((Number) r.get("p_fk_id_freelancer")).longValue());

                proposals.add(proposal);
            }
            return proposals;
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
        
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_FIND_PROPOSALS.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_FIND_PROPOSALS.getMessageUser());
        }
    }
    @Override
    public void acceptProposal(Long idProposal) {
       SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.ACCEPT_PROPOSAL.getFunctionName());

       SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("p_id_proposta", idProposal);

       try{
           jdbcCall.execute(sqlParameterSource);
       
        }catch(DataAccessException e){
           log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
           throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());

        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_ACCEPT_PROPOSAL.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_ACCEPT_PROPOSAL.getMessageUser());
       }
    }

    @Override
    public void rejectProposal(Long idProposal) {
        
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.DELETE_PROPOSAL.getFunctionName());
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("p_id_proposal", idProposal);
        try{
            jdbcCall.execute(sqlParameterSource);
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_REJECT_PROPOSAL.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_REJECT_PROPOSAL.getMessageUser());
        }
    }

    @Override
    public void completeProject(Long idProject) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.COMPLETE_PROJECT.getFunctionName());

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("p_id_projeto", idProject)
                .addValue("p_id_conta_taxa", ID_ADMIN)
                .addValue("p_percentual_taxa", TAXA);

        try{
            jdbcCall.execute(sqlParameterSource);
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_COMPLETE_PROJECT.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_COMPLETE_PROJECT.getMessageUser());
        }
    }

    @Override
    public boolean checkProposalExists(Long idProposal) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.CHECK_PROPOSAL_EXISTS.getFunctionName());
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("p_id_proposta", idProposal);
        try{
            Integer count = jdbcCall.executeFunction(Integer.class, sqlParameterSource);
            return count != null && count > 0;
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_FIND_PROPOSAL.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_FIND_PROPOSAL.getMessageUser());
        }
    }

    @Override
    public boolean checkBalanceCustomer(Long idCustomer, BigDecimal valueProject) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.GET_BALANCE_FROM_USER.getFunctionName());
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("p_id_usuario", idCustomer);
        try{
            BigDecimal balance = jdbcCall.executeFunction(BigDecimal.class, sqlParameterSource);
            return balance.compareTo(valueProject) >= 0;
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser()); 
        
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CHECK_BALANCE_CUSTOMER.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CHECK_BALANCE_CUSTOMER.getMessageUser());    
        }
    }

    @Override
    public Long idProjectByIdProposal(Long idProposal) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.GET_PROJECT_ID_BY_ID_PROPOSAL.getFunctionName());
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("p_id_proposta", idProposal);
        try{
            Long idProject = jdbcCall.executeFunction(Long.class, sqlParameterSource);
            return idProject;
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_FIND_ID_PROJECT.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_FIND_ID_PROJECT.getMessageUser());
        }
    }

    @Override
    public void depositBalance(Long idCustomer, BigDecimal value) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.DEPOSIT_BALANCE.getFunctionName());
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("p_id_usuario", idCustomer)
                .addValue("p_value", value);
        try{         
            jdbcCall.execute(sqlParameterSource);
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_DEPOSIT_BALANCE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_DEPOSIT_BALANCE.getMessageUser());
        }
    }
    
    private SimpleJdbcCall createJdbcCall(String functionName) {
        return new SimpleJdbcCall(jdbcTemplate).withFunctionName(functionName);
    } 
}
