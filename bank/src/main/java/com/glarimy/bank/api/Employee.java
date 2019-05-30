package com.glarimy.bank.api;

public class Employee {
	private String eid;
	private String name;
	private long phone;

	public Employee() {

	}

	public Employee(String eid, String name, long phone) {
		super();
		this.eid = eid;
		this.name = name;
		this.phone = phone;
	}

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getPhone() {
		return phone;
	}

	public void setPhone(long phone) {
		this.phone = phone;
	}

}
