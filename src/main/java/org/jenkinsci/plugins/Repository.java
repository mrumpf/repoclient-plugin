package org.jenkinsci.plugins;

public class Repository {
	private String name;
	private String username;
	private String password;
	private String baseurl;
	
	public Repository() {
	}
	public Repository(String name, String baseurl, String username, String password) {
		this.name = name;
		this.baseurl = baseurl;
		this.username = username;
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public String getUsername() {
		return username;
	}
	public String getBaseurl() {
		return baseurl;
	}
	public void setBaseurl(String baseurl) {
		this.baseurl = baseurl;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(Repository.class.getSimpleName());
		sb.append('[');
		sb.append("name=");
		sb.append(name);
		sb.append("baseurl=");
		sb.append(baseurl);
		sb.append("username=");
		sb.append(username);
		sb.append("password=********");
		sb.append(']');
		return sb.toString();
	}
}
