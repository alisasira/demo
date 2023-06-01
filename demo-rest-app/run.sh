#!/bin/sh

mvn clean package
java -jar target/demo-rest-app.jar