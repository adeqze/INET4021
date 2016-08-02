package adeq;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.*;

public class DepositMoney extends HttpServlet {
	private static final long serialVersionUID = 1L;
        public DepositMoney() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		HttpSession banker = request.getSession();
		PrintWriter out = response.getWriter();
		String currentUser=(String)session.getAttribute("currUser");	
		response.setContentType("text/HTML");
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<TITLE>" + "Deposite Money" + "</TITLE>");
		out.println("</HEAD>");
		out.println("<BODY BGCOLOR = #00FFFF>");
		out.println("<CENTER>");
		out.println("<H4><FONT COLOR = 'red'>"+currentUser+" please specify the amount to be deposited<BR><BR><FONT></H4>");
		out.println("<FORM METHOD='POST' ACTION='ShowUpdate'>");
		out.println("<TABLE width = '700'><TH></TH>");
		out.println("<TR><TD><FONT COLOR = 'blue'>Account Type <select name='AccountType'> "
				+ "<option value='Saving'>Saving Account</option>"
				+ "<option value='Checking'>ChecKing Account</option>"
				+ "<option value='Business'>Business Account</option>"
				+ "</select> </FONT></TD></TR>");
		out.println("<TR><TD><TD><FONT COLOR = 'blue'>Account ID: <select name='AccountID'> "
				+ "<option value='111'>111</option> "
				+ "<option value='222'>222</option>"
				+ "<option value='333'>333</option>"
				+ "<option value='444'>444</option>"
				+ "<option value='555'>555</option>"
				+ "</select></FONT></TD></TD></TR>");
		out.println("<TR><TD><FONT COLOR = 'blue'>Deposit Amount : <INPUT TYPE=Text Name='Amount'></FONT></TD></TR>");
		out.println("</TABLE>");
		out.println("<INPUT TYPE='Submit' NAME='Deposit' VALUE='Submit'>");
		banker.setAttribute("banker", request.getParameter("banker"));	
		out.println("</FORM>");
		out.println("</CENTER>");
		out.println("</BODY>");
		out.println("</HTML>");
		out.flush();

	}

}
