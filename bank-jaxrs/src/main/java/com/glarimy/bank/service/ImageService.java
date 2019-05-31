package com.glarimy.bank.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import com.glarimy.bank.api.AccountNotFoundException;
import com.glarimy.bank.api.BankException;
import com.glarimy.bank.api.Imager;
import com.glarimy.bank.entities.Account;

public class ImageService implements Imager {

	private static String PATH = "/Users/glarimy/Professional/Workspace/";

	@Inject
	private EntityManagerFactory factory;

	@Override
	public void save(int number, InputStream stream) throws BankException {
		EntityManager em = factory.createEntityManager();
		try {
			Account account = em.find(Account.class, number);
			int read = 0;
			byte[] bytes = new byte[1024];

			OutputStream os = new FileOutputStream(new File(PATH + account.getCustomer().getName()));
			while ((read = stream.read(bytes)) != -1) {
				os.write(bytes, 0, read);
			}
			os.flush();
			os.close();
		} catch (Exception e) {
			throw new BankException(e);
		} finally {
			em.close();
		}
	}

	@Override
	public File fetch(int number) throws AccountNotFoundException, BankException {
		EntityManager em = factory.createEntityManager();
		Account account = em.find(Account.class, number);
		if (account == null)
			throw new AccountNotFoundException();
		try {
			return new File(PATH + account.getCustomer().getName());
		} catch (Exception e) {
			throw new BankException();
		}
	}

}
