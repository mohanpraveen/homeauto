#!/bin/bash
echo "Compiling Java class"
/usr/bin/javac -classpath /home/pi/IP-tracker/javax.mail.tar:. IPChecker.java
echo "Running java class"
/usr/bin/java -classpath /home/pi/IP-tracker/javax.mail.tar:. IPChecker
