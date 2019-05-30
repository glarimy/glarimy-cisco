package com.glarimy.bank.rest;

import java.io.InputStream;
import java.net.URI;
import java.util.List;

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
import javax.ws.rs.core.UriInfo;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.glarimy.bank.api.Account;
import com.glarimy.bank.api.Bank;
import com.glarimy.bank.api.BankException;
import com.glarimy.bank.api.Customer;
import com.glarimy.bank.api.Imager;
import com.glarimy.bank.api.Transaction;
import com.glarimy.bank.service.BankService;
import com.glarimy.bank.service.ImageService;

@Path("/")
public class BankController {
	private Bank bank = new BankService();
	private Imager imager = new ImageService();

	// curl -i -X POST -F "picture=@profile.png" -F "user=Krishna" -F
	// "phone=9731423166"
	// http://localhost:8080/bank/savings/customer

	@POST
	@Path("/customer")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	public Response openAccount(@FormDataParam("name") String name, @FormDataParam("phone") long phone,
			@FormDataParam("picture") InputStream is, @FormDataParam("picture") FormDataContentDisposition picture,
			@Context UriInfo uriInfo) {
		Customer customer = new Customer();
		customer.setName(name);
		customer.setPhone(phone);
		Account account = bank.openAccountFor(customer);
		imager.save(account.getNumber(), name, is);

		URI uri = uriInfo.getBaseUriBuilder().path("/account/" + account.getNumber()).build();
		return Response.created(uri).entity(account).build();
	}

	@POST
	@Path("/account/{number}/transaction")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response doTransaction(@PathParam("number") int number, Transaction transaction, @Context UriInfo uriInfo) {
		Transaction tx = bank.doTransaction(number, transaction);
		URI uri = uriInfo.getAbsolutePathBuilder().path("" + tx.getId()).build();
		return Response.created(uri).entity(tx).build();
	}

	@GET
	@Path("/account/{number}/customer/profile")
	@Produces({ "image/png" })
	public Response getProfilePic(@PathParam("number") int number) {
		return Response.ok(imager.fetch(number)).header("Content-Disposition", "attachment; filename=jersey.png")
				.build();
	}

	@GET
	@Path("/account/{number}/balance")
	@Produces({ MediaType.TEXT_PLAIN })
	public Response getBalance(@PathParam("number") int number) {
		double balance = bank.getBalance(number);
		return Response.ok(balance + "").build();
	}

	@GET
	@Path("/account/{number}/transaction")
	public Response getTransactions(@PathParam("number") int number) {
		List<Transaction> list = bank.getTransactions(number);
		return Response.ok(new GenericEntity<List<Transaction>>(list) {
		}).build();
	}

	@GET
	@Path("/account/{number}")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAccount(@PathParam("number") int number) {
		return Response.ok(bank.getAccount(number)).build();

	}
}
