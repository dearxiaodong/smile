package springboot.exception;

/**
 * @author 357069486@qq.com
 * @date 2018-11-8 15:33
 */
public class TipException  extends RuntimeException{
    public TipException() {
    }

    public TipException(String message) {
        super(message);
    }

    public TipException(String message, Throwable cause) {
        super(message, cause);
    }

    public TipException(Throwable cause) {
        super(cause);
    }


}
