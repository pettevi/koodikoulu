package com.koodikoulu;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Contact extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		System.out.println("get");
		
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String message = req.getParameter("message");

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        
        boolean allOK = false;
        
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("pete.hamalainen@gmail.com", "www.koodioulu.com"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress("pete.hamalainen@gmail.com", ""));
            msg.setSubject("Viesti www.koodioulu.com");
            msg.setText("\n\nNimi: " + name + "\nEmail: " + email + "\nViesti: " + message);
            Transport.send(msg);
            allOK = true;
        } catch (AddressException e) {
            // ...
        } catch (MessagingException e) {
            // ...
        }
        
		// respond to query
		// client side forwarding
		if (!allOK)
		{
			resp.sendRedirect("/?error=1");
		}
		else
		{
			resp.sendRedirect("/otayhteytta.html");
		}
	}

}
