package com.xuecheng.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * 这个类先不用，让它报错
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse doException(Exception e) {
        String message = e.getMessage();
        log.error("异常类: {}", e);
        log.error("异常信息: {}",message);
        return new RestErrorResponse(message);
    }



    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse doRuntimeException(RuntimeException e) {
        String message = e.getMessage();
        log.error("异常类: {}", e);
        log.error("异常信息: {}",message);
        return new RestErrorResponse(message);
    }

    /**
     * 捕获参数校验产生的bug，例如参数为空、参数不符合要求
     * @param e 参数校验产生的异常
     * @return 返回统一异常信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse doMethodArgumentNotValidException(MethodArgumentNotValidException e){

        BindingResult bindingResult = e.getBindingResult();
        //校验的错误信息
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        //收集错误
        StringBuffer errors = new StringBuffer();
        fieldErrors.forEach(error->{
            errors.append(error.getDefaultMessage()).append("\n");
        });

        return new RestErrorResponse(errors.toString());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse doAccessDeniedException(AccessDeniedException e) {
        String message = "老毕等，想访问这借口你配吗";
        return new RestErrorResponse(message);
    }

}
