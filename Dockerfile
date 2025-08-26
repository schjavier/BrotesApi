
# --- Etapa de Build ---
# Usa una imagen de Java con Maven preinstalado. Esto facilita el proceso de construcción.
FROM maven:3.9.5-eclipse-temurin-17 AS build

# Define el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos del proyecto al contenedor.
# Esto incluye el pom.xml y todo el código fuente.
COPY pom.xml .
COPY src ./src
COPY .mvn .mvn
COPY mvnw .

# Empaqueta el proyecto en un solo JAR.
# El comando 'package' de Maven crea el JAR ejecutable.
RUN mvn clean package -DskipTests

# --- Etapa Final (Runtime) ---
# Usa una imagen Java más ligera, solo con el entorno de ejecución (JRE).
# Esto reduce el tamaño de la imagen final.
FROM eclipse-temurin:17-jre

# Crea un usuario no privilegiado para seguridad.
RUN useradd --create-home --shell /bin/bash appuser

# Define el directorio de trabajo y el usuario
WORKDIR /app
USER appuser

# Copia el JAR ejecutable desde la etapa 'build' al contenedor final.
# El nombre del archivo puede variar, por eso se usa un comodín (*).
COPY --from=build /app/target/*.jar app.jar

# Expone el puerto por defecto de Spring Boot.
EXPOSE 8080

# Comando para ejecutar la aplicación.
# El -jar le dice a Java que ejecute el archivo app.jar.
ENTRYPOINT ["java", "-jar", "app.jar"]