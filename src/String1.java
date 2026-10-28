
import java.net.*;

public class String1 {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress ip = InetAddress.getByName("36.50.135.242");
        int port = 2208;

        // 1. Gửi
        String s = ";B23DCCN309;CAhvynFC";
        byte[] data = s.getBytes();
        socket.send(new DatagramPacket(data, data.length, ip, port));

        // 2. Nhận
        byte[] buf = new byte[4096];
        DatagramPacket p = new DatagramPacket(buf, buf.length);
        socket.receive(p);

        String res = new String(p.getData(), 0, p.getLength());
        String[] a = res.split(";", 2);
        String id = a[0];
        String str = a[1];

        // 3. Tìm ký tự xuất hiện nhiều nhất
        String[] words = str.trim().toLowerCase().split("\\s+");
        String result = "";

        for (String w : words) {
            result += w.substring(0, 1).toUpperCase()
                    + w.substring(1) + " ";
        }

        result = result.trim();
        // 4. Gửi kết quả
        s = id + ";" + result;
        data = s.getBytes();
        socket.send(new DatagramPacket(data, data.length, ip, port));
        socket.close();
    }
}
