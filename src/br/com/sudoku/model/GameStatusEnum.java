package br.com.sudoku.model;

public enum GameStatusEnum {

    NOT_STARTED("não iniciado"),
    INCOMPLETE("incompleto"),
    COMPLETE("completo");

    private String status;

    private GameStatusEnum(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
