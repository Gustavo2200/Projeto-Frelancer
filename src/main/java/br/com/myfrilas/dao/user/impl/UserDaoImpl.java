package br.com.myfrilas.dao.user.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.myfrilas.dao.user.UserDao;
import br.com.myfrilas.enums.TypeTransaction;
import br.com.myfrilas.enums.TypeUser;
import br.com.myfrilas.err.exceptions.FreelasException;
import br.com.myfrilas.model.Transaction;
import br.com.myfrilas.model.User;

@Repository
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate; //utilizado  para lidar com parâmetros nomeados, o que melhora a legibilidade e a segurança do código.

    public UserDaoImpl(JdbcTemplate jdbcTemplate , NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        SimpleJdbcCall jdbcCall = createJdbcCall("register_user");

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("p_nm_nome", user.getName())
                .addValue("p_nr_cpf", user.getCpf())
                .addValue("p_ds_email", user.getEmail())
                .addValue("p_ds_senha", user.getPassword())
                .addValue("p_nr_telefone", user.getPhone())
                .addValue("p_tp_tipo_usuario", user.getTypeUser().toString())
                .addValue("p_vl_valor", BigDecimal.ZERO);

        try {
            jdbcCall.execute(sqlParameterSource);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FreelasException("Erro interno ao salvar usuário", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    @Override
    public User userById(Long id) {
        String query = "SELECT * FROM TB_USUARIO WHERE NR_ID_USUARIO = :id";
        try{
            SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
            Map<String, Object> result = namedParameterJdbcTemplate.queryForMap(query, namedParameters);
            User user = new User();
            user.setName(result.get("NM_NOME").toString());
            user.setCpf(result.get("NR_CPF").toString());
            user.setEmail(result.get("DS_EMAIL").toString());
            user.setPassword(result.get("DS_SENHA").toString());
            user.setPhone(result.get("NR_TELEFONE").toString());
            user.setTypeUser(TypeUser.valueOf(result.get("TP_TIPO_USUARIO").toString()));
           
            return user;
        }catch (Exception e){
            e.printStackTrace();
            throw new FreelasException("Erro interno ao buscar usuário", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public boolean checkCpfExists(String cpf) {
        String querry = "SELECT COUNT(1) FROM TB_USUARIO WHERE NR_CPF = :cpf";  //parametro nomeado :cpf sera substituido por cpf
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("cpf", cpf);
        try{
            Integer count = namedParameterJdbcTemplate.queryForObject(querry, namedParameters, Integer.class);
            return count != null && count > 0;
        }catch(Exception e){
            e.printStackTrace();
            throw new FreelasException("Erro interno ao buscar cpf", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public boolean checkEmailExists(String email) {
        String querry = "SELECT COUNT(1) FROM TB_USUARIO WHERE DS_EMAIL = :email";
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("email", email);
        try{
            Integer count = namedParameterJdbcTemplate.queryForObject(querry, namedParameters, Integer.class);
            return count != null && count > 0;
        }catch(Exception e){
            e.printStackTrace();
            throw new FreelasException("Erro interno ao buscar email", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public boolean checkPhoneExists(String phone) {
        String querry = "SELECT COUNT(1) FROM TB_USUARIO WHERE NR_TELEFONE = :phone"; 
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("phone", phone);
        try{
            Integer count = namedParameterJdbcTemplate.queryForObject(querry, namedParameters, Integer.class);
            return count != null && count > 0;
        }catch(Exception e){
            e.printStackTrace();
            throw new FreelasException("Erro interno ao buscar telefone", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public Map<String, Object> getUser(String email) {
        String query = "SELECT NR_ID_USUARIO, DS_EMAIL, DS_SENHA, TP_TIPO_USUARIO FROM TB_USUARIO WHERE DS_EMAIL = :email";
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("email", email);
        
        try {
            return namedParameterJdbcTemplate.queryForMap(query, parameters);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FreelasException("Erro interno ao buscar usuário", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
    @Override
    public TypeUser typeUserById(Long id) {
        String query = "SELECT TP_TIPO_USUARIO FROM TB_USUARIO WHERE NR_ID_USUARIO = :id";
        SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("id", id);
        try{
            String type = namedParameterJdbcTemplate.queryForObject(query, parameterSource, String.class);
        
            return TypeUser.fromDescription(type);
        }catch(Exception e){
            e.printStackTrace();
            throw new FreelasException("Erro interno ao buscar tipo de usuário", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public List<Transaction> transfrerHistoryByUserId(Long idUser) {
        String query = "SELECT * FROM TB_TRANSFERENCIA WHERE FK_ID_PAGADOR = :idUser OR FK_ID_BENEFICIARIO = :idUser";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("idUser", idUser);

        try{
            var result = namedParameterJdbcTemplate.queryForList(query, sqlParameterSource);

            List<Transaction> transactions = new ArrayList<>();
            for(var r : result){
                Transaction transaction = new Transaction();
                transaction.setId(((Number) r.get("NR_ID_TRANSFERENCIA")).longValue());
                transaction.setValue((BigDecimal) r.get("VL_VALOR"));
                transaction.setDate(((Timestamp) r.get("DT_DATA")).toLocalDateTime());

                Long idPayer = ((Number) r.get("FK_ID_PAGADOR")).longValue();
                if(idPayer == idUser){
                    transaction.setEntryOrExit("Saida");
                }else{
                    transaction.setEntryOrExit("Entrada");
                }
                transaction.setIdPayer(idPayer);
                transaction.setIdRecipient(((Number) r.get("FK_ID_BENEFICIARIO")).longValue());
                transaction.setIdPayer(((Number) r.get("FK_ID_PAGADOR")).longValue());
                transaction.setType(TypeTransaction.fromDescription(r.get("TP_TIPO_TRANSACAO").toString()));
                transactions.add(transaction);
            }

            return transactions;
        }catch (Exception e){
            e.printStackTrace();
            throw new FreelasException("Erro interno ao buscar historico de transferências", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    @Override
    public BigDecimal getBalance(Long id) {
        String query = "SELECT VL_SALDO FROM TB_USUARIO WHERE NR_ID_USUARIO = :id";
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource().addValue("id", id);
        try{
            BigDecimal balance = namedParameterJdbcTemplate.queryForObject(query, sqlParameterSource, BigDecimal.class);
            return balance;
        }catch(Exception e){
            e.printStackTrace();
            throw new FreelasException("Erro interno ao buscar saldo", HttpStatus.INTERNAL_SERVER_ERROR.value());
            
        }
    }
    private SimpleJdbcCall createJdbcCall(String functionName) {
        return new SimpleJdbcCall(jdbcTemplate).withFunctionName(functionName);
    }
}
