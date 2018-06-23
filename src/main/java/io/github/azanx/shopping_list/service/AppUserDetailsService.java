/**
 * 
 */
package io.github.azanx.shopping_list.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.github.azanx.shopping_list.config.security.AppUserSecurityPrincipal;
import io.github.azanx.shopping_list.repository.AppUserRepository;

/**
 * AppUser Service for usage by Spring Security
 * 
 * @author Kamil Piwowarski
 *
 */
@Service
public class AppUserDetailsService implements UserDetailsService {

    private AppUserRepository userRepository;

    @Autowired
    public AppUserDetailsService(AppUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.security.core.userdetails.UserDetailsService#
     * loadUserByUsername(java.lang.String)
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new AppUserSecurityPrincipal(//
                userRepository.findByUserName(username)//
                    .orElseThrow(//
                        () -> new UsernameNotFoundException(username)));//
    }

}
