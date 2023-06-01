#!/bin/sh

mvn clean package
cp ../input.txt target/input.txt
cd target
java -jar demo-spt.jar