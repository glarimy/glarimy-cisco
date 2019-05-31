package com.glarimy.security.api;

import io.jsonwebtoken.Claims;

public interface Security {
	public Credentials create(String uname) throws SecurityException;
	public boolean authenticate(Credentials credentials) throws SecurityException;
	public String key(String uname) throws SecurityException;
	public Claims verify(String key) throws SecurityException;
}
