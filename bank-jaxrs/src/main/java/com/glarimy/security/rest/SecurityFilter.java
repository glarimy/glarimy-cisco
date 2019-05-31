package com.glarimy.security.rest;

import java.io.IOException;
import java.security.Principal;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import com.glarimy.security.api.Secured;
import com.glarimy.security.api.Security;

import io.jsonwebtoken.Claims;

@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class SecurityFilter implements ContainerRequestFilter {
	@Inject
	private Security security;
	@Context
	private UriInfo uriInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		try {
			String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
			String token = authorizationHeader.substring("Bearer".length()).trim();
			Claims claims = security.verify(token);
			requestContext.setSecurityContext(new SecurityContext() {

				@Override
				public boolean isUserInRole(String role) {
					return false;
				}

				@Override
				public boolean isSecure() {
					return uriInfo.getAbsolutePath().toString().startsWith("https");
				}

				@Override
				public Principal getUserPrincipal() {
					return new Principal() {

						@Override
						public String getName() {
							return claims.getId();
						}
					};
				}

				@Override
				public String getAuthenticationScheme() {
					return "Bearer Token";
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
		}
	}
}