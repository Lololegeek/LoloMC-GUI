@ECHO OFF
SETLOCAL
SET APP_HOME=%~dp0
SET CLASSPATH=%APP_HOME%gradle\wrapper\gradle-wrapper.jar
IF DEFINED JAVA_HOME SET JAVA_EXE=%JAVA_HOME%\bin\java.exe
IF NOT DEFINED JAVA_HOME SET JAVA_EXE=java.exe
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %GRADLE_OPTS% "-Dorg.gradle.appname=%~nx0" -classpath "%CLASSPATH%" org.gradle.wrapper.GradleWrapperMain %*
SET EXIT_CODE=%ERRORLEVEL%
ENDLOCAL
EXIT /B %EXIT_CODE%
