package springboot.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import springboot.exception.TipException;

/**
 * @author 357069486@qq.com
 * @date 2018-11-13 17:07
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = TipException.class)
    public String tipException(Exception e){
        logger.error("find exception:e+{}",e.getMessage());
        return "comm/error_500";
    }

    @ExceptionHandler(value = Exception.class)
    public String exceptionHandler(Exception e){
        logger.error("find exception:e+{}",e.getMessage());
        return "comm/error_404";
    }



}
