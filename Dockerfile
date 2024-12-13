FROM eclipse-temurin:17-jdk-alpine

# Tạo thư mục làm việc bên trong container
WORKDIR /app

# Sao chép file JAR từ thư mục target vào container
COPY target/ABCEnglish-0.0.1-SNAPSHOT.jar app.jar

# Cấu hình lệnh khởi động container
ENTRYPOINT ["java", "-jar", "app.jar"]

# Mở cổng 8080 để container lắng nghe kết nối
EXPOSE 8080
