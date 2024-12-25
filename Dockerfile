FROM eclipse-temurin:17-jre-alpine

# Tạo thư mục làm việc bên trong container
WORKDIR /app

# Sao chép file JAR vào container
COPY target/ABCEnglish-3.3.3.jar /app/app.jar

# Thiết lập các biến môi trường
ENV DB_HOST=abcEnglish.mssql.somee.com
ENV DB_NAME=abcEnglish
ENV DB_USERNAME=MinTrees_SQLLogin_1
ENV DB_PASSWORD=12345678
ENV MAIL_USERNAME=npminhtri.be@gmail.com
ENV MAIL_PASSWORD=cfubhnaxnbdzsnta
ENV JWT_SIGNER_KEY="1TjXchw5FloESb63Kc+DFhTARvpWL4jUGCwfGWxuG5SIf/1y/LgJxHnMqaF6A/ij"
ENV JWT_VALID_DURATION=3600
ENV JWT_REFRESHABLE_DURATION=36000
ENV VNPAY_TMN_CODE=FM2YJ74Z
ENV VNPAY_HASH_SECRET=M2NXGKZG9WNZ0X8HSRX56TFO6XMWCL75
ENV VNPAY_URL=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
ENV VNPAY_RETURN_URL=http://localhost:8080/payment/vnpay_return

# Cấu hình lệnh khởi động container
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

# Mở cổng 8080 để container lắng nghe kết nối
EXPOSE 8080
