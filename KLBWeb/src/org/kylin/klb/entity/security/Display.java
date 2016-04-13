package org.kylin.klb.entity.security;

public class Display {
	private String value;
	private String display;
	
	public Display(String v, String d){
		value = v;
		display = d;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDisplay() {
		return display;
	}
	public void setDisplay(String display) {
		this.display = display;
	}
}
