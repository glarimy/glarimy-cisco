package com.glarimy.bank.service;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.glarimy.bank.api.AccountNotFoundException;
import com.glarimy.bank.api.Bank;
import com.glarimy.bank.api.BankException;
import com.glarimy.bank.api.ErrorCodes;
import com.glarimy.bank.api.InsufficientBalanceException;
import com.glarimy.bank.api.InvalidCustomerException;
import com.glarimy.bank.entities.Account;
import com.glarimy.bank.entities.Customer;
import com.glarimy.bank.entities.Transaction;

public class BankService implements Bank {
	@Inject
	private EntityManagerFactory factory;

	@Override
	public Account openAccountFor(Customer customer) throws InvalidCustomerException, BankException {
		if (customer == null)
			throw new InvalidCustomerException(ErrorCodes.NO_VALUE + "");
		if (customer.getName() == null || customer.getName().trim().length() == 0)
			throw new InvalidCustomerException(ErrorCodes.INVALID_NAME + "");
		if (customer.getPhone() < 500000000000L)
			throw new InvalidCustomerException(ErrorCodes.INVALID_PHONE + "");

		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();

		Account account = new Account();
		account.setCustomer(customer);
		em.persist(account);

		em.getTransaction().commit();
		em.close();

		return account;
	}

	@Override
	public Transaction doTransaction(int number, Transaction transaction)
			throws AccountNotFoundException, InsufficientBalanceException, BankException {
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();

		Account account = em.find(Account.class, number);
		if (account == null)
			throw new AccountNotFoundException();

		if (transaction.getType().equalsIgnoreCase("deposit")) {
			account.setBalance(account.getBalance() + transaction.getAmount());
			transaction.setDate(new Date());
			transaction.setBalance(account.getBalance());
			account.getTransactions().add(transaction);
		} else {
			if (account.getBalance() < transaction.getAmount())
				throw new InsufficientBalanceException();

			account.setBalance(account.getBalance() - transaction.getAmount());
			transaction.setDate(new Date());
			transaction.setBalance(account.getBalance());
			account.getTransactions().add(transaction);
		}

		em.getTransaction().commit();
		em.close();

		return transaction;
	}

	@Override
	public double getBalance(int number) throws AccountNotFoundException, BankException {
		EntityManager em = factory.createEntityManager();

		Account account = em.find(Account.class, number);
		if (account == null)
			throw new AccountNotFoundException();

		em.close();

		return account.getBalance();
	}

	@Override
	public List<Transaction> getTransactions(int number) throws AccountNotFoundException, BankException {
		EntityManager em = factory.createEntityManager();

		Account account = em.find(Account.class, number);
		if (account == null)
			throw new AccountNotFoundException();

		em.close();

		return account.getTransactions();
	}

	@Override
	public Account getAccount(int number) throws AccountNotFoundException, BankException {
		EntityManager em = factory.createEntityManager();

		Account account = em.find(Account.class, number);
		if (account == null)
			throw new AccountNotFoundException();

		em.close();

		return account;
	}

}
