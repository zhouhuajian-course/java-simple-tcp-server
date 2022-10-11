import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * 简单服务器的测试
 *
 * @author zhouhuajian
 * @version v1.0
 */
public class SimpleServerTest {

    private Socket s;

    @BeforeEach
    public void beforeEach() throws IOException {
        s = new Socket("127.0.0.1", 9000);
    }

    @ParameterizedTest
    @CsvSource({
            "title1, content2",
            "title2, content2",
            "title3, content3"
    })
    public void testSendEmail(String title, String content) throws IOException {
        byte[] titleBytes = title.getBytes();
        byte[] contentBytes = content.getBytes();
        byte[] requestBytes = new byte[1 + 1 + 1 + titleBytes.length + contentBytes.length];
        requestBytes[0] = 1;
        requestBytes[1] = (byte) titleBytes.length;
        requestBytes[2] = (byte) contentBytes.length;
        for (int i = 3; i < requestBytes.length; i++) {
            if (i - 3 < titleBytes.length) {
                requestBytes[i] = titleBytes[i - 3];
            } else {
                requestBytes[i] = contentBytes[i - 3 - titleBytes.length];
            }
        }
        OutputStream os = s.getOutputStream();
        os.write(requestBytes);

        // 结果
        InputStream is = s.getInputStream();
        // 第一个字节结果
        int result = is.read();
        // 第二个字节 消息长度
        int messageLength = is.read();
        // 剩余的字节是消息
        byte[] messageBytes = new byte[messageLength];
        is.read(messageBytes);
        String message = new String(messageBytes);

        s.shutdownOutput();
        s.shutdownInput();
        System.out.println("result: " + result + ", message: " + message);
        Assertions.assertEquals(result, 1);
        Assertions.assertEquals(message, "success");
    }

    @AfterEach
    public void afterEach() throws IOException {
        s.close();
    }

}
