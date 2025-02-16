# Utilisation de l'image OpenJDK 17
FROM openjdk:17

# Définition du répertoire de travail
WORKDIR /app

# Copier le JAR de l'application dans l'image
COPY target/Projet_CCSR_2025-1.0-SNAPSHOT.jar app.jar

# Définir la commande de démarrage
CMD ["java", "-jar", "app.jar"]
#
# MailHog Dockerfile
#

FROM golang:1.18-alpine as builder

# Install MailHog:
RUN apk --no-cache add --virtual build-dependencies \
    git \
  && mkdir -p /root/gocode \
  && export GOPATH=/root/gocode \
  && go install github.com/mailhog/MailHog@latest

FROM alpine:3
# Add mailhog user/group with uid/gid 1000.
# This is a workaround for boot2docker issue #581, see
# https://github.com/boot2docker/boot2docker/issues/581
RUN adduser -D -u 1000 mailhog

COPY --from=builder /root/gocode/bin/MailHog /usr/local/bin/

USER mailhog

WORKDIR /home/mailhog

ENTRYPOINT ["MailHog"]

# Expose the SMTP and HTTP ports:
EXPOSE 1025 8025