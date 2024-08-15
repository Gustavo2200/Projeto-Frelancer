package br.com.myfreelas.dao.user.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import br.com.myfreelas.contantutils.ErrorDatabaseMessageConstants;
import br.com.myfreelas.contantutils.FunctionsName;
import br.com.myfreelas.contantutils.SqlFunctionCall;
import br.com.myfreelas.dao.user.UserDao;
import br.com.myfreelas.enums.TypeTransaction;
import br.com.myfreelas.enums.TypeUser;
import br.com.myfreelas.err.exceptions.DataBaseException;
import br.com.myfreelas.model.Transaction;
import br.com.myfreelas.model.User;

@Slf4j
@Repository
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.SAVE_USER.getFunctionName());

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("p_nm_nome", user.getName())
                .addValue("p_nr_cpf_cnpj", user.getCpfCNPJ())
                .addValue("p_ds_email", user.getEmail())
                .addValue("p_ds_senha", user.getPassword())
                .addValue("p_nr_telefone", user.getPhone())
                .addValue("p_tp_tipo_usuario", user.getTypeUser().toString())
                .addValue("p_vl_valor", BigDecimal.ZERO);

        try {
            jdbcCall.execute(sqlParameterSource);
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch (Exception ex) {
            log.error(ErrorDatabaseMessageConstants.ERROR_SAVE_USER.getMessageDev(), ex.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_SAVE_USER.getMessageUser());
        }
    }
    @Override
    public User userById(Long id) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.USER_BY_ID.getFunctionName());

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("id_user", id);

        try{
            var result = jdbcCall.execute(sqlParameterSource);

            User user = new User();
            user.setName((String) result.get("p_nm_nome"));
            user.setCpfCNPJ((String) result.get("p_nr_cpf_cnpj"));
            user.setEmail((String) result.get("p_ds_email"));
            user.setPassword((String) result.get("p_ds_senha"));
            user.setPhone((String) result.get("p_nr_telefone"));
            user.setTypeUser(TypeUser.fromDescription((String) result.get("p_tp_tipo_usuario")));
            return user;
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_FIND_USER.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_FIND_USER.getMessageUser());
        }
    }

    @Override
    public boolean checkCpfExists(String cpf) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.CHECK_CPF_EXISTS.getFunctionName());

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                    .addValue("p_cpf_cnpj", cpf);
        try{
            Integer count = jdbcCall.executeFunction(Integer.class, sqlParameterSource);
            return count != null && count > 0;
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CHECK_CPF.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CHECK_CPF.getMessageUser());
        }
    }

    @Override
    public boolean checkEmailExists(String email) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.CHECK_EMAIL_EXISTS.getFunctionName());

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                    .addValue("p_email", email);
        try{
            Integer count = jdbcCall.executeFunction(Integer.class, sqlParameterSource);
            return count != null && count > 0;
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CHECK_EMAIL.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CHECK_EMAIL.getMessageUser());
        }
    }

    @Override
    public boolean checkPhoneExists(String phone) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.CHECK_PHONE_EXISTS.getFunctionName());

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                    .addValue("p_phone", phone);
        try{
            Integer count = jdbcCall.executeFunction(Integer.class, sqlParameterSource);
            return count != null && count > 0;
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CHECK_PHONE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CHECK_PHONE.getMessageUser());
        }
    }

    @Override
    public Map<String, Object> getUser(String email) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.GET_DATA_AUTHENTICATION.getFunctionName());

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("p_email", email);
        
        try {
            var result = jdbcCall.execute(sqlParameterSource);

            Map<String, Object> user = new HashMap<>();
            user.put("NR_ID_USUARIO", result.get("p_nr_id_usuario"));
            user.put("DS_EMAIL", result.get("p_ds_email"));
            user.put("DS_SENHA", result.get("p_ds_senha"));
            user.put("TP_TIPO_USUARIO", result.get("p_tp_tipo_usuario"));
            
            return user;
        }catch(EmptyResultDataAccessException e){
            return null;

        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch (Exception e) {
            log.error(ErrorDatabaseMessageConstants.ERROR_FIND_USER.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_FIND_USER.getMessageUser());
        }
    }
    @Override
    public TypeUser typeUserById(Long id) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.TYPE_USER_BY_ID.getFunctionName());

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                        .addValue("p_id_user", id);
        try{
            String result = jdbcCall.executeFunction(String.class, sqlParameterSource);

            return TypeUser.fromDescription(result);
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_FIND_TYPE_USER.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_FIND_TYPE_USER.getMessageUser());
        }
    }

    @Override
    public List<Transaction> transfrerHistoryByUserId(Long idUser) {
        try{
            List<Map<String, Object>> result = jdbcTemplate.queryForList(
                SqlFunctionCall.HISTORIC_BY_USER, idUser);

            List<Transaction> transactions = new ArrayList<>();
            for(var r : result){
                Transaction transaction = new Transaction();
                transaction.setId(((Number) r.get("p_NR_ID_TRANSFERENCIA")).longValue());
                transaction.setValue((BigDecimal) r.get("p_VL_VALOR"));
                transaction.setDate(((Timestamp) r.get("p_DT_DATA")).toLocalDateTime());

                Long idPayer = ((Number) r.get("p_FK_ID_PAGADOR")).longValue();
                if(idPayer == idUser){
                    transaction.setEntryOrExit("Saida");
                }else{
                    transaction.setEntryOrExit("Entrada");
                }
                transaction.setIdPayer(idPayer);
                transaction.setIdRecipient(((Number) r.get("p_FK_ID_BENEFICIARIO")).longValue());
                transaction.setIdPayer(((Number) r.get("p_FK_ID_PAGADOR")).longValue());
                transaction.setType(TypeTransaction.fromDescription(r.get("p_TP_TIPO_TRANSACAO").toString()));
                transactions.add(transaction);
            }

            return transactions;
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch (Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_GET_HISTORIC_TRANSFER.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_GET_HISTORIC_TRANSFER.getMessageUser());
        }
    }

    @Override
    public BigDecimal getBalance(Long id) {
        SimpleJdbcCall jdbcCall = createJdbcCall(FunctionsName.GET_BALANCE.getFunctionName());
        
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                    .addValue("p_id_user", id);
        try{
            return jdbcCall.executeFunction(BigDecimal.class, sqlParameterSource);
        
        }catch(DataAccessException e){
            log.error(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_CONECTION_DATABASE.getMessageUser());
 
        }catch(Exception e){
            log.error(ErrorDatabaseMessageConstants.ERROR_GET_BALANCE.getMessageDev(), e.getMessage());
            throw new DataBaseException(ErrorDatabaseMessageConstants.ERROR_GET_BALANCE.getMessageUser());
            
        }
    }
    private SimpleJdbcCall createJdbcCall(String functionName) {
        return new SimpleJdbcCall(jdbcTemplate).withFunctionName(functionName);
    }
}
