#!/bin/sh

mvn clean package
cp ../input.txt target/input.txt
cd target
java -jar module2.jar