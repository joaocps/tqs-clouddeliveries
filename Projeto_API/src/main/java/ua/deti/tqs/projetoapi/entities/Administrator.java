package ua.deti.tqs.projetoapi.entities;

import javax.persistence.Entity;

@Entity
public class Administrator extends User{
	
	private int token;
	
	public Administrator(){}
	
	public Administrator(String email, String password){
		super(email, password);
	}

	public int getToken() {
		return token;
	}

	public void setToken(int token) {
		this.token = token;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + token;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Administrator other = (Administrator) obj;			
		return (token != other.token) ? false : true;
	}

	@Override
	public String toString() {
		return "Administrator [token=" + token + "]";
	}
	
	
	

}
