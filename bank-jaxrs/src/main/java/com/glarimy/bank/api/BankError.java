package com.glarimy.bank.api;

public class BankError {
	private String code;
	private String message;

	public BankError() {

	}

	public BankError(String code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
