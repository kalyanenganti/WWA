package com.tab.wwa;
// Basic Employee bean with setters and getters for the required state
public class Employee {
	private String name;
	private String department;
	private String biography;
	private String photo;
	
	public Employee(String name,String department,String biography,String photo){
		this.name=name;
		this.department=department;
		this.biography=biography;
		this.photo=photo;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getBiography() {
		return biography;
	}
	public void setBiography(String biography) {
		this.biography = biography;
	}
	public String getPhoto() {
		return photo;
	}
	public void setPhoto(String photo) {
		this.photo = photo;
	}
	

}
