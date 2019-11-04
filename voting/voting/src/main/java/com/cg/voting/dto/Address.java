/**
 * 
 */
package com.cg.voting.dto;

import javax.persistence.Embeddable;

/**
 * @author Swanand
 *
 */
@Embeddable
public class Address {

	private String area;
	private String city;
	private String pincode;
	private String state;
	
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		return "Address [area=" + area + ", city=" + city + ", pincode=" + pincode + ", state=" + state + "]";
	}
	
}
