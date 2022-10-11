import controller.EmailController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务启动器
 *
 * @author zhouhuajian
 * @version v1.0
 */
public class SimpleServer {

    private static final EmailController emailController = new EmailController();

    private static final Logger log = LoggerFactory.getLogger(SimpleServer.class);

    public static void main(String[] args) throws IOException {
        try (ServerSocket ss = new ServerSocket(9000)) {
            log.info("Ready to accept connections!");
            while (true) {
                try (Socket s = ss.accept()) {
                    try (InputStream is = s.getInputStream()) {
                        int businessType = is.read();
                        Response r = new Response(1, "success");
                        try {
                            switch (businessType) {
                                // 发邮件
                                case 1:
                                    int titleLength = is.read();
                                    int contentLength = is.read();
                                    byte[] titleBytes = new byte[titleLength];
                                    byte[] contentBytes = new byte[contentLength];
                                    is.read(titleBytes);
                                    is.read(contentBytes);
                                    String title = new String(titleBytes);
                                    String content = new String(contentBytes);
                                    emailController.send(title, content);
                                    break;
                            }
                        } catch (Exception e) {
                            r.setResult(0);
                            // e.getMessage可能会是null空指针
                            r.setMessage(e.toString());
                        }
                        // 响应
                        try (OutputStream os = s.getOutputStream()) {
                            os.write(r.toBytes());
                        }
                        // 响应结果
                        System.out.println("result: " + r.getResult() + ", message: " + r.getMessage());
                    }
                }
            }
        }
    }

}
