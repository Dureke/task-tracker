set COMMAND=%1
set ARG_1=%2
set ARG_2=%3

mvn exec:java -Dexec.mainClass="tracker.Main" -Dexec.args="%COMMAND% %ARG_1% %ARG_2%"