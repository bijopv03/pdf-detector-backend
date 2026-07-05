FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

RUN cp target/pdfconverter-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8060

ENTRYPOINT ["java","-jar","app.jar"]