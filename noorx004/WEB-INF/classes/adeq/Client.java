package adeq;

import java.io.Serializable;
import java.util.*;

public class Client implements Serializable{

private static final long serialVersionUID = -2284079786212849611L;
private String custID;
private String firstN; 
private String lastN;
private String acctID; 
private String acctType;
private String passWord;

public Client(){
	
}

public String getCustID() {
	return custID;
}

public void setCustID(String custID) {
	this.custID = custID;
}

public String getFirstN() {
	return firstN;
}

public void setFirstN(String firstN) {
	this.firstN = firstN;
}

public String getLastN() {
	return lastN;
}

public void setLastN(String lastN) {
	this.lastN = lastN;
}

public String getpassWord() {
	return passWord;
}

public void setpassWord(String passWord) {
	this.passWord = passWord;
}

public String getacctID() {
	return acctID;
}

public void setacctID(String id) {
	acctID = id;
}

public String getacctType() {
	return acctType;
}

public void setacctType(String type) {
	acctType = type;
}


}
