package br.com.projeto.slim_tech.exception;

public class SlimTechException extends  RuntimeException{

    public SlimTechException (String message){
        super(message);
    }
    public SlimTechException (String message, Throwable cause){
        super(message,cause);
    }
}
