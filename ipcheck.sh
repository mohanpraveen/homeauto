#!/bin/bash

echo "Pulling Git Repo"
cd /home/pi/IP-tracker/homeauto
git pull
echo "Compiling Java class"
/usr/bin/javac -classpath /home/pi/IP-tracker/homeauto/javax.mail.jar:. IPChecker.java
echo "Running java class"
/usr/bin/java -classpath /home/pi/IP-tracker/homeauto/javax.mail.jar:. IPChecker
