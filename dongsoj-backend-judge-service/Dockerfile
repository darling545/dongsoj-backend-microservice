# 基础镜像
FROM openjdk:8-jdk-alpine

# 指定工作目录
WORKDIR /app

# 将jar包复制到工作目录当中，本地的工作jar路径
ADD target/dongsoj-backend-judge-service-0.0.1-SNAPSHOT.jar .

# 暴露端口
EXPOSE 8104

# 启动容器时执行的命令
ENTRYPOINT ["java", "-jar", "/app/dongsoj-backend-judge-service-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]
