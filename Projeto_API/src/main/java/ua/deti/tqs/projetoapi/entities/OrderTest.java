package ua.deti.tqs.projetoapi.entities;

public class OrderTest {
	
	private String name;
	private String recipientContact;
	private String recipientAddress;
	private String category;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRecipientContact() {
		return recipientContact;
	}
	public void setRecipientContact(String recipientContact) {
		this.recipientContact = recipientContact;
	}
	public String getrecipientAddress() {
		return recipientAddress;
	}
	public void setAddress(String recipientAddress) {
		this.recipientAddress = recipientAddress;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	@Override
	public String toString() {
		return "OrderTest [name=" + name + ", RecipientContact=" + recipientContact + ", recipientAddress="
				+ recipientAddress + ", category=" + category + "]";
	}
	
	
	
	

}
