/**
 * 
 */
package io.github.azanx.shopping_list.controller.exception;

import java.util.Map;

/**
 * 
 * @author Kamil Piwowarski
 *
 */
public class MessageableExceptionResponse {

    private int errorCode;
    private Map<String,String> parameters;

    public MessageableExceptionResponse(Map<String, String> parameters, int status) {
        super();
        this.parameters = parameters;
        this.errorCode = status;
    }

    /**
     * @return error code for this exception, mainly HttpStatus number
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Get Parameters of this exception
     * @return Map of exception parameters and their values
     */
    public Map<String, String> getParameters() {
        return parameters;
    }
}
