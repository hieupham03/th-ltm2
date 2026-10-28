//Một chương trình server cho phép giao tiếp qua giao thức UDP tại cổng 2209. Yêu cầu là xây dựng một chương trình client trao đổi thông tin với server theo kịch bản sau: Đối tượng trao đổi là thể hiện của lớp UDP.Student được mô tả:
//
//Tên đầy đủ lớp: UDP.Student
//Các thuộc tính: id String,code String, name String, email String
//02 Hàm khởi tạo:
//public Student(String id, String code, String name, String email)
//public Student(String code)
//Trường dữ liệu: private static final long serialVersionUID = 20171107
//Thực hiện:
//
//a. Gửi thông điệp là một chuỗi chứa mã sinh viên và mã câu hỏi theo định dạng ;studentCode;qCode.
//
//Ví dụ: ;B15DCCN001;EE29C059
//
//b. Nhận thông điệp chứa: 08 byte đầu chứa chuỗi requestId, các byte còn lại chứa một đối tượng là thể hiện của lớp Student từ server. Trong đó, các thông tin được thiết lập gồm id và name.
//
//c. Yêu cầu:
//
//Chuẩn hóa tên theo quy tắc: Chữ cái đầu tiên in hoa, các chữ cái còn lại in thường và gán lại thuộc tính name của đối tượng
//Tạo email ptit.edu.vn từ tên người dùng bằng cách lấy tên và các chữ cái bắt đầu của họ và tên đệm.
//Ví dụ: nguyen van tuan nam -> namnvt@ptit.edu.vn. Gán giá trị này cho thuộc tính email của đối tượng nhận được
//
//Gửi thông điệp chứa đối tượng xử lý ở bước c lên Server với cấu trúc: 08 byte đầu chứa chuỗi requestId và các byte còn lại chứa đối tượng Student đã được sửa đổi.
//d. Đóng socket và kết thúc chương trình.
package UDP;

import java.net.*;
import java.io.*;

class Student implements Serializable {
    private static final long serialVersionUID = 20171107L;

    public String id, code, name, email;

    public Student(String id, String code, String name, String email) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.email = email;
    }

    public Student(String code) {
        this.code = code;
    }
}

public class Client {
    public static void main(String[] args) throws Exception {
        DatagramSocket socket = new DatagramSocket();
        InetAddress ip = InetAddress.getByName("36.50.135.242");
        int port = 2209;

        // 1. Gui
        String s = ";B23DCCN309;mPpblyCf";
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
        Student st = (Student) ois.readObject();

        // 3. Chuan hoa ten
        String[] words = st.name.trim().toLowerCase().split("\\s+");
        String name = "";

        for (String w : words) {
            name += w.substring(0, 1).toUpperCase()
                    + w.substring(1) + " ";
        }
        st.name = name.trim();

        // Tao email
        String email = words[words.length - 1];
        for (int i = 0; i < words.length - 1; i++) {
            email += words[i].charAt(0);
        }
        st.email = email + "@ptit.edu.vn";

        // 4. Gui lai object
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(st);
        oos.flush();

        byte[] obj = bos.toByteArray();
        byte[] result = new byte[8 + obj.length];

        System.arraycopy(p.getData(), 0, result, 0, 8);
        System.arraycopy(obj, 0, result, 8, obj.length);

        socket.send(new DatagramPacket(
                result, result.length, ip, port));

        System.out.println(st.name);
        System.out.println(st.email);

        socket.close();
    }
}
