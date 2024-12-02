# 使用多阶段构建，优化镜像大小
FROM node:16-alpine as frontend
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .

FROM openjdk:11-jdk-alpine as backend
WORKDIR /app
COPY pom.xml ./
RUN mvn package

FROM nginx:alpine

# 将前端构建结果复制到 Nginx
COPY --from=frontend /app/dist /usr/share/nginx/html

# 将后端构建结果复制到指定目录（根据您的项目结构调整）
COPY --from=backend /app/target/your-backend.jar /usr/local/java/

# 配置 Nginx
COPY nginx.conf /etc/nginx/nginx.conf

# 暴露端口
EXPOSE 80

# 启动 Nginx 和 Java 后端
CMD ["nginx", "-g", "daemon off;"] && java -jar /usr/local/java/your-backend.jar