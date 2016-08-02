package adeq;

import java.io.*;
import java.util.*;
import java.util.Vector;
import javax.servlet.*;
import javax.servlet.http.*;

public class AccountsBalance extends HttpServlet {
 	private static final long serialVersionUID = 165489L;


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession customer = request.getSession();
		String userName = request.getParameter("FirstName");
		String passWord = request.getParameter("PassWord");
		customer.setAttribute("currUser", userName);	
		Vector<Client> custProfileVector= new Vector<Client>();
		ObjectInputStream custProfileRead = new ObjectInputStream(new FileInputStream("custProfile.so"));
		PrintWriter out = response.getWriter();
		boolean found = false;
		try {
			while(true){
				try {
					custProfileVector.addElement((Client)custProfileRead.readObject());		
				} catch (Exception e) {
					break;
				}
			}
			//custProfileVector.addElement((Client)custProfileRead.readObject());		
			response.setContentType("text/HTML");
			out.println("<HTML>");
			out.println("<HEAD>");
			out.println("<TITLE>" + "Account Balance Information" + "</TITLE>");
			out.println("</HEAD>");
			out.println("<BODY BGCOLOR = #00FFFF>");
			for (Client client : custProfileVector) {
				if(client.getFirstN().equals(userName) && client.getpassWord().equals(passWord)){
					found=true;
					break;
				}
				else found = false;
			}
			if(found){
				custAcct(userName, response);
				menu(response);
			}
			else {
				out.println("<CENTER><FONT COLOR = 'red'>Apologies, Username and password cannot be recognized! <FONT><BR>");
				out.println("<a href='NewUser.html'>Open Account!</a><BR>");
				out.println("<a href='Login.html'>Try Again</a></CENTER>");
			}
			out.println("</BODY>");
			out.println("</HTML>");
			out.flush();
		    } catch (Exception e) {
			out.println("No Bank Account Record was found");
			custProfileRead.close();
		}

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
			out.println("<H4><FONT COLOR = 'red'>"+custName+" your account summary is listed below<BR><BR><FONT></H4>");
			out.println("<TABLE BGCOLOR=E0E0E1 BORDER=1 width = '900'>");
			out.println("<TR>");
			out.println("<TH><FONT COLOR = 'blue'>Account ID</FONT></TH>");
			out.println("<TH><FONT COLOR = 'blue'>Account Type</FONT></TH>");
			out.println("<TH><FONT COLOR = 'blue'>Amount($)</FONT></TH>");
			out.println("</TR>");
			double total=0;
			for (Accounts accounts : custAcctVector) {
				out.println("<TR>");
				if(accounts.getcustName().equals(custName)){
				out.println("<TD>" +accounts.getAcctId()+"</TD>");
				out.println("<TD>"+accounts.getAcctType()+"</TD>");
				out.println("<TD>" +accounts.getBalance()+"</TD>");
				total+=accounts.getBalance();
				}
				out.println("</TR>");
			}
			out.println("<TR>");
			out.println("<TD> </TD>");
			out.println("<TD> Balance</TD>");
			out.println("<TD>"+total+"</TD>");
			out.println("</TR>");
			out.println("</TABLE width = '900'>");
			out.println("</CENTER>");
	}
	
public void menu(HttpServletResponse response) throws IOException{
	PrintWriter out = response.getWriter();
	out.println("<CENTER><BR>");
	out.println("<TABLE width='700'>");
	out.println("<TH></TH><TH></TH><TH></TH><TH></TH><TH></TH><TH></TH>");
	out.println("<TR>");
	out.println("<font><FORM METHOD='POST' ACTION='TransferMoney'>");
	out.println("<INPUT TYPE='Submit' NAME='banker' VALUE='Transfer'>");
	out.println("</FORM>");
	out.println("<FORM METHOD='POST' ACTION='WithdrawMoney'>");
	out.println("<INPUT TYPE='Submit' NAME='banker' VALUE='Withdraw'>");
	out.println("</FORM>"); 
	out.println("<FORM METHOD='POST' ACTION='DepositMoney'>");
	out.println("<INPUT TYPE='Submit' NAME='banker' VALUE='Deposit'>");
	out.println("</FORM>");
	out.println("<FORM METHOD='POST' ACTION='CloseAccount'>");
	out.println("<INPUT TYPE='Submit' NAME='banker' VALUE='Close Account'>");
	out.println("</FORM>");//to this point. 
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
	
}

