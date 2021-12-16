FROM openjdk:11-jdk

#==========
# Maven
#==========
ENV MAVEN_VERSION 3.5.4
ARG USER_HOME_DIR="/root"

RUN curl -fsSL http://archive.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz | tar xzf - -C /usr/share \
  && mv /usr/share/apache-maven-$MAVEN_VERSION /usr/share/maven \
  && ln -s /usr/share/maven/bin/mvn /usr/bin/mvn

ENV MAVEN_HOME /usr/share/maven
ENV MAVEN_CONFIG "$USER_HOME_DIR/.m2"

#========================
# git
#========================
RUN apt-get -y update
RUN apt-get -y install git

#========================================
# Add normal user with passwordless sudo
#========================================
RUN useradd remoteuser --shell /bin/bash --create-home \
  && usermod -a -G sudo remoteuser \
  && echo 'ALL ALL = (ALL) NOPASSWD: ALL' >> /etc/sudoers \
  && echo 'remoteuser:secret' | chpasswd

RUN apt-get update -y && apt-get -y install vim

USER remoteuser

RUN mkdir /home/remoteuser/serenity-project

WORKDIR /home/remoteuser/serenity-project

RUN git clone https://github.com/leftty/rest-api-homework.git
