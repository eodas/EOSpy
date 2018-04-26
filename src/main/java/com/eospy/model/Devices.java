package com.eospy.model;

/**
 * A POJO for devices
 */
public class Devices {

	public String name;
	public String id;
	public String action;
	public String status;

	public Devices(String name, String id) {
		this(name, id, "", "");
	}

	public Devices(String name, String id, String action, String status) {
		this.name = name;
		this.id = id;
		this.action = action;
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
