package vttp2023.batch4.paf.assessment.errors;

public class SQLInsertionError extends Exception {
    public SQLInsertionError(){
        super();
    }

    public SQLInsertionError(String msg){
        super(msg);
    }
}
