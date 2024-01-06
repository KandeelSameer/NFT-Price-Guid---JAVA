package application;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import javax.mail.Transport;

import javax.mail.internet.InternetAddress;

import javax.mail.internet.MimeMessage;

import Model.emailDataAuth;





public class EmailApi {
	 private emailDataAuth emailData;
     private static EmailApi instance;
     
     private EmailApi() {
    	 emailData=new emailDataAuth();
     }
     //singleton
     public static EmailApi getInstance() {
    	 if(instance==null) {
    		 instance=new EmailApi();
    		 
    	 }
    	 return instance;
     }
     public void sendMail(Set<String> emails,String text,String subject) {

       

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "outlook.office365.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(emailData.getUsername(), emailData.getPassword());
            }
          });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("ayman.gazal.java@outlook.com"));
            for(String s:emails)
            message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(s));
            message.setSubject(subject);
            message.setText(text);

            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}