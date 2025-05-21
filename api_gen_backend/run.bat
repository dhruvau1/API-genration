@echo off

set /p josn="Path for JSON: "
set /p output="Path to output: "

java -jar target/app-0.0.1-SNAPSHOT-jar-with-dependencies.jar %josn% %output%
