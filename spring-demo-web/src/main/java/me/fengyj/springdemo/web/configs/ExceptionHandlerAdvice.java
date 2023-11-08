package me.fengyj.springdemo.web.configs;

import me.fengyj.springdemo.utils.exceptions.ResourceNotFoundException;
import me.fengyj.springdemo.utils.exceptions.UserInvalidInputException;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(UserInvalidInputException.class)
    public ModelAndView handleUserInvalidInputException(UserInvalidInputException ex) {
        ModelAndView mav = new ModelAndView("errors/400");
        mav.setStatus(HttpStatusCode.valueOf(400));
        mav.addObject("message", ex.getMessage());
        mav.addObject("rawInput", ex.getRawInput());
        return mav;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFoundException(ResourceNotFoundException ex) {
        ModelAndView mav = new ModelAndView("errors/404");
        mav.setStatus(HttpStatusCode.valueOf(404));
        mav.addObject("message", ex.getMessage());
        mav.addObject("resourceId", ex.getResourceId());
        mav.addObject("resourceType", ex.getResourceType());
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleUnknownException(Exception ex) {

        ModelAndView mav = new ModelAndView("errors/500");
        mav.setStatus(HttpStatusCode.valueOf(500));
        mav.addObject("message", ex.getMessage());
        return mav;
    }
}
