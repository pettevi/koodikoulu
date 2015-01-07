package com.koodikoulu;

import java.io.IOException;
import java.util.Date;
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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@SuppressWarnings("serial")
public class RegisterJ extends HttpServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		System.out.println("get");
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

        String name = req.getParameter("name");
        String age = req.getParameter("age");
        String parentname = req.getParameter("parentname");
        String email = req.getParameter("email");
        String message = req.getParameter("message");
        String event = req.getParameter("event");
        String group = req.getParameter("group");
        
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        
        boolean allOK = false;
        
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("pete.hamalainen@gmail.com", "www.koodioulu.com"));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress("jani.vallirinne@gmail.com", ""));
            msg.setSubject("Ilmoittautuminen koodikouluun " + event);
            msg.setText("\n\nNimi: " + name + "\nIkä: " + age + "\nHuoltajan nimi: " + parentname + "\nEmail: " + email + "\nRyhmä: " + group + "\nViesti: " + message);
            Transport.send(msg);
            allOK = true;
        } catch (AddressException e) {
            // ...
        } catch (MessagingException e) {
            // ...
        }
		Key key = KeyFactory.createKey("Koodioulu", event);
		Entity person = new Entity("person", key);

		person.setProperty("event", event);
		person.setProperty("date", new Date());
		person.setProperty("kidname", name);
		person.setProperty("age", age);
		person.setProperty("email", email);
		person.setProperty("message", message);
		
		// save to datastore
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		datastore.put(person);
        
        // respond to query
		// client side forwarding
		if (!allOK)
		{
			resp.sendRedirect("/?error=1");
		}
		else
		{
			resp.sendRedirect("/ilmoittaudu.html");
		}
	}

}
