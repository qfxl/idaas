package com.zbycorp.user.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author xuyonghong
 * @date 2025-06-24 16:46
 **/
public class IdsUserDetailService implements UserDetailsService {
    /**
     * 用户认证：
     * 在用户登录时，Spring Security 会调用此方法，根据输入的用户名（username）查询用户详情（包括密码、权限等），然后与用户输入的密码进行比对，完成认证。
     * <p>
     * 返回用户信息：
     * 方法需要返回一个 UserDetails 对象，该对象包含：
     * <p>
     * 用户名（username）
     * 密码（password）
     * 用户权限（authorities/roles）
     * 账户状态（是否启用、是否过期等）
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //只判断用户是否存在，密码校验请见 IdsUserAuthenticationProvider
        return new IdsUserDetails(null);
    }

}
