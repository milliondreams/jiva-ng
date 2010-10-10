@echo off
setlocal enabledelayedexpansion

if "%JAVA_HOME%"=="" goto nojava

set DIST_DIR=.
set TARGET_DIR=%DIST_DIR%\target

set SCALA_HOME=.
set SCALA_VERSION=2.8.0
set BOOT_CP=%SCALA_HOME%\lib\scala-library-%SCALA_VERSION%.jar

set CLASSPATH=%TARGET_DIR%\jiva.jar
set CLASSPATH=%CLASSPATH%;%SCALA_HOME%\lib\scala-compiler-%SCALA_VERSION%.jar

rem echo "%JAVA_HOME%\bin\java" -Xbootclasspath/a:"%BOOT_CP%" -server -Xmx256m -cp "%CLASSPATH%" net.kogics.jiva.JivaMain %*
"%JAVA_HOME%\bin\java" -Xbootclasspath/a:"%BOOT_CP%" -server -Xmx256m -cp "%CLASSPATH%" net.kogics.jiva.JivaMain %*

goto end

:nojava
echo Please set the JAVA_HOME environment variable.
goto thepause

:thepause
pause

:end
endlocal
