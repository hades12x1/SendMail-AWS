# Project SendMail-AWS
Project sử dụng Springboot(Java).
# Purpose
Chịu trách nhiệm gửi mail qua Amazone SES:
- Đọc message từ queue.
- Gửi email tới client với nội dung message đó.
# Project Details
- Đọc message từ config queue: `activemq.job.mail-queue`.
- Hoặc nhận message từ API.
- Thực hiện gửi tới client qua Amazone SES.
- Nếu sử dụng call API: `http://localhost:8882/sent-mail`
     + Sử dụng basic auth: Với username và password trong cấu hình file 
       application.properties: `authen.basic.username`  và `authen.basic.password` (Chưa phân theo role)
- Cấu trúc message cần nhận: 
    ```
    {
            "from" : "chuyenns@eway.vn",
            "from_name" : "chuyenns",
            "to" : ["leng8068@gmail.com"],
            "subject" : "tesstttt", 
            "body" : "<html><head></head> <body> <h1>Hello chuyenns!</h1> </body> </html>",
            "attachments" : [
                "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRuqZCgY99HnUaPe1a4IhLxiSuX-yE-1AvFkA&usqp=CAU"
            ]
    }
- Trường: 
    + from: không có sẽ được gán giá trị mặc định theo config trong project: `mail.client.default.from`
    + from_name: không có sẽ được gán trị mặc định theo config trong project: `mail.client.default.name`
    + to(require)
    + subject(require)
    + body: type có thể là text hoặc text/html
    + attachments: là 1 mảng tuỳ chọn link uri của file(Chỉ hỗ trợ link online)
   
# Getting Started
- Set up environment: Java 8, Gradle.
- Clone code.
- Config lại file /resource/application.properties các uri cho việc kết nối.
- Development IDE: IntelliJ.

## Build
- Build trên terminal: `gradle clean build`

#Author
```
Name: Nguyen Chuyen - Chuyenns
Email: chuyenns@eway.vn
```
#Acknowledgments
```
Java core
Sringboot
ActiveMQ
```
