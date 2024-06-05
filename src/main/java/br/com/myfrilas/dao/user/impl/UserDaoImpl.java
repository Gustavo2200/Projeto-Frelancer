package br.com.myfrilas.dao.user.impl;

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
import br.com.myfrilas.err.exceptions.FreelasException;
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
                .addValue("p_nr_ranking", user.getRanking())
                .addValue("p_tp_tipo_usuario", user.getTypeUser().toString());

        try {
            jdbcCall.execute(sqlParameterSource);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FreelasException("Erro interno ao salvar usuário", HttpStatus.BAD_REQUEST.value());
        }
    }

    private SimpleJdbcCall createJdbcCall(String functionName) {
        return new SimpleJdbcCall(jdbcTemplate).withFunctionName(functionName);
    }

    @Override
    public boolean checkCpfExists(String cpf) {
        String querry = "SELECT COUNT(1) FROM TB_USUARIO WHERE NR_CPF = :cpf";  //parametro nomeado :cpf sera substituido por cpf
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("cpf", cpf);
        Integer count = namedParameterJdbcTemplate.queryForObject(querry, namedParameters, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public boolean checkEmailExists(String email) {
        String querry = "SELECT COUNT(1) FROM TB_USUARIO WHERE DS_EMAIL = :email";
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("email", email);
        Integer count = namedParameterJdbcTemplate.queryForObject(querry, namedParameters, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public boolean checkPhoneExists(String phone) {
        String querry = "SELECT COUNT(1) FROM TB_USUARIO WHERE NR_TELEFONE = :phone"; 
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("phone", phone);
        Integer count = namedParameterJdbcTemplate.queryForObject(querry, namedParameters, Integer.class);
        return count != null && count > 0;
    }

    @Override
    public Map<String, Object> getUser(String email) {
        String query = "SELECT NR_ID_USUARIO, DS_EMAIL, DS_SENHA, TP_TIPO_USUARIO FROM TB_USUARIO WHERE DS_EMAIL = :email";
        SqlParameterSource parameters = new MapSqlParameterSource().addValue("email", email);
        
        try {
            return namedParameterJdbcTemplate.queryForMap(query, parameters);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
