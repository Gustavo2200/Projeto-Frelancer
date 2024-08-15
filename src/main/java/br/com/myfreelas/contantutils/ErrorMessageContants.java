package br.com.myfreelas.contantutils;

public enum ErrorMessageContants {

    USER_NOT_FOUND("Usuário não encontrado"),
    USER_NOT_FOUND_WITH_EMAIL("Usuário não encontrado com o email informado"),
    INCORRECT_PASSWORD("Senha incorreta"),
    TOO_MANY_REQUESTS("Limite de tentativas excedido, tente novamente mais tarde"),
    ACCESS_DENIED("Acesso negado. Você não tem permissão para acessar este recurso"),
    
    PROPOSAL_NOT_FOUND("Proposta não encontrada"),
    PROPOSAL_ALREDY_SEND("Proposta ja enviada para esse projeto"),
    PROJECT_NOT_OPEN("Voce so pode concluir projetos que estejam em andamento"),
    PROJECT_NOT_OPEN_TO_SEND_PROPOSAL("Apenas projetos abertos podem receber propostas"),
    CANNOT_DELETE_ONGOING_PROJECTS("Para deletar projetos em andamento, contate o administrador"),
    INSUFFICIENT_BALANCE("Seu saldo é insuficiente"),
    PROJECT_NOT_FOUND("Projeto não encontrado"),
    CUSTOMER_NOT_OWNER("Voce so pode gerenciar propostas do seu projeto"),
    STATUS_PROJECT_NOT_FOUND("Status do projeto não corresponde a nenhuma das opções"),
    
    SKILL_NOT_FOUND("Skill não encontrada"),
    SKILL_NOT_FOUND_TO_FREELANCER("Skill não encontrada para esse freelancer"),
    SKILL_NOT_FOUND_TO_PROJECT("Skill não encontrada para esse projeto"),
    NO_SKILLS_REPORTED("Nenhuma skill informada"),
    SKILL_ALREADY_SAVED("Skill ja foi adicionada"),
    
    CPF_ALREADY_REGISTERED("CPF ja registrado"),
    CNPJ_ALREADY_REGISTERED("CNPJ ja registrado"),
    EMAIL_ALREADY_REGISTERED("Email ja registrado"),
    PHONE_ALREADY_REGISTERED("Telefone ja registrado"),
    
    NAME_INVALID("Nome nao pode conter caracteres especiais ou numericos"),
    CPF_INVALID("Cpf invalido"),
    CNPJ_INVALID("Cnpj invalido"),
    EMAIL_INVALID("Email invalido"),
    PHONE_INVALID("Telefone invalido"),
    PASSWORD_INVALID("Senha deve ter entre 6 e 20 caracteres"),
    TYPE_USER_INVALID("Tipo de usuario invalido, deve ser FREELANCER ou CUSTOMER"),
    INVALID_DATA_TYPE("Tipo de dado invalido, por favor verifique e tente novamente");  

    ErrorMessageContants(String message) {
        this.message = message;
    }
    private String message;

    public String getMessage() {
        return message;
    }
    
}
