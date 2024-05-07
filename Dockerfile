FROM debian:latest

RUN apt update && apt install -y wget
RUN apt install -y gpg
RUN wget -O - https://apt.corretto.aws/corretto.key | gpg --dearmor -o /usr/share/keyrings/corretto-keyring.gpg
RUN echo "deb [signed-by=/usr/share/keyrings/corretto-keyring.gpg] https://apt.corretto.aws stable main" | tee /etc/apt/sources.list.d/corretto.list
RUN apt update && apt install -y java-11-amazon-corretto-jdk
RUN apt install -y groovy
RUN apt install -y vim
CMD ["bash"]

