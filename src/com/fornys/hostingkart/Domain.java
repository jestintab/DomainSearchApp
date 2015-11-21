package com.fornys.hostingkart;

public class Domain {

	int _id;
	String _domain;

	// Empty constructor
	public Domain() {
	}

	// Constructor
	public Domain(int id, String domain_name) {
		this._id = id;
		this._domain = domain_name;
	}

	// Constructor
	public Domain(String domain_name) {
		this._domain = domain_name;
	}
	
	// Constructor
	public Domain(int id) {
		this._id = id;
	}

	// Getting id
	public int getID() {
		return this._id;
	}

	// Setting id
	public void setID(int id) {
		this._id = id;
	}

	// getting domain
	public String getDomain() {
		return this._domain;
	}

	// setting domain
	public void setDomain(String domain) {
		this._domain = domain;
	}

}
