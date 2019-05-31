package com.glarimy.bank.rest;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.glarimy.bank.api.Bank;
import com.glarimy.bank.api.Imager;
import com.glarimy.bank.entities.Account;
import com.glarimy.bank.entities.Customer;
import com.glarimy.bank.entities.Transaction;
import com.glarimy.security.api.Credentials;
import com.glarimy.security.api.Secured;
import com.glarimy.security.api.Security;

@Path("/")
@Consumes({ MediaType.APPLICATION_JSON })
@Produces({ MediaType.APPLICATION_JSON })
public class BankController {
	@Context
	SecurityContext securityContext;

	@Inject
	private Bank bank;

	@Inject
	private Security security;

	@Inject
	private Imager imager;

	@POST
	@Path("/customer")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	public Response openAccountFor(@FormDataParam("name") String name, @FormDataParam("phone") long phone,
			@FormDataParam("picture") InputStream is, @FormDataParam("picture") FormDataContentDisposition picture,
			@Context UriInfo uriInfo) {
		Customer customer = new Customer();
		customer.setName(name);
		customer.setPhone(phone);
		Credentials credentials = security.create(customer.getName());
		Account account = bank.openAccountFor(customer);
		imager.save(account.getNumber(), is);
		URI uri = uriInfo.getBaseUriBuilder().path("/account/" + account.getNumber()).build();
		return Response.created(uri).entity(credentials).build();
	}

	@POST
	@Path("/account/{number}/transaction")
	@Secured
	public Response doTransaction(@PathParam("number") int number, Transaction transaction, @Context UriInfo uriInfo) {
		System.out.println(securityContext.getUserPrincipal().getName() + " initiated a transaction");
		Transaction tx = bank.doTransaction(number, transaction);
		URI uri = uriInfo.getAbsolutePathBuilder().path("" + tx.getId()).build();
		return Response.created(uri).entity(tx).build();
	}

	@GET
	@Path("/account/{number}/customer/profile")
	@Produces({ "image/png" })
	@Secured
	public Response getProfilePic(@PathParam("number") int number) {
		System.out.println(securityContext.getUserPrincipal().getName() + " initiated a transaction");
		return Response.ok(imager.fetch(number)).header("Content-Disposition", "attachment; filename=jersey.png")
				.build();
	}

	@GET
	@Path("/account/{number}/balance")
	@Produces({ MediaType.TEXT_PLAIN })
	@Secured
	public Response getBalance(@PathParam("number") int number) {
		System.out.println(securityContext.getUserPrincipal().getName() + " request for fetch balance");
		double balance = bank.getBalance(number);
		return Response.ok(balance + "").build();
	}

	@GET
	@Path("/account/{number}/transaction")
	@Secured
	public Response getTransactions(@PathParam("number") int number) {
		System.out.println(securityContext.getUserPrincipal().getName() + " requested for fetch transactions");
		List<Transaction> list = bank.getTransactions(number);
		return Response.ok(new GenericEntity<List<Transaction>>(list) {
		}).build();
	}

	@GET
	@Path("/account/{number}")
	@Secured
	public Response getAccount(@PathParam("number") int number) {
		System.out.println(securityContext.getUserPrincipal().getName() + " requested for fetch account");
		return Response.ok(bank.getAccount(number)).build();
	}
}