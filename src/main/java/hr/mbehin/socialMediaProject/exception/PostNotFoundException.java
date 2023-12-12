package hr.mbehin.socialMediaProject.exception;

public class PostNotFoundException extends RuntimeException{

    public PostNotFoundException(){
        super();
    }

    public PostNotFoundException(String message){
        super(message);
    }
}
