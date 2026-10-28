//Một chương trình server cho phép kết nối qua giao thức UDP tại cổng 2208. Yêu cầu là xây dựng một chương trình client tương tác với server kịch bản dưới đây:
//
//a. Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng ;studentCode;qCode.
//
//Ví dụ: ;B15DCCN001;EE29C059
//
//b. Nhận thông điệp từ server theo định dạng requestId; data
//
//requestId là một chuỗi ngẫu nhiên duy nhất
//data là chuỗi dữ liệu đầu vào cần xử lý
//Ví dụ: requestId;Qnc8d5x78aldSGWWmaAAjyg3
//
//c. Tìm kiếm ký tự xuất hiện nhiều nhất trong chuỗi và gửi lên server theo định dạng “requestId;ký tự xuất hiện nhiều nhất: các vị trí xuất hiện ký tự đó”
//
//Ví dụ: requestId;8:4,9,
//
//d. Đóng socket và kết thúc chương trình
import java.net.*;

public class String2{
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress ip = InetAddress.getByName("36.50.135.242");
        int port = 2208;

        // 1. Gui
        String s = ";B23DCCN309;pImPFnAv";
        byte[] data = s.getBytes();
        socket.send(new DatagramPacket(data, data.length, ip, port));

        // 2. Nhan
        byte[] buf = new byte[4096];
        DatagramPacket p = new DatagramPacket(buf, buf.length);
        socket.receive(p);

        String res = new String(p.getData(), 0, p.getLength());
        String[] a = res.split(";", 2);
        String id = a[0];
        String str = a[1];

        // 3. Tim ky tu xuat hien nhieu nhat
        char c = str.charAt(0);
        int max = 0;

        for (int i = 0; i < str.length(); i++) {
            int dem = 0;
            for (int j = 0; j < str.length(); j++) {
                if (str.charAt(i) == str.charAt(j))
                    dem++;
            }
            if (dem > max) {
                max = dem;
                c = str.charAt(i);
            }
        }

        // Tim cac vi tri xuat hien
        String result = c + ":";
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == c)
                result += i+1 + ",";
        }

        // 4. Gui ket qua
        s = id + ";" + result;
        data = s.getBytes();
        socket.send(new DatagramPacket(data, data.length, ip, port));

        System.out.println(s);
        socket.close();
    }
}
