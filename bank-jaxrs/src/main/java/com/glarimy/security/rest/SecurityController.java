package com.glarimy.security.rest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.DatatypeConverter;

import com.glarimy.security.api.Credentials;
import com.glarimy.security.api.Security;

@Path("/")
public class SecurityController {
	@Inject
	private Security security;

	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response authenticate(@Context HttpServletRequest request) {
		try {
			String authorization = request.getHeader("Authorization");
			if (authorization != null && authorization.startsWith("Basic")) {
				String header = authorization.substring("Basic".length()).trim();
				String decoded = new String(DatatypeConverter.parseBase64Binary(header));
				String[] creds = decoded.split(":");
				Credentials credentials = new Credentials();
				credentials.setUname(creds[0]);
				credentials.setPassword(creds[1]);
				security.authenticate(credentials);
				String token = security.key(credentials.getUname());
				return Response.ok().header(HttpHeaders.AUTHORIZATION, "Bearer " + token).build();
			}
			return Response.status(Status.UNAUTHORIZED).build();
		} catch (Exception e) {
			return Response.status(Status.UNAUTHORIZED).build();
		}
	}
}