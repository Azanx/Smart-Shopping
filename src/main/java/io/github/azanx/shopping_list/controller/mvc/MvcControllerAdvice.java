package io.github.azanx.shopping_list.controller.mvc;

import io.github.azanx.shopping_list.service.exception.ListNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class MvcControllerAdvice {

    public final static Logger LOG = LoggerFactory.getLogger(MvcControllerAdvice.class);

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler(ListNotFoundException.class)
    public String listNotFound(final ListNotFoundException e) {
        //redirect to home as user requested to display nonexistant list
        LOG.debug("List not found, redirecting to home");
        return "redirect:/";
    }

    @ExceptionHandler(Exception.class)
    public String genericExceptionHandler(Exception e) {
        LOG.error("Uncaught exception encountered!", e);
        return "redirect:/internalError";
    }
}
