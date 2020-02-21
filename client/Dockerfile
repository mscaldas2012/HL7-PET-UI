#FROM gradle:6.2.0-jdk11 AS build
#
#COPY --chown=gradle:gradle . /home/gradle/src
#WORKDIR /home/gradle/src
#
#RUN gradle wrapper
#RUN ./gradlew build
#
###------ Step 2
## FROM openjdk:11-jre-slim
#
#EXPOSE 3000
#
#RUN mkdir /app
#
##COPY --from=build /home/gradle/src/build/ /app/
#COPY  /home/gradle/src/build/ /app/
#WORKDIR /app
#ENTRYPOINT ["yarn",  "start"]


FROM nginx
# copy build folder and assets
COPY build /usr/share/nginx/html
# permission to build folder
RUN chmod 755 -R /usr/share/nginx/html

# support running as arbitrary user which belogs to the root group
RUN chmod g+rwx /var/cache/nginx /var/run /var/log/nginx
# users are not allowed to listen on priviliged ports
RUN sed -i.bak 's/listen\(.*\)80;/listen 3000;/' /etc/nginx/conf.d/default.conf
# comment user directive as master process is run as user in OpenShift anyhow
RUN sed -i.bak 's/^user/#user/' /etc/nginx/nginx.conf
# OpenShift port
EXPOSE 3000
