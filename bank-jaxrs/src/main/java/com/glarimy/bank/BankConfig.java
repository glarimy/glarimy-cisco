package com.glarimy.bank;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;

@ApplicationPath("/savings")
public class BankConfig extends ResourceConfig {
    public BankConfig() {
        register(new BankBinder());
        packages(true, "com.glarimy.bank");
    }
}