FROM gradle:8.11.1-jdk21

# Копируем исходники
WORKDIR /app
COPY . .

# Не собираем здесь! Собираем при запуске.
EXPOSE 8080

# Создаём скрипт запуска
RUN echo '#!/bin/bash\n\
gradle clean build -x test && \n\
java -jar build/libs/DevOps-1.0.0.jar\n'\
> /run.sh && chmod +x /run.sh

ENTRYPOINT ["/run.sh"]
