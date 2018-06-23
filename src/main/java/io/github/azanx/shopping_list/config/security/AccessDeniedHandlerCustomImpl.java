/**
 * 
 */
package io.github.azanx.shopping_list.config.security;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * Custom AccessDeniedHandler implementation. Adds Logging
 * 
 * @see org.springframework.security.web.access.AccessDeniedHandler
 * @see org.springframework.security.access.AccessDeniedException
 * 
 * @author Kamil Piwowarski
 *
 */
public class AccessDeniedHandlerCustomImpl implements AccessDeniedHandler {

    public static final Logger LOGGER = LoggerFactory.getLogger(AccessDeniedHandlerCustomImpl.class);
    private String errorPage;

    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.security.web.access.AccessDeniedHandler#handle(javax.
     * servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * org.springframework.security.access.AccessDeniedException)
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String userName = (auth == null) ? "anonymous" : auth.getName();
        LOGGER.warn("User: {} attempted unauthorized access to URL: {} . message: {}", userName,
                request.getRequestURL(), accessDeniedException.getMessage());

        if (!response.isCommitted()) {
            if (errorPage != null) {
                // Set the 403 status code.
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);

                // forward to error page.
                RequestDispatcher dispatcher = request.getRequestDispatcher(errorPage);
                dispatcher.forward(request, response);
            }
            else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        accessDeniedException.getMessage());
            }
        }
    }

    /**
     * The error page to use. Must begin with a "/" and is interpreted relative to the
     * current context root.
     *
     * @param errorPage the dispatcher path to display
     *
     * @throws IllegalArgumentException if the argument doesn't comply with the above
     * limitations
     */
    public void setErrorPage(String errorPage) {
        if ((errorPage != null) && !errorPage.startsWith("/")) {
            throw new IllegalArgumentException("errorPage must begin with '/'");
        }

        this.errorPage = errorPage;
    }

}
