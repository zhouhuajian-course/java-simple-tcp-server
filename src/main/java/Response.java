/**
 * 响应对象
 *
 * @author zhouhuajian
 * @version v1.0
 */
public class Response {

    private int result;
    private String message;

    public Response() {
    }

    public Response(int result, String message) {
        this.result = result;
        this.message = message;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        if (message == null) {
            message = "";
        }
        this.message = message;
    }

    public byte[] toBytes() {
        // 第一个字节 结果 第二个字节 消息的长度 剩余的字节消息
        byte[] messageBytes = message.getBytes();
        byte[] responseBytes = new byte[1 + 1 + messageBytes.length];
        responseBytes[0] = (byte) result;
        responseBytes[1] = (byte)messageBytes.length;
        for (int i = 2; i < responseBytes.length; i++) {
            responseBytes[i] = messageBytes[i - 2];
        }
        return responseBytes;
    }
}
