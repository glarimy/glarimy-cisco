package com.glarimy.bank;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.glassfish.hk2.utilities.binding.AbstractBinder;

import com.glarimy.bank.api.Bank;
import com.glarimy.bank.api.Imager;
import com.glarimy.bank.service.BankService;
import com.glarimy.bank.service.ImageService;
import com.glarimy.security.api.Security;
import com.glarimy.security.service.SecurityService;

public class BankBinder extends AbstractBinder {
	@Override
	protected void configure() {
		bind(SecurityService.class).to(Security.class);
		bind(BankService.class).to(Bank.class);
		bind(ImageService.class).to(Imager.class);
		bind(Persistence.createEntityManagerFactory("bank")).to(EntityManagerFactory.class);
	}
}
