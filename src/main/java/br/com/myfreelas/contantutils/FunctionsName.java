package br.com.myfreelas.contantutils;

public enum FunctionsName {
    
    SAVE_USER("register_user"),
    USER_BY_ID("find_user_by_id"),
    CHECK_CPF_EXISTS("count_users_by_cpf"),
    CHECK_EMAIL_EXISTS("count_users_by_email"),
    CHECK_PHONE_EXISTS("count_users_by_phone"),
    GET_DATA_AUTHENTICATION("get_data_authentication"),
    TYPE_USER_BY_ID("type_user_by_id"),
    TRANSFER_HISTORY_BY_USER_ID("find_transferencias_by_user"),
    GET_BALANCE("balance_user_by_id"),

    LIST_PROJECTS_BY_STATUS("list_projects_by_status"),
    UPDATE_PROJECT("update_projeto"),
    DELETE_PROJECT_AND_SKILLS("delete_project_and_skills"),
    GET_PROJECT_BY_ID("get_projeto_by_id"),
    GET_PROJECTS_BY_USER_ID("get_projetos_by_user"),
    GET_PROJECTS_BY_USER_ID_AND_STATUS("get_projetos_by_user_and_status"),
    COUNT_PROJECT_BY_ID("count_project_by_id"),
    CUSTOMER_ID_BY_USER_ID("get_customer_id_by_project_id"),
    ADD_SKILL_NECESARY("add_skills_on_project"),
    REMOVE_SKILL_NECESARY("delete_skill_from_project"),
    GET_SKILLS_BY_PROJECT_ID("get_skills_by_project_id"),
    GET_STATUS_FROM_PROJECT("get_status_from_project"),
    GET_PRICE_FROM_PROJECT("get_price_from_projeto"),
    
    SAVE_SKILL("save_skill"),
    GET_ALL_SKILLS("get_all_skills"),
    DELETE_SKILL_BY_ID("delete_skill_by_id"),
    UPDATE_SKILL("update_skill_name"),
    CHECK_SKILL_EXISTS("count_skill_by_name"),
    GET_SKILL_BY_ID("get_skill_by_id"),
    
    ADD_SKILL_FREELANCER("add_skill_freelancer"),
    DELETE_SKILL_FREELANCER("delete_freelancer_skill"),
    GET_ID_SKILL_BY_NAME("get_skill_id_by_name"),
    CHECK_FREELANCER_SKILL("check_freelancer_skill"),
    SEND_PROPOSAL("send_proposal"),
    GET_ALL_SKILLS_FREELANCER("get_skills_by_freelancer_id"),
    
    GET_ALL_PROPOSALS_FROM_PROJECT("get_proposals_by_project_id"),
    ACCEPT_PROPOSAL("ACCEPT_PROPOSAL"),
    DELETE_PROPOSAL("delete_proposal_by_id"),
    COMPLETE_PROJECT("COMPLETE_PROJECT"),
    CHECK_PROPOSAL_EXISTS("count_proposal_by_id"),
    GET_BALANCE_FROM_USER("get_balance_by_user_id"),
    GET_PROJECT_ID_BY_ID_PROPOSAL("get_project_id_by_proposal_id"),
    DEPOSIT_BALANCE("deposit_by_user_id");

    FunctionsName(String functionName){
        this.functionName = functionName;
    }

    private String functionName;

    public String getFunctionName() {
        return functionName;
    }
}
