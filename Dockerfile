FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy file JAR đã build từ Maven
COPY target/ABCEnglish-3.3.3.jar /app/app.jar

EXPOSE 8080

# Không cần chỉ định cấu hình ngoài – Spring Boot tự đọc application.yml từ JAR
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
