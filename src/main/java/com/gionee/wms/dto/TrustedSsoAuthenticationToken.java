/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.gionee.wms.dto;

import org.apache.shiro.authc.AuthenticationToken;

public class TrustedSsoAuthenticationToken implements AuthenticationToken {
	private static final long serialVersionUID = 2688200949686589407L;

	/**
	 * The username
	 */
	private String username;

	/**
	 * The password, in char[] format
	 */
	private String ssoTgt;

	/**
	 * Whether or not 'rememberMe' should be enabled for the corresponding login
	 * attempt;
	 * default is <code>false</code>
	 */
	private boolean rememberMe = false;

	/**
	 * The location from where the login attempt occurs, or <code>null</code> if
	 * not known or explicitly
	 * omitted.
	 */
	private String host;

	public TrustedSsoAuthenticationToken() {
	}
	
	public TrustedSsoAuthenticationToken(final String ssoTgt) {
		this(null, ssoTgt, false, null);
	}

	public TrustedSsoAuthenticationToken(final String username, final String ssoTgt) {
		this(username, ssoTgt, false, null);
	}

	public TrustedSsoAuthenticationToken(final String username, final String ssoTgt, final String host) {
		this(username, ssoTgt, false, host);
	}

	public TrustedSsoAuthenticationToken(final String username, final String ssoTgt, final boolean rememberMe) {
		this(username, ssoTgt, rememberMe, null);
	}

	public TrustedSsoAuthenticationToken(final String username, final String ssoTgt, final boolean rememberMe, final String host) {
		this.username = username;
		this.ssoTgt = ssoTgt;
		this.rememberMe = rememberMe;
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSsoTgt() {
		return ssoTgt;
	}

	public void setSsoTgt(String ssoTgt) {
		this.ssoTgt = ssoTgt;
	}

	/*
	 * 获取用户名
	 * @see org.apache.shiro.authc.AuthenticationToken#getPrincipal()
	 */
	public Object getPrincipal() {
		return getUsername();
	}

	/*
	 * 获取凭证、证书
	 * @see org.apache.shiro.authc.AuthenticationToken#getCredentials()
	 */
	public Object getCredentials() {
		return getSsoTgt();
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public boolean isRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(boolean rememberMe) {
		this.rememberMe = rememberMe;
	}

	public void clear() {
		this.username = null;
		this.ssoTgt = null;
		this.host = null;
		this.rememberMe = false;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(getClass().getName());
		sb.append(" - ");
		sb.append(username);
		sb.append(", rememberMe=").append(rememberMe);
		if (host != null) {
			sb.append(" (").append(host).append(")");
		}
		return sb.toString();
	}
}
