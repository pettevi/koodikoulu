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
public class RegisterKid extends HttpServlet {

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
        String tel = req.getParameter("tel");
        String message = req.getParameter("message");
        String event = req.getParameter("event");
        String group = req.getParameter("group");
        
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        boolean allOK = false;
        
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("pete.hamalainen@gmail.com", "Petteri Hamalainen")); // pakko olla pete.hamalainen@gmail.com
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress("petteri_hamalainen@yahoo.com", "petteri_hamalainen@yahoo.com"));
            msg.setSubject("Ilmoittautuminen " + event);
            msg.setText("\n\nNimi: " + name + "\nIk�: " + age + "\nHuoltajan nimi: " + parentname + "\nEmail: " + email + "\nPuh: " + tel + "\nRyhm�: " + group + "\nViesti: " + message);
            Transport.send(msg);
            allOK = true;
        } catch (AddressException e) {
            // ...
        } catch (MessagingException e) {
            // ...
        }

        if (allOK)
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress("pete.hamalainen@gmail.com", "Petteri Hamalainen")); // pakko olla pete.hamalainen@gmail.com
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
            msg.setSubject("Ilmoittautuminen " + event);
            msg.setText("Hei,\n\nT�m� on automaattinen vahvistus. Olen saanut ilmoittautumisen t�st� s�hk�postiosoitteesta " + 
              "alla olevilla tiedoilla. Jos haluatte kysy� jotain voitte vastata t�h�n viestiin, palaan asiaan niin pian kuin voin. \n\nTerveisin, Petteri \n\nNimi: " +
              name + "\nIk�: " + age + "\nHuoltajan nimi: " + parentname + "\nEmail: " + email + "\nViesti: " + message);
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
			//resp.sendRedirect("/ilmoittaudu.html");
		}
	}

}
