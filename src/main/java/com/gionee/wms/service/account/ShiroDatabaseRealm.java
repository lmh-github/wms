package com.gionee.wms.service.account;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gionee.wms.common.ResultCode;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.dto.Menu;
import com.gionee.wms.dto.ShiroUser;
import com.gionee.wms.dto.SsoGetMenuResult;
import com.gionee.wms.dto.TrustedSsoAuthenticationToken;
import com.gionee.wms.web.client.SsoClient;
import com.gionee.wms.web.filter.ShiroAuthenticationFilter;

public class ShiroDatabaseRealm extends AuthorizingRealm {
	
	private static final Logger logger = LoggerFactory.getLogger(ShiroAuthenticationFilter.class);
	
	private SsoClient ssoClient;
	public ShiroDatabaseRealm(){
		setCredentialsMatcher(new AllowAllCredentialsMatcher());
		setAuthenticationTokenClass(TrustedSsoAuthenticationToken.class);
	}

	protected AccountService accountService;

	/**
	 * 认证回调函数,登录时调用.
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		TrustedSsoAuthenticationToken token = (TrustedSsoAuthenticationToken) authcToken;
//		User user = accountService.findUserByLoginName(token.getUsername());
		if (token == null || StringUtils.isBlank(token.getSsoTgt())) {
			return null;
		}

		ShiroUser user = new ShiroUser(token.getUsername(),token.getCredentials().toString());
		//远程到用户中心取菜单信息
		try {
			SsoGetMenuResult result = ssoClient.getMenus(token.getSsoTgt());
			if(result==null){
				throw new AuthenticationException("Get Menue From IUC Error.");
			}
			if(!(ResultCode.SUCCESS+"").equals(result.getCode())){
				//System.err.println("取菜单时异常："+result.getCode());
				logger.error(ResultCode.parseResultCode(result.getCode()));
				return null;
			}
			List<Menu> menus = result.getMenu();
			if(CollectionUtils.isNotEmpty(menus)){
				user.setMenuList(menus);	
			}else{
				user.setMenuList(new ArrayList<Menu>());
			}
			
		} catch (IOException e) {
			throw new AuthenticationException("Get Menue From IUC Error.");
		}
		// return new
		// SimpleAuthenticationInfo(user.getLoginName(),user.getPassword(),getName());
//		return new SimpleAuthenticationInfo(new ShiroUser(user.getId(), user.getLoginName()), user.getPassword(),getName());
		return new SimpleAuthenticationInfo(user, token.getCredentials(),WmsConstants.APP_NAME);
	
	}
	
	//跳过shiro内部密码认证.
    protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws AuthenticationException{
    	
    }

	/**
	 * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
//		List<String> permissionList = accountService.getPermissionList(shiroUser.getId());
		List<String> permissionList = shiroUser.getPermission();
		if (CollectionUtils.isNotEmpty(permissionList)) {
			SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
			// 添加权限信息
			info.addStringPermissions(permissionList);
			return info;
		} else {
			return null;
		}
	}

	/**
	 * 设定Password校验的Hash算法与迭代次数.
	 */
	@PostConstruct
	public void initCredentialsMatcher() {
		HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(Md5Hash.ALGORITHM_NAME);
		// matcher.setHashIterations(AccountService.HASH_INTERATIONS);

		setCredentialsMatcher(matcher);
	}

	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}
	@Autowired
	public void setSsoClient(SsoClient ssoClient) {
		this.ssoClient = ssoClient;
	}
}
