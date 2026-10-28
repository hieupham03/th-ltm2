//Thông tin khách hàng được yêu cầu thay đổi định dạng lại cho phù hợp với khu vực, cụ thể:
//
//a. Tên khách hàng cần được chuẩn hóa theo định dạng mới.
//
//Ví dụ: nguyen van hai duong -> DUONG, Nguyen Van Hai
//
//b. Ngày sinh của khách hàng đang ở dạng mm-dd-yyyy, cần được chuyển thành định dạng dd/mm/yyyy.
//
//Ví dụ: 10-11-2012 -> 11/10/2012
//
//c. Tài khoản khách hàng được tạo từ các chữ cái in thường được sinh tự động từ họ tên khách hàng.
//
//Ví dụ: nguyen van hai duong -> nvhduong
//
//Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2209. Yêu cầu là xây dựng một chương trình client giao tiếp với server theo mô tả sau:
//
//a. Đối tượng trao đổi là thể hiện của lớp UDP.Customer được mô tả như sau
//
//Tên đầy đủ của lớp: UDP.Customer
//Các thuộc tính: id String, code String, name String, , dayOfBirth String, userName String
//Một Hàm khởi tạo với đầy đủ các thuộc tính được liệt kê ở trên
//Trường dữ liệu: private static final long serialVersionUID = 20151107;
//b. Client giao tiếp với server theo các bước
//
//Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng ;studentCode;qCode.
//Ví dụ: ;B15DCCN001;EE29C059
//
//Nhận thông điệp chứa: 08 byte đầu chứa chuỗi requestId, các byte còn lại chứa một đối tượng là thể hiện của lớp Customer từ server. Trong đó, các thuộc tính id, code, name,dayOfBirth đã được thiết lập sẵn.
//Yêu cầu thay đổi thông tin các thuộc tính như yêu cầu ở trên và gửi lại đối tượng khách hàng đã được sửa đổi lên server với cấu trúc: 08 byte đầu chứa chuỗi requestId và các byte còn lại chứa đối tượng Customer đã được sửa đổi.
package UDP;

import java.net.*;
import java.io.*;

class Customer implements Serializable {
    private static final long serialVersionUID = 20151107L;

    public String id, code, name, dayOfBirth, userName;

    public Customer(String id, String code, String name,
                    String dayOfBirth, String userName) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.dayOfBirth = dayOfBirth;
        this.userName = userName;
    }
}

public class Client2 {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress ip = InetAddress.getByName("36.50.135.242");
        int port = 2209;

        // 1. Gui
        String s = ";B23DCCN309;oQP8vAkj";
        byte[] data = s.getBytes();
        socket.send(new DatagramPacket(data, data.length, ip, port));

        // 2. Nhan
        byte[] buf = new byte[4096];
        DatagramPacket p = new DatagramPacket(buf, buf.length);
        socket.receive(p);

        String id = new String(p.getData(), 0, 8);

        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(
                        p.getData(), 8, p.getLength() - 8));
        Customer c = (Customer) ois.readObject();

        // 3. Chuan hoa ten

        String[] words = c.name.trim().toLowerCase().split("\\s+");

        String name = "";
        for (int i = 0; i < words.length - 1; i++) {
            String w = words[i];
            name += w.substring(0, 1).toUpperCase()
                    + w.substring(1) + " ";
        }

        String last = words[words.length - 1].toUpperCase();
        c.name = last + ", " + name.trim();


        // Doi ngay sinh mm-dd-yyyy -> dd/mm/yyyy
        String[] date = c.dayOfBirth.split("-");
        c.dayOfBirth = date[1] + "/" + date[0] + "/" + date[2];

        // Tao username: chu cai dau ho dem + ten
        String username = "";
        for (int i = 0; i < words.length - 1; i++) {
            username += words[i].charAt(0);
        }
        c.userName = username + words[words.length - 1];

        // 4. Gui lai object
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(c);
        oos.flush();

        byte[] obj = bos.toByteArray();
        byte[] result = new byte[8 + obj.length];

        System.arraycopy(p.getData(), 0, result, 0, 8);
        System.arraycopy(obj, 0, result, 8, obj.length);

        socket.send(new DatagramPacket(
                result, result.length, ip, port));

        System.out.println(c.name);
        System.out.println(c.dayOfBirth);
        System.out.println(c.userName);

        socket.close();
    }
}
