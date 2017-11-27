/**
 * 
 */
package io.github.azanx.shopping_list.exception;

import java.util.HashMap;

/**
 * @author Kamil Piwowarski
 *
 */

public interface MessageableException {

	public HashMap<String,String> getParameters();
}
