#!/usr/bin/env bash

echo 'The following Maven command installs your Maven-built Java application'
echo 'into the local Maven repository, which will ultimately be stored in'
echo 'Jenkins''s local Maven repository (and the "maven-repository" Docker data'
echo 'volume).'
set -x
mvn jar:jar install:install help:evaluate -Dexpression=project.name
set +x

echo 'The following complex command extracts the value of the <name/> element'
echo 'within <project/> of your Java/Maven project''s "pom.xml" file.'
set -x
NAME=`mvn help:evaluate -Dexpression=project.name | grep "^[^\[]"`
set +x

echo 'The following complex command behaves similarly to the previous one but'
echo 'extracts the value of the <version/> element within <project/> instead.'
set -x
VERSION=`mvn help:evaluate -Dexpression=project.version | grep "^[^\[]"`
set +x

echo 'The following command runs and outputs the execution of your Java'
echo 'application (which Jenkins built using Maven) to the Jenkins UI.'
set -x

scp -i /var/jenkins_home/.ssh/id_rsa target/${NAME}-${VERSION}.jar tqs@192.168.160.6:~/target/
scp -i /var/jenkins_home/.ssh/id_rsa Dockerfile tqs@192.168.160.6:~/

ssh -i /var/jenkins_home/.ssh/id_rsa tqs@192.168.160.6 'docker stop clouddeliveries'
ssh -i /var/jenkins_home/.ssh/id_rsa tqs@192.168.160.6 'docker container rm clouddeliveries'
ssh -i /var/jenkins_home/.ssh/id_rsa tqs@192.168.160.6 'docker build -t clouddeliveries/app .'
ssh -i /var/jenkins_home/.ssh/id_rsa tqs@192.168.160.6 'docker run -p 8080:8080 --name clouddeliveries -d clouddeliveries/app'