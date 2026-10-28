//Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2207. Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản:
//
//a. Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng ;studentCode;qCode.
//
//Ví dụ: ;B15DCCN001;DC73CA2E
//
//b. Nhận thông điệp là một chuỗi từ server theo định dạng requestId;a1,a2,...,a50
//
//requestId là chuỗi ngẫu nhiên duy nhất
//a1 -> a50 là 50 số nguyên ngẫu nhiên
//c. Thực hiện tìm giá trị lớn nhất và giá trị nhỏ nhất thông điệp trong a1 -> a50 và gửi thông điệp lên lên server theo định dạng requestId;max,min
//
//d. Đóng socket và kết thúc chương trình
import java.net.*;

public class Data2 {
    public static void main(String[] args) throws Exception{
        DatagramSocket socket = new DatagramSocket();
        InetAddress ip = InetAddress.getByName("36.50.135.242");
        int port = 2207;

        String s = ";B23DCCN309;Niil2X3d";
        byte[] data = s.getBytes();
        socket.send(new DatagramPacket(data, data.length, ip, port));

        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);

        String[] a =  new String(packet.getData(), 0, packet.getLength()).split(";");
        String id = a[0];

        String[] nums = a[1].split(",");

        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;

        for (String x : nums) {
            int n = Integer.parseInt(x.trim());
            max = Math.max(max, n);
            min = Math.min(min, n);
        }

        // 4. Gửi kết quả
        s = id + ";" + max + "," + min;
        data = s.getBytes();
        socket.send(new DatagramPacket(data, data.length, ip, port));

        socket.close();


    }
}
