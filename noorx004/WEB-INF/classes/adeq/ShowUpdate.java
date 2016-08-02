package adeq;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.*;

public class ShowUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String currentUser=(String)session.getAttribute("currUser");	
		PrintWriter out = response.getWriter();
		response.setContentType("text/HTML");
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<TITLE>" + "Below Is Your Updated Account Information" + "</TITLE>");
		out.println("</HEAD>");
		out.println("<BODY BGCOLOR = #00FFFF>");
		if(session.getAttribute("banker").equals("Withdraw"))
			doWithdraw(response, request);
		else if(session.getAttribute("banker").equals("Deposite"))
			doDeposite(request);			
		else if(session.getAttribute("banker").equals("Close Account"))
			doClose(response, request);		
		else if(session.getAttribute("banker").equals("New Account"))
			doOpen(request);
		else if(session.getAttribute("banker").equals("Transfer"))
			doTransfer(response, request);
		custAcct(currentUser, response);
		menu(response);
		out.println("</BODY>");
		out.println("</HTML>");
		out.flush();

	}
	
	private void custAcct(String custName, HttpServletResponse response) throws FileNotFoundException, IOException{
		Vector<Accounts> custAcctVector= new Vector<Accounts>();
		ObjectInputStream custAcctRead = new ObjectInputStream(new FileInputStream("custAcct.so"));
		PrintWriter out = response.getWriter();
			while(true){
				try {
					custAcctVector.addElement((Accounts)custAcctRead.readObject());		
				} catch (Exception e) {
					custAcctRead.close();
					break;
				}
			}
			out.println("<CENTER>");
			out.println("<H4><FONT COLOR = 'red'>"+custName+" your updated account information is shown below<BR><BR><font></H4>");
			out.println("<TABLE BGCOLOR=E0E0E1 BORDER=1 width = '800'>");
			out.println("<TR>");
			out.println("<TH><FONT COLOR = 'blue'>Account ID  </FONT></TH>");
			out.println("<TH><FONT COLOR = 'blue'>Account Type  </FONT></TH>");
			out.println("<TH><FONT COLOR = 'blue'>Amount($)  </FONT></TH>");
			out.println("</TR><TR>");
			double total=0;
			for (Accounts accounts : custAcctVector) {
				out.println("<TR>");
				if(accounts.getcustName().equals(custName)){
				out.println("<TD><TD>" +accounts.getAcctId()+"</TD></TD>");
				out.println("<TD><TD>"+accounts.getAcctType()+"</TD></TD>");
				out.println("<TD><TD>" +accounts.getBalance()+"</TD></TD>");
				total+=accounts.getBalance();
				}
				out.println("</TR></TR>");
			}
			out.println("<TR>");
			out.println("<TD> </TD>");
			out.println("<TD><TD> Balance</TD></TD>");
			out.println("<TD>"+total+"</TD>");
			out.println("</TR>");
			out.println("</TABLE>");
			out.println("</CENTER>");
	}

	public void menu(HttpServletResponse response) throws IOException{
		PrintWriter out = response.getWriter();
		out.println("<CENTER><BR>");
		out.println("<TABLE width = '700'>");
		out.println("<TH></TH><TH></TH><TH></TH><TH></TH><TH></TH><TH></TH>");
		out.println("<TR>");
		out.println("<FORM METHOD='POST' ACTION='TransferMoney'>");
		out.println("<INPUT TYPE='Submit' NAME='banker' VALUE='Transfer'>");
		out.println("</FORM>");
		out.println("<TD><FORM METHOD='POST' ACTION='WithdrawMoney'>");
		out.println("<INPUT TYPE='Submit' NAME='banker' VALUE='Withdraw'>");
		out.println("</FORM></TD>");
		out.println("<TD><FORM METHOD='POST' ACTION='DepositMoney'>");
		out.println("<INPUT TYPE='Submit' NAME='banker' VALUE='Deposit'>");
		out.println("</FORM></TD>");
		out.println("<TD><FORM METHOD='POST' ACTION='CloseAccount'>");
		out.println("<INPUT TYPE='Submit' NAME='banker' VALUE='Close Account'>");
		out.println("</FORM></TD>");
		out.println("<TD><FORM METHOD='POST' ACTION='OpenAccount'>");
		out.println("<INPUT TYPE='Submit' NAME='banker' VALUE='New Account'>");
		out.println("</FORM></TD>");
		out.println("<TD><FORM METHOD='POST' ACTION='Login.html'>");
		out.println("<INPUT TYPE='Submit' NAME='logout' VALUE='Log Out'>");
		out.println("</FORM></TD>");
		out.println("<TD><FORM METHOD='POST' ACTION='ViewHistory'>");
		out.println("<INPUT TYPE='Submit' NAME='log' VALUE='View Log'>");
		out.println("</FORM></TD>");
		out.println("</TR>");
		out.println("</TABLE>");
		out.println("</CENTER>");

	}
	
	public void doWithdraw(HttpServletResponse response, HttpServletRequest request) throws IOException{
		HttpSession session = request.getSession();
		Logging log = new Logging();
		PrintWriter out = response.getWriter();
		File custAcctFile = new File("custAcct.so");
		Vector<Accounts> custAcctVector= new Vector<Accounts>();
		ObjectInputStream custAcctRead = new ObjectInputStream(new FileInputStream(custAcctFile));
		while(true){
				try {
					custAcctVector.addElement((Accounts)custAcctRead.readObject());		
				} catch (Exception e) {
					custAcctRead.close();
					break;
				}
			}
		FileOutputStream custAcctOutFile =  new FileOutputStream(custAcctFile);
		ObjectOutputStream custAcctWrite = new ObjectOutputStream(custAcctOutFile);
		String acct = request.getParameter("AccountType");
		String acctId = request.getParameter("AccountType").substring(0, 3)+request.getParameter("AccountID");
		double money = Double.parseDouble(request.getParameter("Amount"));
		String currentUser=(String)session.getAttribute("currUser");	
		for (Accounts accounts : custAcctVector) {
			if(accounts.getcustName().equals(currentUser) && accounts.getAcctType().equals(acct) && accounts.getAcctId().equals(acctId)){
				if(accounts.getBalance() < money)
					out.println("<CENTER><FONT COLOR=Red>Invalid Operation! Can't withdraw more money than you have</FONT></CENTER>");
				else	{
					accounts.withdraw(money);
					log.logaction(currentUser+" Withdrawn Money Of Amount "+request.getParameter("Amount")+" From "+acctId);
				}
				break;
			}
		}
		for (Accounts accounts : custAcctVector) {
			custAcctWrite.writeObject(accounts);
		}
		custAcctWrite.close();
	}

	public void doDeposite(HttpServletRequest request) throws IOException{
		HttpSession session = request.getSession();
		Logging log = new Logging();
		File custAcctFile = new File("custAcct.so");
		Vector<Accounts> custAcctVector= new Vector<Accounts>();
		ObjectInputStream custAcctRead = new ObjectInputStream(new FileInputStream(custAcctFile));
		while(true){
				try {
					custAcctVector.addElement((Accounts)custAcctRead.readObject());		
				} catch (Exception e) {
					custAcctRead.close();
					break;
				}
			}
		FileOutputStream custAcctOutFile =  new FileOutputStream(custAcctFile);
		ObjectOutputStream custAcctWrite = new ObjectOutputStream(custAcctOutFile);
		String acct = request.getParameter("AccountType");
		String acctId = request.getParameter("AccountType").substring(0, 3)+request.getParameter("AccountID");
		double money = Double.parseDouble(request.getParameter("Amount"));
		String currentUser=(String)session.getAttribute("currUser");	
		for (Accounts accounts : custAcctVector) {
			if(accounts.getcustName().equals(currentUser) && accounts.getAcctType().equals(acct) && accounts.getAcctId().equals(acctId)){
			{
				accounts.deposite(money);
				log.logaction(currentUser+" Deposited Money Of Amount "+request.getParameter("Amount")+" To "+acctId);
				break;
			}
			}
		}
		for (Accounts accounts : custAcctVector) {
			custAcctWrite.writeObject(accounts);
		}
		custAcctWrite.close();
	}

