# FROM maven:3.8.6-openjdk-11-slim AS backend
# WORKDIR /app
# # nodejs


# # minio
# WORKDIR /app/minio
# RUN apt-get update
# RUN apt-get install -y wget
# RUN wget https://dl.min.io/server/minio/release/linux-amd64/minio
# RUN chmod +x minio
# RUN nohup ./minio server ./data > ./logfile 2>&1 &

# # mysql
# RUN apt-get install mysql-server


# WORKDIR /app/
# COPY ./M2VR-back ./back
# COPY ./M2VR-front ./front
# # RUN git clone https://github.com/stringl1l1l1l/M2VR-buefy.git

# WORKDIR /app/M2VR-back
# RUN mvn package -DskipTests


# # RUN cd /app/M2VR-front && npm install
# RUN nohup mysqld --defaults-file=/etc/mysql/my.cnf --user=root &
# RUN cd /app/minio && nohup ./minio server ./data > ./logfile 2>&1 &
# RUN cd /app/front && nohup npm run dev >./logfile 2>&1 &
# RUN cd /app/back && nohup java -jar target/*.jar >./logfile 2>&1 &

# EXPOSE 5173
# EXPOSE 9000
# EXPOSE 3306
# EXPOSE 8080

# CMD [ "/bin/bash" ]
FROM m2vr:squash
WORKDIR /app
ENV PATH=/app/minio/bin:$PATH
COPY ./start.sh .
COPY ./m2vr.sql ./m2vr.sql
COPY ./data/imgs ./imgs
ENTRYPOINT ["/bin/bash", "./start.sh"]