package com.aegis.es_demo.config;

import com.aegis.es_demo.domin.Role;
import com.aegis.es_demo.domin.User;
import com.aegis.es_demo.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.List;

@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        String token = principalCollection.getPrimaryPrincipal().toString();
        User user = JWTUtil.getUsername(token);
        List<Role> list = user.getRoles();
        list.forEach(role -> info.addRole(role.getName()));
        list.forEach(role -> role.getPermissions().forEach(permission -> info.addStringPermission(permission.getCode())));
        return info;
    }

//    身份认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String token = (String) authenticationToken.getCredentials();//login(jwtToken)，带有jwtToken信息,被shiroFilter拦截
        User user = JWTUtil.getUsername(token);//获取该用户
        if (user == null) {
            throw new AuthenticationException("用户不存在!");
        }
        String username = user.getName();
        int jwt = JWTUtil.verify(token, username, user.getPassword());
        if (jwt!=0){
            throw new AuthenticationException("登录失效，请重新登录!");
        }
        return new SimpleAuthenticationInfo(token, token, getName());//返回shiro需要的数据格式
    }
}
