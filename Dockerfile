FROM adoptopenjdk/openjdk11-openj9:jre-11.0.7_10_openj9-0.20.0-alpine
ADD target/openwms-wms-receiving.jar app.jar
ENV JAVA_OPTS="-Xshareclasses -Xquickstart -noverify"
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar
