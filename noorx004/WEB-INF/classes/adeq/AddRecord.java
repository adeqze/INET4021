package adeq;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class AddRecord extends HttpServlet {
	private static final long serialVersionUID = 3765187372670760L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
	}	
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		Accounts custAcct = new Accounts();		
		Logging log = new Logging();
		Client custProfile = new Client();
		HttpSession customer = request.getSession();
		//customer profile
		custProfile.setFirstN(request.getParameter("FirstName"));
		custProfile.setLastN(request.getParameter("LastName"));
		custProfile.setpassWord(request.getParameter("PassWord"));
		//customer account info
		custAcct.setcustName(request.getParameter("FirstName"));
		custAcct.setAcctId(request.getParameter("AccountType").substring(0, 3)+request.getParameter("AccountID"));
		custAcct.setAcctType(request.getParameter("AccountType"));
		double amount = Double.parseDouble(request.getParameter("Amount"));
		if(custAcct.getAcctType().contains("Sec"))
		custAcct.deposite(-1*amount);
		else custAcct.deposite(amount);
		customer.setAttribute("currUser", request.getParameter("FirstName"));	
		//Write to File and Respond to user
		writeToFile(custAcct,custProfile);
		log.logaction(request.getParameter("FirstName")+" Created "+request.getParameter("AccountType")+" Account With Account ID " +request.getParameter("AccountType").substring(0, 3)+request.getParameter("AccountID"));
		response.setContentType("text/HTML");
		PrintWriter out = response.getWriter();
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<TITLE>" + "Registration Information" + "</TITLE>");
		out.println("</HEAD>");
		out.println("<BODY BGCOLOR = #00FFFF>");
		out.println("<CENTER>");
		out.println("<H3><FONT COLOR = 'red'> Your Account Has Been Created!! <FONT></H3>");
		out.println("<H3><FONT COLOR = 'red'> Congrats now you have a bank account! Hopefully you put in a lot of money!<FONT><H3>");
		out.println("<TABLE width = '800'>");
		out.println("<TR><TH><TH><TH><TH></TR>");
		out.println("<TR><TD><FONT COLOR = 'blue'> User Name:</FONT> "+custProfile.getFirstN()+"</TD></TR>");
		out.println("<TR><TD> </TD></TR>");
		out.println("<TR><TD><FONT COLOR = 'blue'> Account Type:</FONT> "+custAcct.getAcctType()+"</TD></TR>");
		out.println("<TR><TD><FONT COLOR = 'blue'> Account Id:</FONT> "+ custAcct.getAcctId()+"</TD></TR>");
		out.println("<TR><TD><FONT COLOR = 'blue'> Begining Balance:</FONT> " +custAcct.getBalance()+"</TD></TR>");
		out.println("<TR><TD> </TD></TR><TR><TD> </TD></TR><TR><TD> </TD></TR>");
		out.println("<TR>");
		out.println("<TD><FORM METHOD='POST' ACTION='Login.html'>");
		out.println("<INPUT TYPE='Submit' NAME='login' VALUE='Login'>");
		out.println("</FORM></TD>");
		out.println("<TD><FORM METHOD='POST' ACTION='OpenAccount'>");
		out.println("<INPUT TYPE='Submit' NAME='banker' VALUE='New Account'>");
		out.println("</FORM></TD>");
		out.println("</TR>");
		out.println("</TABLE >");
		out.println("</CENTER>");
		out.println("</BODY>");
		out.println("</HTML>");
		out.flush();
	}
	
	private void writeToFile(Accounts custAcct, Client custProfile) throws IOException{
		File custAcctFile = new File("custAcct.so");
		File custProfileFile = new File("custProfile.so");
		//create two random access files to hold acct and customer info
		FileOutputStream custAcctOutFile =  new FileOutputStream(custAcctFile,true);
		FileOutputStream custProfileOutFile =  new FileOutputStream(custProfileFile,true);
		//write objects to file
		if(custAcctFile.length()==0 && custProfileFile.length()==0){
		ObjectOutputStream custAcctWrite = new ObjectOutputStream(custAcctOutFile);
		ObjectOutputStream custProfileWrite = new ObjectOutputStream(custProfileOutFile);
		custAcctWrite.writeObject(custAcct);
		custProfileWrite.writeObject(custProfile);
		custAcctWrite.close();;
		custProfileWrite.close();;
		}else{
		AppendObjectOutputStream custAcctWrite = new AppendObjectOutputStream(custAcctOutFile);
		AppendObjectOutputStream custProfileWrite = new AppendObjectOutputStream(custProfileOutFile);
		custAcctWrite.writeObject(custAcct);
		custProfileWrite.writeObject(custProfile);
		custAcctWrite.close();;
		custProfileWrite.close();;
		}
		
	}
	}

 class AppendObjectOutputStream extends ObjectOutputStream {

	  public AppendObjectOutputStream(OutputStream os) throws IOException {
	    super(os);
	  }

	  @Override
	  protected void writeStreamHeader() throws IOException {
	    reset();
	  }

	}
