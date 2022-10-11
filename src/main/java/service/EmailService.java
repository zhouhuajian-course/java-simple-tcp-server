package service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static javax.mail.Message.RecipientType.TO;

/**
 * 邮件业务
 *
 * @author zhouhuajian
 * @version v1.0
 */
public class EmailService {

    public boolean send(String title, String content) {
        InputStream is = EmailService.class.getClassLoader().getResourceAsStream("email.properties");
        Properties emailProperties = new Properties();
        try {
            emailProperties.load(is);
        } catch (IOException e) {
            throw new RuntimeException("email configuration can't be loaded!");
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.qq.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "false");
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        emailProperties.getProperty("email.username"),
                        emailProperties.getProperty("email.password"));
            }
        });
        session.setDebug(true);
        try {
            MimeMessage msg = new MimeMessage(session);
            // 发件人
            msg.setFrom(new InternetAddress(emailProperties.getProperty("email.sender")));
            // 收件人
            msg.addRecipients(TO, emailProperties.getProperty("email.receiver"));
            msg.setSubject(title);
            msg.setContent(content, "text/plain;charset=utf-8");
            // 发送邮件
            Transport.send(msg);
        } catch (MessagingException e) {
            throw new RuntimeException("fail to send the email!");
        }
        return true;
    }

}
