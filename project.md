# HƯỚNG DẪN SỬ DỤNG GIT

#### Trước khi bắt đầu

- Trước tiên cần fork project về tài khoản của mình
- Clone project từ tài khoản của mình về máy
- Tạo nhánh mới từ nhánh main

#### Trong quá trình làm việc

- Rebase nhánh của bạn với nhánh main
- Lập trình → Commit + Push theo nhánh → Tạo Pull Request

---

# QUY ƯỚC TẠO PULL REQUEST

1. Pull Request phải mô tả rõ ràng nội dung thay đổi.
2. Nếu Pull Request liên quan đến `ADMIN/ USER` thì phải thêm từ khóa `[ADMIN]`/`[USER]` vào nội dung Pull Request.
3. Mỗi Pull Request chỉ có 1 commit.
3. Tiêu đề Pull Request phải bắt đầu bằng một trong các từ khóa sau:

```
- Add: Thêm chức năng mới
- Update: Cập nhật chức năng
- Fix: Sửa lỗi
- Remove: Xóa chức năng
- Refactor: Tối ưu mã nguồn
- Style: Thay đổi về style
- Test: Thêm hoặc cập nhật test
- Docs: Thêm hoặc cập nhật tài liệu
```

#### Nội dung của Pull Request tuân theo định dạng sau:

```
#### TASK ĐÃ THỰC HIỆN

- [TaskID](Link-task)

#### NỘI DUNG THAY ĐỔI (tùy chọn)

- Thực hiện những chức năng gì?

#### LÝ DO THAY ĐỔI (tùy chọn)

- Tại sao thay đổi?
- Lợi ích của thay đổi?
- Có những vấn đề gì cần lưu ý?
- Có những vấn đề gì cần giải quyết?
- Có những vấn đề gì cần thảo luận?

#### MINH HOẠ (Ảnh chụp màn hình hoặc Video)

- Ảnh chụp màn hình hoặc Video về chức năng đã thực hiện
```

---

# THIẾT LẬP MÔI TRƯỜNG

1. Cài đặt JDK 11
2. Cài đặt SDK 19
3. Tạo file .env trong thư mục gốc của project với nội dung như sau:

```
MAIL_USERNAME=...
MAIL_PASSWORD=...
MAIL_HOST=...
MAIL_PORT=...

DATABASE_URL=...
DATABASE_NAME=...
DATABASE_PORT=...
DATABASE_USERNAME=...
DATABASE_PASSWORD=...
DATABASE_DRIVER=...

HIBERNATE_DIALECT=...
```

---

# QUY ƯỚC VIẾT CODE

1. Tên biến, tên hàm, tên lớp phải viết bằng tiếng Anh, không dấu, không viết tắt.
2. Tên biến, tên hàm, tên lớp phải viết theo kiểu CamelCase.
3. Tên biến, tên hàm, tên lớp phải mô tả rõ nghĩa.
4. Tên biến, tên hàm, tên lớp không được viết tắt.
5. Cuối mỗi tệp Java phải có một dòng trống.
6. Sử dụng cách đặt tên biến và hàm theo quy ước của Java (ví dụ: getXXX() cho phương thức getter, isXXX() cho phương
   thức boolean, vv).
7. Sử dụng tiếng Anh trong các chú thích (comment) và đặt tên biến, hàm, lớp.
8. Tuân thủ quy tắc viết hoa chữ cái đầu cho tên lớp và viết thường chữ cái đầu cho tên biến và hàm.

---

# QUY ƯỚC COMMIT

1. Commit message phải mô tả rõ ràng nội dung thay đổi.
2. Commit message phải viết hoa chữ cái đầu.
3. Commit message không được viết tắt.
4. Commit message phải viết bằng tiếng Anh.
5. Commit message phải có độ dài tối đa 50 ký tự.
6. Commit message phải bắt đầu bằng một trong các từ khóa sau:

```
- Add: Thêm chức năng mới
- Update: Cập nhật chức năng
- Fix: Sửa lỗi
- Remove: Xóa chức năng
- Refactor: Tối ưu mã nguồn
- Style: Thay đổi về style
- Test: Thêm hoặc cập nhật test
- Docs: Thêm hoặc cập nhật tài liệu
```

---

# CODING CONVENTION

1. Sử dụng 4 dấu cách để thụt lề.
2. Mỗi dòng code không được quá 120 ký tự.
3. Mỗi dòng code không được chứa quá 1 phép gán.
4. Mỗi dòng code không được chứa quá 1 phép gọi hàm.
