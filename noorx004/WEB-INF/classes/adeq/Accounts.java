package adeq;

import java.io.Serializable;

public class Accounts implements Serializable{

	private static final long serialVersionUID = 5372494205270028L;
		private String acctType;
		private String acctId;
		private double balance;
		private String custName;

	public Accounts(){
		balance = 0.00;
	}

	public void deposite(double amount){
		balance += amount;
	}
	
	public void withdraw(double amount){
		balance -= amount;
	}
	
	public double getBalance(){
		return balance;
	}

	public String getAcctId() {
		return acctId;
	}

	public void setAcctId(String id) {
		this.acctId=id;
	}

	public String getAcctType() {
		return acctType;
	}

	public void setAcctType(String acctType) {
		this.acctType = acctType;
	}

	public String getcustName() {
		return custName;
	}

	public void setcustName(String name) {
		custName = name;
	}

}
