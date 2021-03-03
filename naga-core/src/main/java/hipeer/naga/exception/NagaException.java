package hipeer.naga.exception;

import lombok.Getter;

@Getter
public class NagaException extends RuntimeException {

    private String errorMsg;
    private int errorCode;

    public NagaException(String errorMsg, int errorCode) {
        super(errorMsg);
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }

    public NagaException(String errorMsg, int errorCode, Throwable cause) {
        super(cause);
        this.errorMsg = errorMsg;
        this.errorCode = errorCode;
    }
}
