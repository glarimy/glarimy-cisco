package com.glarimy.bank.rest;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.glarimy.bank.api.AccountNotFoundException;
import com.glarimy.bank.api.BankError;
import com.glarimy.bank.api.BankException;
import com.glarimy.bank.api.ErrorCodes;
import com.glarimy.bank.api.InsufficientBalanceException;
import com.glarimy.bank.api.InvalidCustomerException;

@Provider
public class BankExceptionMapper implements ExceptionMapper<BankException> {
	private static Map<String, String> messages = new HashMap<>();

	public BankExceptionMapper() {
		messages.put(ErrorCodes.NO_VALUE, "Missing required value");
		messages.put(ErrorCodes.INVALID_NAME, "Name is required and length must be between 3-32 characters");
		messages.put(ErrorCodes.INVALID_PHONE, "Phone is required and it must contain 12 digits");
		messages.put(ErrorCodes.INSUFFICIENT_BALANCE, "Account balance is insufficient");
		messages.put(ErrorCodes.NOT_FOUND, "Resource is not found");
	}

	public Response toResponse(BankException e) {
		if (e instanceof AccountNotFoundException) {
			return Response.status(404).entity(new BankError(e.getMessage(), messages.get(e.getMessage()))).build();
		}
		if (e instanceof InvalidCustomerException) {
			return Response.status(400).entity(new BankError(e.getMessage(), messages.get(e.getMessage()))).build();
		}
		if (e instanceof InsufficientBalanceException) {
			return Response.status(409).entity(new BankError(e.getMessage(), messages.get(e.getMessage()))).build();
		}
		return Response.status(500).build();
	}

}
