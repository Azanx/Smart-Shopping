/**
 * 
 */
package io.github.azanx.shopping_list.controller;


import static org.springframework.test.util.AssertionErrors.assertTrue;

import java.util.Optional;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

/**
 * Used for assertions on globalErrors on the model which are not implemented in org.springframework.test.web.servlet.result.ModelResultMatchers
 * @author Kamil Piwowarski
 *
 */
public class GlobalErrorModelResultMatchers{

    protected GlobalErrorModelResultMatchers() {
    }

    public static GlobalErrorModelResultMatchers globalBindingErrors() {
        return new GlobalErrorModelResultMatchers();
    }

    /**
     * Assert the given model attribute has error/s
     * @param attributeName
     * @return
     */
    public ResultMatcher attributehasGlobalErrors(final String attributeName) {
        return result -> {
            ModelAndView mav = getModelAndView(result);
            BindingResult bindingResult = getBindingResult(mav, attributeName);
            assertTrue("No global binding errors for attribute: " + attributeName, bindingResult.hasGlobalErrors());
        };
    }

    public ResultMatcher hasGlobalErrorCode(final String attributeName, final String error) {
        return result -> {
            ModelAndView mav = getModelAndView(result);
            BindingResult bindingResult = getBindingResult(mav, attributeName);
            assertTrue("No global binding errors for attribute: " + attributeName, bindingResult.hasGlobalErrors());
            Optional<String> presentError = bindingResult.getGlobalErrors()//
                .stream()//
                    .map(globalError -> globalError.getCode())//
                        .filter(errorCode -> errorCode.equals(error))//
                            .findAny();//
            assertTrue("No global error with code " + error + " found for attribute: " + attributeName, presentError.isPresent());
        };
    }

    private ModelAndView getModelAndView(MvcResult mvcResult) {
        ModelAndView mav = mvcResult.getModelAndView();
        assertTrue("No ModelAndView found", mav != null);
        return mav;
    }

    private BindingResult getBindingResult(ModelAndView mav, String name) {
        BindingResult result = (BindingResult) mav.getModel().get(BindingResult.MODEL_KEY_PREFIX + name);
        assertTrue("No BindingResult for attribute: " + name, result != null);
        return result;
    }
}
