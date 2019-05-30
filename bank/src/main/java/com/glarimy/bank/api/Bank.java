package com.glarimy.bank.api;

import java.util.List;

public interface Bank {

	Account openAccountFor(Customer customer) throws InvalidCustomerException, BankException;

	Transaction doTransaction(int number, Transaction transaction)
			throws AccountNotFoundException, InsufficientBalanceException, BankException;

	double getBalance(int number) throws AccountNotFoundException, BankException;

	List<Transaction> getTransactions(int number) throws AccountNotFoundException, BankException;

	Account getAccount(int number) throws AccountNotFoundException, BankException;

}