public void doClose(HttpServletResponse response, HttpServletRequest request) throws IOException{
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		File custAcctFile = new File("custAcct.so");
		Vector<Accounts> custAcctVector= new Vector<Accounts>();
		ObjectInputStream custAcctRead = new ObjectInputStream(new FileInputStream(custAcctFile));
		Logging log = new Logging();
		while(true){
				try {
					custAcctVector.addElement((Accounts)custAcctRead.readObject());		
				} catch (Exception e) {
					custAcctRead.close();
					break;
				}
			}
		FileOutputStream custAcctOutFile =  new FileOutputStream(custAcctFile);
		ObjectOutputStream custAcctWrite = new ObjectOutputStream(custAcctOutFile);
		String acct = request.getParameter("AccountType");
		String acctId = request.getParameter("AccountType").substring(0, 3)+request.getParameter("AccountID");
		String currentUser=(String)session.getAttribute("currUser");	
		for (Accounts accounts : custAcctVector) {
			if(accounts.getcustName().equals(currentUser) && accounts.getAcctType().equals(acct) && accounts.getAcctId().equals(acctId)){
				if(accounts.getBalance() == 0){
					custAcctVector.remove(accounts);
					log.logaction(currentUser+" Closed "+request.getParameter("AccountType")+" Account With Account ID " +request.getParameter("AccountType").substring(0, 3)+request.getParameter("AccountID"));
				}
				else	
					out.println("<CENTER><FONT COLOR=Red>Invalid Operation! Account caanot be closed. The amount is not zero </FONT></CENTER>");
				break;
			}
		}
		for (Accounts accounts : custAcctVector) {
			custAcctWrite.writeObject(accounts);
		}
		custAcctWrite.close();
	}

