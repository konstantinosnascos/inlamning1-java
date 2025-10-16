@echo off
cd %~dp0
mvn -q exec:java
pause