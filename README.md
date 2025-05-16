# **HCMUTE FORUMS - DIỄN ĐÀN HCMUTE FORUMS**

## 👨‍💻 **Nhóm 54**

| Họ tên                              | MSSV         |
| ----------------------------------- | ------------ |
| Trần Trọng Nghĩa                    | 22110380     |
| Hồ Vũ Thanh Bình                    | 22110287     |

---

## 📌 **Giới Thiệu Dự Án**

**Mục đích dự án**
- Trong bối cảnh công nghệ thông tin phát triển mạnh mẽ, nhu cầu trao đổi thông tin và kết nối cộng đồng ngày càng tăng cao. Đề tài "Xây dựng diễn đàn HCMUTE Forums trên ứng dụng Android" được thực hiện nhằm tạo ra một nền tảng trực tuyến tiện lợi, hỗ trợ sinh viên, giảng viên và cựu sinh viên Trường Đại học Sư phạm Kỹ thuật TP.HCM (HCMUTE) chia sẻ kiến thức, thảo luận học thuật và kết nối cộng đồng. Ứng dụng được phát triển trên hệ điều hành Android, tích hợp các tính năng như đăng bài, bình luận, quản lý tài khoản và thông báo, mang đến trải nghiệm thân thiện và hiệu quả cho người dùng. Báo cáo này trình bày quá trình nghiên cứu, thiết kế và triển khai ứng dụng, đồng thời đánh giá hiệu quả và tiềm năng phát triển của diễn đàn trong tương lai.
---
**Công nghệ sử dụng**
Để xây dựng và phát triển ứng dụng ***HCMUTE FORUMS*** , nhóm sử dụng các công nghệ sau:
- **Android(Java)**:
  Nền tảng chính để xây dựng giao diện người dùng và xử lý các thao tác trên thiết bị di động.
- **Retrofit**:
  Thư viện của Android hỗ trợ thực hiện các yêu cầu mạng (HTTP), dùng để giao tiếp với hệ thống backend thông qua API.
- **SpringBoot(Java)**:
  Framework phía backend dùng để xây dựng các RESTful API, xử lý logic nghiệp vụ và truy xuất cơ sở dữ liệu.
- **MySQL**:
  Hệ quản trị cơ sở dữ liệu dùng để lưu trữ thông tin người dùng, topic, follow và các dữ liệu liên quan đến hệ thống.
- **Docker, Redis**:
  Docker và Redis dùng để deploy MySQL lên docker và redis giúp hổ trợ trong việc lưu trữ mã OTP

---

## ⚙️ **Cơ Chế Vận Hành**

**Hệ thống gồm 2 vai trò chính:**

- 👤 **USER (Người dùng)**

  - Đăng nhập, đăng ký, quên mật khẩu, đổi mật khẩu. 
  - Xem các bài viết, danh sách follower, danh sách following, xem trang cá nhân của người khác. 
  - Chỉnh sửa thông tin trang cá nhân.
  - Xem chi tiết một bài viết. 
  - Chỉnh sửa tài khoản cá nhân.
  - Like, Bình luận bài viết.
  - Follow người dùng khác
  - Xem thông báo khi có người khác bình luận hoặc like bài viết
- 🧑‍💼 **GUEST(Khách)**
  - Đăng ký tài khoản. 
  - Xem các bài viết, danh sách follower, danh sách following, xem trang cá nhân của người khác. 
---

## 🛠️ **Hướng Dẫn Setup Dự Án**

### 🔧 **1. Cài đặt ban đầu**
Cách 1: Tạo thư mục → Clone lần lượt 2 repo dự án từ Github
- API : [https://github.com/binh1922004/hcmuteforums/tree/main/backend](https://github.com/binh1922004/hcmuteforums/tree/main/backend)
- Android APP: [https://github.com/binh1922004/hcmuteforums/tree/main/android](https://github.com/binh1922004/hcmuteforums/tree/main/android)
  
Cách 2: Download source code của toàn bộ dự án được sinh viên gửi trong phần nộp dự án cuối kì, sau đó tiến hành giải nén 

### 🗄️ **2. Cài đặt cơ sở dữ liệu**

- Mở **MySQL Workbench**:
  - Tạo database tên `hcmuteforums`
  - Vào `Server → Data Import`
  - Chọn `Import from Dump Project Folder`
  - Chọn thư mục `db_hcmuteforums` trong thư mục đi kèm với dự án 

### 💻 **3. Mở và chạy project**

- Mở **Android Studio**:
  - `File → Open → chọn thư mục vừa clone app về( hoặc chọn thư mục chứa project nếu tải trực tiếp source code về) 
- Mở **IntelliJ IDEA**
  - `File → Open → chọn thư mục vừa clone phần API về( hoặc chọn thư mục chứa API cho dự án nếu tải trực tiếp source code về) 
  - Mở file `application.yaml`, chỉnh sửa phần cấu hình database:
    ```yaml
    username: <Tên người dùng MySQL>
    password: <Mật khẩu MySQL>
    ```
- **RUN** bấm run cả 2 bên ứng dụng để tiến hành chạy ứng dụng HCMUTEFORUMS. 

