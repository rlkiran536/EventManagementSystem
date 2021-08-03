cd "WEB-INF\src\"
for /r %%a in (.) do (javac -d "E:\ZOHO\05\Tomcat\webapps\EventManagementSystem\WEB-INF\classes" %%a\*.java)