@echo off
setlocal
cd /d "%~dp0..\.."

REM Run build-windows.bat first.
set APP_NAME=CpE Lab Inventory
set MAIN_CLASS=cpe_lab_inventory.MainApp
if defined JAVAFX_HOME (
  set JAVAFX_LIB=%JAVAFX_HOME%\lib
) else (
  set JAVAFX_LIB=C:\JavaFX\javafx-sdk-25.0.2\lib
)
if not exist "%JAVAFX_LIB%\javafx.controls.jar" (
  if exist "C:\JavaFX\javafx-sdk-25.0.3\lib\javafx.controls.jar" (
    set JAVAFX_LIB=C:\JavaFX\javafx-sdk-25.0.3\lib
  )
)
set MYSQL_JAR=dist\manual\lib\mysql-connector-j-8.0.33.jar
set APP_JAR=dist\manual\CpE_Lab_Inventory.jar
set APP_IMAGE=dist\installer\%APP_NAME%
set APP_MODULES=javafx.controls,javafx.fxml,java.sql,java.desktop,java.naming,java.management,java.logging,java.xml,jdk.crypto.ec,jdk.unsupported
set JPACKAGE=jpackage
set JPACKAGE_FOUND=

where jpackage >nul 2>nul
if not errorlevel 1 set JPACKAGE_FOUND=1

if not defined JPACKAGE_FOUND (
  if defined JAVA_HOME if exist "%JAVA_HOME%\bin\jpackage.exe" (
    set JPACKAGE=%JAVA_HOME%\bin\jpackage.exe
    set JPACKAGE_FOUND=1
  )
)

if not defined JPACKAGE_FOUND (
  for /d %%J in ("C:\Program Files\Java\jdk-25*") do (
    if exist "%%~fJ\bin\jpackage.exe" (
      set JPACKAGE=%%~fJ\bin\jpackage.exe
      set JPACKAGE_FOUND=1
    )
  )
)

if not defined JPACKAGE_FOUND (
  echo ERROR: jpackage was not found.
  echo.
  echo Check if this file exists:
  echo C:\Program Files\Java\jdk-25...\bin\jpackage.exe
  echo.
  echo If it exists, set JAVA_HOME to that JDK folder and reopen Command Prompt.
  echo If it does not exist, install a full JDK that includes jpackage.
  goto fail
)

if not exist "%JAVAFX_LIB%\javafx.controls.jar" (
  echo ERROR: JavaFX was not found at:
  echo %JAVAFX_LIB%
  echo Edit JAVAFX_LIB in this file to match your JavaFX SDK path.
  goto fail
)

if not exist "%APP_JAR%" (
  echo ERROR: App jar was not found:
  echo %APP_JAR%
  echo Run deploy\windows\build-windows.bat first.
  goto fail
)

if not exist "%MYSQL_JAR%" (
  echo ERROR: MySQL Connector/J was not found:
  echo %MYSQL_JAR%
  echo Run deploy\windows\build-windows.bat first.
  goto fail
)

if exist "%APP_IMAGE%" (
  echo ERROR: App image already exists:
  echo %APP_IMAGE%
  echo Rename or delete that folder before packaging again.
  goto fail
)

echo Using jpackage: %JPACKAGE%
echo Using JavaFX: %JAVAFX_LIB%
echo Using app jar: %APP_JAR%
echo Using modules: %APP_MODULES%
echo Destination: dist\installer
echo.

"%JPACKAGE%" ^
  --type app-image ^
  --name "%APP_NAME%" ^
  --input dist\manual ^
  --main-jar CpE_Lab_Inventory.jar ^
  --main-class "%MAIN_CLASS%" ^
  --module-path "%JAVAFX_LIB%" ^
  --add-modules %APP_MODULES% ^
  --java-options "--enable-native-access=javafx.graphics" ^
  --win-console ^
  --app-version 1.0 ^
  --dest dist\installer
if errorlevel 1 goto fail

(
  echo @echo off
  echo cd /d "%%~dp0"
  echo echo Package folder:
  echo cd
  echo echo.
  echo echo Top-level files:
  echo dir /b
  echo echo.
  echo echo App folder:
  echo dir /b app
  echo echo.
  echo echo Runtime bin folder:
  echo if exist runtime\bin ^(dir /b runtime\bin^) else echo runtime\bin was not found
  echo echo.
  echo if exist runtime\bin\java.exe ^(
  echo   runtime\bin\java.exe --add-modules %APP_MODULES% -cp "app\CpE_Lab_Inventory.jar;app\lib\mysql-connector-j-8.0.33.jar" cpe_lab_inventory.MainApp
  echo ^) else ^(
  echo   echo runtime\bin\java.exe was not found. This runtime image may not include the java launcher.
  echo   echo Try running the EXE from this same Command Prompt:
  echo   echo "CpE Lab Inventory.exe"
  echo ^)
  echo echo.
  echo echo Debug launcher finished. Read any error above.
  echo pause
) > "%APP_IMAGE%\run-debug.bat"
if errorlevel 1 goto fail

echo App image created in dist\installer
echo Debug runner created: %APP_IMAGE%\run-debug.bat
echo.
pause
endlocal
exit /b 0

:fail
echo.
echo Packaging failed. Read the error above.
pause
endlocal
exit /b 1
