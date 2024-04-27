package green_life;

import jakarta.mail.MessagingException;

public class MailMessage {
	public static void registrationSuccess(String emailId, String name) {
		String recipient = emailId;
		String subject = "Registration Successfull";
		String htmlTextMessage = "" + "<html>" + "<body>"
				+ "<h2 style='color:green;'>Welcome to Green Life</h2>" + "" + "Hi " + name + ","
				+ "<br><br>Congratulations on successfully creating your account. You're now part of our community, where shopping for your favorite products is just a click away..<br>"
				+ "Feel free to explore our wide range of products, exclusive deals, and convenient features tailored to make your shopping experience seamless."
				+ "<br>If you have any questions or need assistance, our support team is here to help. Just drop us a message at greenlife.notifications@gmail.com or give us a call at <b> 070 3654286 </b>.."
				+ "<br><br>Happy shopping! "
				+ "<br>Best regards, "
				+ "<br>Green Life Support Team"
				+ "Have a good day!<br>" + "" + "</body>" + "</html>";
		try {
			JavaMailUtil.sendMail(recipient, subject, htmlTextMessage);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
