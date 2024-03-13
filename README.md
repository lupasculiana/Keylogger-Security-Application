# Keylogger application
Security application that creates a log of the user's behavior on his keyboard in a C program using hooks. This data is sent to a Java microservice that parses it and creates a .csv file with relevant data, such as left/right/backspace amount of clicks, keyboard latency, most used words, and the typing speed of the user. This file will then be used as the data set for a machine learning application in Python that uses Isolation forests to quickly detect the anomaly, and alert back the Java microservice using a cloud AMQP RabbitMQ queue.

Technology stack :
- Java/Springboot
- Spring Security
- PostgreSQL
- C
- Hooks
- Python
- Machine learning
- AMQP/ RabbitMQ
