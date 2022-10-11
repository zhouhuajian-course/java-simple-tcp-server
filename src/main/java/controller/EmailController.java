package controller;

import service.EmailService;

/**
 * 邮件控制器
 *
 * @author zhouhuajian
 * @version v1.0
 */
public class EmailController {

    /**
     * 邮件业务
     */
    private EmailService emailService = new EmailService();

    /**
     * 发送邮件
     */
    public boolean send(String title, String content) {
        return emailService.send(title, content);
    }

}
