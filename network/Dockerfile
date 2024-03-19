# 使用官方 OpenJDK 镜像作为基础镜像
FROM openjdk:21

# 设置工作目录
WORKDIR /app

# 将编译并打包后的应用程序 JAR 文件拷贝到容器中
COPY target/ddc-network.jar /app/ddc-network.jar


# 暴露应用程序使用的端口
EXPOSE 8088

# 启动应用程序
CMD ["java", "-jar", "ddc-network.jar"]