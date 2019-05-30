package com.glarimy.bank.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.glarimy.bank.api.Account;
import com.glarimy.bank.api.AccountNotFoundException;
import com.glarimy.bank.api.Bank;
import com.glarimy.bank.api.BankException;
import com.glarimy.bank.api.Customer;
import com.glarimy.bank.api.InsufficientBalanceException;
import com.glarimy.bank.api.InvalidCustomerException;
import com.glarimy.bank.api.Transaction;

public class BankService implements Bank {

	private static Map<Integer, Account> accounts = new HashMap<>();
	private static int number = 0;

	@Override
	public Account openAccountFor(Customer customer) throws InvalidCustomerException, BankException {
		Account account = new Account();
		account.setCustomer(customer);
		account.setNumber(++number);
		accounts.put(account.getNumber(), account);
		return account;
	}

	@Override
	public Transaction doTransaction(int number, Transaction transaction)
			throws AccountNotFoundException, InsufficientBalanceException, BankException {
		Account account = accounts.get(number);
		if (transaction.getType().equalsIgnoreCase("deposit")) {
			account.setBalance(account.getBalance() + transaction.getAmount());
			transaction.setId(account.getTransactions().size() + 1);
			transaction.setDate(new Date());
			transaction.setBalance(account.getBalance());
			account.getTransactions().add(transaction);
		} else {
			account.setBalance(account.getBalance() - transaction.getAmount());
			transaction.setId(account.getTransactions().size() + 1);
			transaction.setDate(new Date());
			transaction.setBalance(account.getBalance());
			account.getTransactions().add(transaction);
		}
		return transaction;
	}

	@Override
	public double getBalance(int number) throws AccountNotFoundException, BankException {
		Account account = accounts.get(number);
		return account.getBalance();
	}

	@Override
	public List<Transaction> getTransactions(int number) throws AccountNotFoundException, BankException {
		Account account = accounts.get(number);
		return account.getTransactions();
	}

	@Override
	public Account getAccount(int number) throws AccountNotFoundException, BankException {
		Account account = accounts.get(number);
		return account;
	}

}
