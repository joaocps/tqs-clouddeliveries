package ua.deti.tqs.projetoapi.entities;

public class CommentTest {
	
	private int orderId;
	private String text;
	private int rating;
	
	
	public CommentTest(int orderId,int rating,  String text) {
		super();
		this.orderId = orderId;
		this.text = text;
		this.rating = rating;
	}
	
	
	
	public int getRating() {
		return rating;
	}


	public void setRating(int rating) {
		this.rating = rating;
	}



	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	

}
