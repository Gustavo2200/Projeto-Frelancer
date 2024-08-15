package br.com.myfreelas.contantutils;

public enum ErrorDatabaseMessageConstants {

    ERROR_CONECTION_DATABASE(
        "Houve um erro inesperado ao conectar com o banco de dados", 
         "Erro ao conectar com o banco de dados {}"),

    ERROR_FIND_PROPOSALS(
        "Houve um erro inesperado ao buscar propostas", 
         "Erro ao buscar propostas {}"),
         
    ERROR_ACCEPT_PROPOSAL(
        "Houve um erro inesperado ao tentar aceitar a proposta", 
         "Erro ao aceitar proposta {}"),
         
    ERROR_REJECT_PROPOSAL(
        "Houve um erro inesperado ao tentar rejeitar a proposta", 
         "Erro ao rejeitar proposta {}"),
         
    ERROR_COMPLETE_PROJECT(
        "Houve um erro inesperado ao tentar concluir o projeto", 
         "Erro ao concluir projeto {}"),
         
    ERROR_FIND_PROPOSAL(
        "Houve um erro inesperado ao tentar buscar a proposta", 
         "Erro ao buscar proposta {}"),
         
    ERROR_CHECK_BALANCE_CUSTOMER(
        "Houve um erro inesperado ao tentar checar seu saldo", 
         "Erro ao checar o saldo do cliente {}"),
         
    ERROR_FIND_ID_PROJECT(
        "Houve um erro inesperado ao tentar buscar o id do projeto", 
         "Erro ao buscar o id do projeto {}"),

    ERROR_DEPOSIT_BALANCE(
        "Houve um erro inesperado ao tentar depositar o saldo", 
         "Erro ao depositar o saldo do cliente {}"),
         
    ERROR_SAVE_SKILL(
        "Houve um erro inesperado ao tentar salvar a skill", 
         "Erro ao salvar a skill {}"),
    
    ERROR_DELETE_SKILL(
        "Houve um erro inesperado ao tentar deletar a skill", 
         "Erro ao deletar a skill {}"),
    
    ERROR_FIND_SKILL(
        "Houve um erro inesperado ao tentar buscar a skill", 
         "Erro ao buscar a skill {}"),

    ERROR_SEND_PROPOSAL(
        "Houve um erro inesperado ao tentar enviar a proposta", 
         "Erro ao enviar proposta {}"),
    
    ERROR_SAVE_PROJECT(
        "Houve um erro inesperado ao tentar salvar o projeto", 
         "Erro ao salvar o projeto {}"),

    ERROR_FIND_PROJECT(
        "Houve um erro inesperado ao tentar buscar o projeto", 
         "Erro ao buscar o projeto {}"),

    ERROR_FIND_PROJECTS(
        "Houve um erro inesperado ao tentar buscar os projetos", 
         "Erro ao buscar os projetos {}"),

    ERROR_UPDATE_PROJECT(
        "Houve um erro inesperado ao tentar atualizar o projeto", 
         "Erro ao atualizar projeto {}"),

    ERROR_DELETE_PROJECT(
        "Houve um erro inesperado ao tentar deletar o projeto", 
         "Erro ao deletar o projeto {}"),

    ERROR_FIND_CUSTOMER(
        "Houve um erro inesperado ao tentar buscar o cliente", 
         "Erro ao buscar o cliente {}"),
    
    ERROR_ADD_SKILL_DEPENDENCY(
        "Houve um erro inesperado ao tentar adicionar a dependencia",
         "Erro ao adicionar a dependencia {}"),    
    
    ERROR_REMOVE_SKILL_DEPENDENCY(
        "Houve um erro inesperado ao tentar remover a dependencia",
         "Erro ao remover a dependencia {}"),
    
    ERROR_FIND_SKILL_PROJECT(
        "Houve um erro inesperado ao tentar buscar a skill do projeto",
         "Erro ao buscar a skill do projeto {}"),

    ERROR_FIND_STATUS_PROJECT(
        "Houve um erro inesperado ao tentar buscar o status do projeto",
         "Erro ao buscar o status do projeto {}"),

    ERROR_FIND_PRICE_PROJECT(
        "Houve um erro inesperado ao tentar buscar o preço do projeto",
         "Erro ao buscar o preço do projeto {}"),

    ERROR_UPDATE_SKILL(
        "Houve um erro inesperado ao tentar atualizar a skill",
         "Erro ao atualizar a skill {}"),

    ERROR_VERIFY_SKILL_EXISTS(
        "Houve um erro inesperado ao tentar verificar se a skill existe",
         "Erro ao verificar se a skill existe {}"),

    ERROR_SAVE_USER(
        "Houve um erro inesperado ao tentar salvar o usuário",
         "Erro ao salvar o usuário {}"),

    ERROR_FIND_USER(
        "Houve um erro inesperado ao tentar buscar o usuário",
         "Erro ao buscar o usuário {}"),

    ERROR_CHECK_CPF(
        "Houve um erro inesperado ao tentar checar o cpf",
         "Erro ao checar o cpf {}"),

    ERROR_CHECK_EMAIL(
        "Houve um erro inesperado ao tentar checar o email",
         "Erro ao checar o email {}"),

    ERROR_CHECK_PHONE(
        "Houve um erro inesperado ao tentar checar o telefone",
         "Erro ao checar o telefone {}"),

    ERROR_FIND_TYPE_USER(
        "Houve um erro inesperado ao tentar buscar o tipo de usuário",
         "Erro ao buscar o tipo de usuário {}"),

    ERROR_GET_HISTORIC_TRANSFER(
        "Houve um erro inesperado ao tentar buscar o historico de transferencia",
         "Erro ao buscar o historico de transferencia {}"),

    ERROR_GET_BALANCE(
        "Houve um erro inesperado ao tentar buscar o saldo",
         "Erro ao buscar o saldo {}");


    private String messageUser;
    private String messageDev;

    ErrorDatabaseMessageConstants(String messageUser, String messageDev) {
        this.messageUser = messageUser;
        this.messageDev = messageDev;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public String getMessageDev(){
        return messageDev;
    }
}