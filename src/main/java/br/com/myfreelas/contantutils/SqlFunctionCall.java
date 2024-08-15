package br.com.myfreelas.contantutils;

public class SqlFunctionCall {
    
    private static final String SELECT = "SELECT * FROM ";

    public static final String HISTORIC_BY_USER = 
        "" + SELECT + FunctionsName.TRANSFER_HISTORY_BY_USER_ID.getFunctionName() + "(?)";
    
    public static final String LIST_PROJECTS_BY_STATUS = 
        "" + SELECT + FunctionsName.LIST_PROJECTS_BY_STATUS.getFunctionName() + "(?)";

    public static final String LIST_PROJECTS_BY_USER = 
        "" + SELECT + FunctionsName.GET_PROJECTS_BY_USER_ID.getFunctionName() + "(?)";

    public static final String LIST_PROJECTS_BY_USER_AND_STATUS = 
        "" + SELECT + FunctionsName.GET_PROJECTS_BY_USER_ID_AND_STATUS.getFunctionName() + "(?,?)";

    public static final String GET_SKILLS_BY_PROJECT_ID = 
        "" + SELECT + FunctionsName.GET_SKILLS_BY_PROJECT_ID.getFunctionName() + "(?)";

    
    public static final String GET_ALL_SKILLS = 
        "" + SELECT + FunctionsName.GET_ALL_SKILLS.getFunctionName() + "()";

    public static final String GET_ALL_SKILLS_FREELANCER =
        "" + SELECT + FunctionsName.GET_ALL_SKILLS_FREELANCER.getFunctionName() + "(?)";

    public static final String GET_ALL_PROPOSALS_FROM_PROJECT = 
        "" + SELECT + FunctionsName.GET_ALL_PROPOSALS_FROM_PROJECT.getFunctionName() + "(?)";
}

