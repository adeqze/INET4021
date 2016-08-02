package adeq;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.*;

public class TransferMoney extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		HttpSession banker = request.getSession();
		PrintWriter out = response.getWriter();
		String currentUser=(String)session.getAttribute("currUser");	
		response.setContentType("text/HTML");
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<TITLE>" + "Transfer Money" + "</TITLE>");
		out.println("</HEAD>");
		out.println("<BODY BGCOLOR = #00FFFF>");
		out.println("<CENTER>");
		out.println("<H2><FONT COLOR = 'red'>"+currentUser+" provide transfer information <FONT></H2><BR>");
		out.println("<H4><FONT COLOR = 'green'>Informatoin of transferer!</FONT></H4>");
		out.println("<FORM METHOD='POST' ACTION='ShowUpdate'>");
		out.println("<TABLE><TH></TH>");
		out.println("<TR>");
		out.println("<TABLE width = '700' BGCOLOR=E0E0E0><TH></TH>");
		out.println("<TR><TD><H5><FONT COLOR = 'red'>Please select the account and amount to be transferred<FONT></H5> </TD></TR>");
		out.println("<TR><TD><FONT COLOR = 'blue'>Account Type <select name='senderAccountType'> "
				+ "<option value='Saving'>Saving Account</option>"
				+ "<option value='Checking'>ChecKing Account</option>"
				+ "<option value='Business'>Business Account</option>"
				+ "</select> </FONT></TD></TR>");
		out.println("<TR><TD><TD><FONT COLOR = 'blue'>Account ID: <select name='senderAccountID'> "
				+ "<option value='111'>111</option> "
				+ "<option value='222'>222</option>"
				+ "<option value='333'>333</option>"
				+ "<option value='444'>444</option>"
				+ "<option value='555'>555</option>"
				+ "</select></FONT><TD></TD></TR>");
		out.println("<TR><TD><FONT COLOR = 'blue'>Transfer Amount: <INPUT TYPE=Text Name='senderAmount'></FONT></TD></TR>");
		out.println("</TABLE><BR><BR>");
		out.println("</TR>");
		out.println("<TR></TR>");
		//Receiver Account
		out.println("<TR>");
		out.println("<TABLE width = '700' BGCOLOR=E0E0E0><TH></TH>");
		out.println("<TR><TD><H5><FONT COLOR = 'red'> Please specify the information of transfer receiver<FONT></H5> </TD></TR>");
		out.println("<H4><FONT COLOR = 'green'>Informatoin of receiver!</FONT></H4>");
		out.println("<TR><TD><TD><FONT COLOR = 'blue'>Reciever Name: <INPUT TYPE=Text Name='receiverName'></FONT></TD><TD></TR>");
		out.println("<TR><TD><FONT COLOR = 'blue'>Account Type <select name='receiverAccountType'> "
				+ "<option value='Saving'>Saving Account</option>"
				+ "<option value='Checking'>ChecKing Account</option>"
				+ "<option value='Second'>Second Saving</option>"
				+ "</select> </FONT></TD></TR>");
		out.println("<TR><TD><FONT COLOR = 'blue'>Account ID: <select name='receiverAccountID'> "
				+ "<option value='111'>111</option> "
				+ "<option value='222'>222</option>"
				+ "<option value='333'>333</option>"
				+ "<option value='444'>444</option>"
				+ "<option value='555'>555</option>"
				+ "</select></FONT></TD></TR>");
		out.println("</TABLE>");
		out.println("</TR>");
		out.println("</TABLE>");
		out.println("<BR><INPUT TYPE='Submit' NAME='Transfer' VALUE='Submit'>");
		banker.setAttribute("banker", request.getParameter("banker"));	
		out.println("</FORM>");
		out.println("</CENTER>");
		out.println("</BODY>");
		out.println("</HTML>");
		out.flush();

	
	}

}
