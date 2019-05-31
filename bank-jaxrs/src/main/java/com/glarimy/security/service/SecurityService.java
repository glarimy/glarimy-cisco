package com.glarimy.security.service;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.xml.bind.DatatypeConverter;

import com.glarimy.bank.api.SecurityException;
import com.glarimy.security.api.Credentials;
import com.glarimy.security.api.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class SecurityService implements Security {
	@Inject
	private EntityManagerFactory factory;
	private static String SECRET_KEY = "lksaetue9r857wogakhng";

	@Override
	public Credentials create(String uname) {
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		Credentials credentials = em.find(Credentials.class, uname);
		if (credentials != null)
			throw new SecurityException();
		credentials = new Credentials();
		credentials.setUname(uname.toLowerCase());
		credentials.setPassword(new Date().getTime() + "");
		em.persist(credentials);
		em.getTransaction().commit();
		return credentials;
	}

	@Override
	public boolean authenticate(Credentials credentials) {
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		Credentials saved = em.find(Credentials.class, credentials.getUname());
		if (saved == null)
			throw new SecurityException();
		if (saved.getPassword().equals(credentials.getPassword()))
			return true;
		return false;
	}

	@Override
	public String key(String uname) throws java.lang.SecurityException {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		JwtBuilder builder = Jwts.builder().setId(uname).setIssuedAt(now).setSubject("GTS").setIssuer("Glarimy")
				.signWith(signatureAlgorithm, signingKey);

		long expMillis = nowMillis + 360000;
		Date exp = new Date(expMillis);
		builder.setExpiration(exp);

		return builder.compact();
	}

	@Override
	public Claims verify(String key) throws java.lang.SecurityException {
		Claims claims = Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY)).parseClaimsJws(key)
				.getBody();
		return claims;

	}

}
