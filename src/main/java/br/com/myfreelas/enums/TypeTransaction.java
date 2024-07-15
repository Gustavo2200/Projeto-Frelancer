package br.com.myfreelas.enums;

public enum TypeTransaction {
    
    PLATAFORM(0,"PLATAFORMA"),
    TAXA(1,"TAXA");

    private int type;
    private String description;

   public int getType() {
       return type;
   }
   
   public String getDescription() {
       return description;
   }

   TypeTransaction(int type, String description) {
       this.type = type;
       this.description = description;
   }

   public static TypeTransaction fromDescription(String description) {
       for (TypeTransaction type : TypeTransaction.values()) {
           if (type.getDescription().equalsIgnoreCase(description)) {
               return type;
           }
       }
       throw new IllegalArgumentException("No enum constant with description " + description);
   }
}
