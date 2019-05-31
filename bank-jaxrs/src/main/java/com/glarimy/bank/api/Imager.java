package com.glarimy.bank.api;

import java.io.File;
import java.io.InputStream;

public interface Imager {

	public void save(int number, InputStream stream) throws BankException;

	public File fetch(int number) throws AccountNotFoundException, BankException;

}