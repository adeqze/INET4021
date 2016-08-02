package adeq;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ViewHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		String currentUser=(String)session.getAttribute("currUser");	
		response.setContentType("text/HTML");
		out.println("<HTML>");
		out.println("<HEAD>");
		out.println("<TITLE>" + "Deposite Money" + "</TITLE>");
		out.println("</HEAD>");
		out.println("<BODY BGCOLOR = #00FFFF>");
		out.println("<CENTER>");
		out.println("<H4><FONT COLOR = 'red'>"+currentUser+" your recent activity is shown below: <BR><BR><FONT></H4>");
		out.println("<TABLE><TH></TH>");
		readLog(currentUser,response);
		out.println("</TABLE>");
		menu(response);
		out.println("</CENTER>");
		out.println("</BODY>");
		out.println("</HTML>");
		out.flush();
	}
	public void readLog(String userName, HttpServletResponse response) throws IOException{
		File logfile = new File("Logfile.txt");
		PrintWriter out = response.getWriter();		
		try {
			String line;
			BufferedReader br = new BufferedReader(new FileReader(logfile));
			while((line=br.readLine())!=null){
				if(line.contains(userName))
					out.println("<TR><TD> -> "+line+"<BR><BR></TR></TD>");
			}
			br.close();
		} catch (Exception e) {
			out.println("No Log Data Found");
		}
	}

	public void menu(HttpServletResponse response) throws IOException{
		PrintWriter out = response.getWriter();
		out.println("<CENTER><BR><BR>");
		out.println("<TABLE width = '900'>");
		out.println("<TH></TH><TH></TH><TH></TH><TH></TH><TH></TH><TH></TH>");
		out.println("<TR>");
		out.println("<BR><FORM METHOD='POST' ACTION='TransferMoney'>");
		out.println("<INPUT TYPE='Submit' NAME='banker' VALUE='Transfer'>");
		out.println("</FORM></BR>");
		out.println("<BR><FORM METHOD='POST' ACTION='WithdrawMoney'>");
		out.println("<INPUT TYPE='Submit' NAME='banker' VALUE='Withdraw'>");
		out.println("</FORM></BR>");
		out.println("<BR><FORM METHOD='POST' ACTION='DepositMoney'>");
		out.println("<INPUT TYPE='Submit' NAME='banker' VALUE='Deposite'>");
		out.println("</FORM></BR>");
		out.println("<BR><FORM METHOD='POST' ACTION='CloseAccount'>");
		out.println("<INPUT TYPE='Submit' NAME='banker' VALUE='Close Account'>");
		out.println("</FORM></BR>");
		out.println("<BR><FORM METHOD='POST' ACTION='OpenAccount'>");
		out.println("<INPUT TYPE='Submit' NAME='banker' VALUE='New Account'>");
		out.println("</FORM></BR>");
		out.println("<BR><FORM METHOD='POST' ACTION='Login.html'>");
		out.println("<INPUT TYPE='Submit' NAME='logout' VALUE='Log Out'>");
		out.println("</FORM></BR>");
		out.println("</TR>");
		out.println("</TABLE>");
		out.println("</CENTER>");

	}

}
