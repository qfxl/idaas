package com.zbycorp.user.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 * @author xuyonghong
 * @date 2025-06-24 17:49
 **/
public class IdsUserDetailsManager implements UserDetailsManager,
        UserDetailsPasswordService {
    /**
     * Modify the specified user's password. This should change the user's password in the
     * persistent user repository (database, LDAP etc).
     *
     * @param user        the user to modify the password for
     * @param newPassword the password to change to,
     *                    encoded by the configured {@code PasswordEncoder}
     * @return the updated UserDetails with the new password
     */
    @Override
    public UserDetails updatePassword(UserDetails user, String newPassword) {
        return null;
    }

    /**
     * Create a new user with the supplied details.
     *
     * @param user
     */
    @Override
    public void createUser(UserDetails user) {

    }

    /**
     * Update the specified user.
     *
     * @param user
     */
    @Override
    public void updateUser(UserDetails user) {

    }

    /**
     * Remove the user with the given login name from the system.
     *
     * @param username
     */
    @Override
    public void deleteUser(String username) {

    }

    /**
     * Modify the current user's password. This should change the user's password in the
     * persistent user repository (datbase, LDAP etc).
     *
     * @param oldPassword current password (for re-authentication if required)
     * @param newPassword the password to change to
     */
    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    /**
     * Check if a user with the supplied login name exists in the system.
     *
     * @param username
     */
    @Override
    public boolean userExists(String username) {
        return false;
    }

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
