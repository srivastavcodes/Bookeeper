package com.project.bookbackend.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.mail.javamail.MimeMessageHelper.MULTIPART_MODE_MIXED;

@Service
public class EmailService {

	private final JavaMailSender mailSender;
	private final SpringTemplateEngine templateEngine;

	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	public EmailService(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
		this.mailSender = mailSender;
		this.templateEngine = templateEngine;
	}

	@Async
	public void sendEmail(
		String sendTo, String username, EmailTemplateName emailTemplate,
		String confirmationUrl, String activationCode, String subject
	) throws MessagingException {

		String templateName = (emailTemplate != null) ? emailTemplate.getTemplateName() : "confirm-email";

		MimeMessage mimeMessage = createMimeMessage(sendTo, subject);
		Map<String, Object> properties = createEmailProperties(
			username, confirmationUrl, activationCode
		);
		String template = generateEmailContent(templateName, properties);

		MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, MULTIPART_MODE_MIXED, UTF_8.name());
		messageHelper.setText(template, true);

		mailSender.send(mimeMessage);
	}

	private MimeMessage createMimeMessage(String sendTo, String subject) throws MessagingException {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper messageHelper = new MimeMessageHelper(
			mimeMessage, MULTIPART_MODE_MIXED, UTF_8.name()
		);
		messageHelper.setFrom("parthsrivastav.00@gmail.com");
		messageHelper.setTo(sendTo);
		messageHelper.setSubject(subject);
		return mimeMessage;
	}

	private Map<String, Object> createEmailProperties(
		String username, String confirmationUrl,
		String activationCode
	) {
		Map<String, Object> properties = new HashMap<>();
		properties.put("confirmationUrl", confirmationUrl);

		properties.put("username", username);
		properties.put("activationCode", activationCode);
		return properties;
	}

	private String generateEmailContent(String templateName, Map<String, Object> properties) {
		Context context = new Context();
		context.setVariables(properties);
		return templateEngine.process(templateName, context);
	}
}