private void doOpen(HttpServletRequest request) throws IOException{
	Accounts custAcct = new Accounts();
	File custAcctFile = new File("custAcct.so");
	Logging log = new Logging();
	HttpSession session = request.getSession();
	String currentUser=(String)session.getAttribute("currUser");	
	custAcct.setcustName(currentUser);
	custAcct.setAcctId(request.getParameter("AccountType").substring(0, 3)+request.getParameter("AccountID"));
	custAcct.setAcctType(request.getParameter("AccountType"));
	double amount = Double.parseDouble(request.getParameter("Amount"));
	if(request.getParameter("AccountType").contains("Loa"))
	custAcct.deposite(-1*amount);
	else custAcct.deposite(amount);
	Vector<Accounts> custAcctVector= new Vector<Accounts>();
	ObjectInputStream custAcctRead = new ObjectInputStream(new FileInputStream(custAcctFile));
	while(true){
			try {
				custAcctVector.addElement((Accounts)custAcctRead.readObject());		
			} catch (Exception e) {
				custAcctRead.close();
				break;
			}
		}
	FileOutputStream custAcctOutFile =  new FileOutputStream(custAcctFile);
	ObjectOutputStream custAcctWrite = new ObjectOutputStream(custAcctOutFile);
	custAcctVector.addElement((Accounts)custAcct);
	for (Accounts accounts : custAcctVector) {
		custAcctWrite.writeObject(accounts);
	}
	custAcctWrite.close();
	log.logaction(currentUser+" Opened New "+request.getParameter("AccountType")+" Account With Account ID " +request.getParameter("AccountType").substring(0, 3)+request.getParameter("AccountID"));
}

public void doTransfer(HttpServletResponse response, HttpServletRequest request) throws IOException{
	//Deduct Money From Sender
	HttpSession session = request.getSession();
	PrintWriter out = response.getWriter();
	Logging log = new Logging();
	File custAcctFile = new File("custAcct.so");
	Vector<Accounts> custAcctVector= new Vector<Accounts>();
	ObjectInputStream custAcctRead = new ObjectInputStream(new FileInputStream(custAcctFile));
	while(true){
			try {
				custAcctVector.addElement((Accounts)custAcctRead.readObject());		
			} catch (Exception e) {
				custAcctRead.close();
				break;
			}
		}
	FileOutputStream custAcctOutFile =  new FileOutputStream(custAcctFile);
	ObjectOutputStream custAcctWrite = new ObjectOutputStream(custAcctOutFile);
	String acct = request.getParameter("senderAccountType");
	String acctId = request.getParameter("senderAccountType").substring(0, 3)+request.getParameter("senderAccountID");
	double money = Double.parseDouble(request.getParameter("senderAmount"));
	String currentUser=(String)session.getAttribute("currUser");	
	boolean approve=false;
	for (Accounts accounts : custAcctVector) {
		if(accounts.getcustName().equals(currentUser) && accounts.getAcctType().equals(acct) && accounts.getAcctId().equals(acctId)){
			if(accounts.getBalance() < money)
				out.println("<CENTER><FONT COLOR=Red>Invalid Operation! Can't withdraw more than you have</FONT></CENTER>");
			else {
				accounts.withdraw(money);
				approve = true;
				log.logaction(currentUser+" Transfered Money Of Amount "+request.getParameter("senderAmount")+" TO "+request.getParameter("receiverName"));
			}
			break;
		}
	}

	//Deposit Money To Receiver
	String receiverName = request.getParameter("receiverName");
	String receiverAcct = request.getParameter("receiverAccountType");
	String receiverAcctId = request.getParameter("receiverAccountType").substring(0, 3) + request.getParameter("receiverAccountID");
	if(approve){
	for (Accounts accounts : custAcctVector) {
		if(accounts.getcustName().equals(receiverName) && accounts.getAcctType().equals(receiverAcct) && accounts.getAcctId().equals(receiverAcctId)){
			accounts.deposite(money);
			break;
		}
	}
	}
	for (Accounts accounts : custAcctVector) {
		custAcctWrite.writeObject(accounts);
	}
	custAcctWrite.close();
	
	
}

}
