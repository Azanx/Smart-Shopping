/**
 * 
 */
package io.github.azanx.shopping_list.config;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Kamil Piwowarski
 *
 */
public class SessionListener implements HttpSessionListener{

	private static final Logger LOGGER = LoggerFactory.getLogger(SessionListener.class);
	private final int maxInactiveInterval;
	
	
	
	public SessionListener(int maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		session.setMaxInactiveInterval(maxInactiveInterval);
		LOGGER.debug("New session started, ID: {}, maxInactiveInterval (in seconds): {}", session.getId(), maxInactiveInterval);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		HttpSession session = se.getSession();
		LOGGER.debug("Session destroyed, ID: {}", session.getId());
	}

}
