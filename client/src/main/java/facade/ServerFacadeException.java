package facade;

/**
 * Indicates there was an error connecting to the database
 */
public class ServerFacadeException extends Exception{
    public ServerFacadeException(String message) { super(message); }
    public ServerFacadeException(String message, Throwable ex) {
        super(message, ex);
    }
    public int error_code;
    public void addErrorCode(int code) {error_code = code;}
}
