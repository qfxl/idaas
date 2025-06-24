package com.zbycorp.user.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author xuyonghong
 * @date 2025-06-24 18:17
 **/
public class IdsUserAuthenticationProvider implements AuthenticationProvider {

    private final IdsUserDetailService idsUserDetailService;

    public IdsUserAuthenticationProvider(IdsUserDetailService idsUserDetailService) {
        this.idsUserDetailService = idsUserDetailService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();
        // 加载用户信息
        IdsUserDetails user = (IdsUserDetails) idsUserDetailService.loadUserByUsername(username);
        // 创建认证通过的Token
        return new UsernamePasswordAuthenticationToken(
                user, null, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
