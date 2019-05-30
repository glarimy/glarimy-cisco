package com.glarimy.bank.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.glarimy.bank.api.AccountNotFoundException;
import com.glarimy.bank.api.BankException;
import com.glarimy.bank.api.Imager;

public class ImageService implements Imager {

	private static String PATH = "/Users/glarimy/Professional/Engineering/Workspace/images/";
	private static Map<Integer, String> images = new HashMap<>();

	@Override
	public void save(int number, String name, InputStream stream) throws BankException {
		try {
			int read = 0;
			byte[] bytes = new byte[1024];

			OutputStream os = new FileOutputStream(new File(PATH + name));
			while ((read = stream.read(bytes)) != -1) {
				os.write(bytes, 0, read);
			}
			os.flush();
			os.close();
			images.put(number, name);
		} catch (Exception e) {
			throw new BankException();
		}
	}

	@Override
	public File fetch(int number) throws AccountNotFoundException, BankException {
		String name = images.get(number);
		if (name == null)
			throw new AccountNotFoundException();
		try {
			return new File(PATH + name);
		} catch (Exception e) {
			throw new BankException();
		}
	}

}
