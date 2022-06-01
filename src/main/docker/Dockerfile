FROM fiadliel/java8-jre
MAINTAINER kingwarluo
VOLUME /tmp
ADD target/es-1.0.jar es-1.0.jar
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo 'Asia/Shanghai' >/etc/timezone
ENTRYPOINT ["java", "-jar", "/es-1.0.jar "]