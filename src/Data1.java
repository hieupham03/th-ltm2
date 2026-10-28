//Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2207. Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản:
//
//a. Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng ;studentCode;qCode.
//
//Ví dụ: ;B15DCCN001;73457A17
//
//b. Nhận thông điệp là một chuỗi từ server theo định dạng requestId;n;A1,A2,...An , với
//
//requestId là chuỗi ngẫu nhiên duy nhất
//n là một số ngẫu nhiên nhỏ hơn 100.
//A1, A2 ... Am (m <= n) là các giá trị ngẫu nhiên nhỏ hơn hoặc bằng n và có thể trùng nhau.
//Ví dụ: requestId;10;2,3,5,6,5
//
//c. Tìm kiếm các giá trị còn thiếu và gửi lên server theo định dạng requestId;B1,B2,...,Bm
//
//Ví dụ: requestId;1,4,7,8,9,10
//
//d. Đóng socket và kết thúc chương trình.
import java.net.*;
import java.util.*;

public class Data1 {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress ip = InetAddress.getByName("36.50.135.242");
        int port = 2207;

        // 1. Gửi mã sinh viên và mã câu hỏi
        String s = ";B23DCCN309;Q2VENG9c";
        byte[] data = s.getBytes();
        socket.send(new DatagramPacket(data, data.length, ip, port));

        // 2. Nhận dữ liệu
        byte[] buf = new byte[4096];
        DatagramPacket p = new DatagramPacket(buf, buf.length);
        socket.receive(p);

        String[] a = new String(p.getData(), 0, p.getLength()).split(";");
        String id = a[0];
        int n = Integer.parseInt(a[1]);

        // 3. Tìm số thiếu
        Set<Integer> set = new HashSet<>();
        for (String x : a[2].split(","))
            set.add(Integer.parseInt(x.trim()));

        String result = "";
        for (int i = 1; i <= n; i++)
            if (!set.contains(i))
                result += i + ",";

        if (!result.isEmpty())
            result = result.substring(0, result.length() - 1);

        // 4. Gửi kết quả
        s = id + ";" + result;
        data = s.getBytes();
        socket.send(new DatagramPacket(data, data.length, ip, port));

        socket.close();
    }
}
