# 基础镜像
FROM openjdk:8-jdk-alpine

# 指定工作目录
WORKDIR /app

# 将jar包放到工作目录当中
ADD target/dongsoj-backend-gateway-0.0.1-SNAPSHOT.jar .

# 暴露端口
EXPOSE 8101

# 启动命令
ENTRYPOINT ["java","-jar","/app/dongsoj-backend-gateway-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]
