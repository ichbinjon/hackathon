@echo off &setlocal

rem install -r PATH_TO_MAVEN_REPO
rem example install -r http://WS01190:8081/repository
rem install without -r will use default internet settings


:loop
if not "%1"=="" (
    if "%1"=="-r" (
        set REPO=%2
        shift
    )
    shift
    goto :loop
)

set WRAPPER_PROPS_IN="install\gradle-wrapper.properties.template"
set WRAPPER_PROPS_OUT="gradle\wrapper\gradle-wrapper.properties"
set PROXY_BUILD_GRADLE_IN="install\proxy-build.gradle.template"
set BUILD_GRADLE="build.gradle"

if %REPO%=="" (
  call :configure_default_access
) else (  
  call :configure_proxy_access
)
echo Executing: gradlew clean shadowJar
(gradlew clean shadowJar)
exit /B %ERRORLEVEL% 

:configure_default_access
  set GRADLE_DIST_URL=https://services.gradle.org/distributions
  echo GRADLE_DIST_URL: %GRADLE_DIST_URL%
  (for /f "delims=" %%a in ('findstr "^" "%WRAPPER_PROPS_IN%"') do (
    set "s=%%a"
    setlocal enabledelayedexpansion
    set "s=!s:${gradle_distributions_url}=%GRADLE_DIST_URL%!"
    echo !s!
    endlocal)
  )>%WRAPPER_PROPS_OUT%
   
  rem type "%WRAPPER_PROPS_OUT%"  
  echo Gradle will be installed from %GRADLE_DIST_URL% and Maven will use maven central

  if exist "%BUILD_GRADLE%.bak" COPY %BUILD_GRADLE%.bak "%BUILD_GRADLE%"
exit /B 0


:configure_proxy_access
  set GRADLE_DIST_URL=%REPO%/gradle-distributions
  echo GRADLE_DIST_URL: %GRADLE_DIST_URL%
  (for /f "delims=" %%a in ('findstr "^" "%WRAPPER_PROPS_IN%"') do (
    set "s=%%a"
    setlocal enabledelayedexpansion
    set "s=!s:${gradle_distributions_url}=%GRADLE_DIST_URL%!"
    echo !s!
    endlocal)
  )>%WRAPPER_PROPS_OUT%
   
  rem type "%WRAPPER_PROPS_OUT%"  
  
  if not exist "%BUILD_GRADLE%.bak" COPY %BUILD_GRADLE% "%BUILD_GRADLE%.bak"
  
  set MAVEN_URL=%REPO%/maven-public/

  (for /f "delims=" %%a in ('findstr "^" "%PROXY_BUILD_GRADLE_IN%"') do (
    set "s=%%a"
    setlocal enabledelayedexpansion
    set "s=!s:${maven_url}=%MAVEN_URL%!"
    echo !s!
    endlocal)
  )>%BUILD_GRADLE%

  rem type "%BUILD_GRADLE%"  

  echo Gradle will be installed from %GRADLE_DIST_URL% and Maven will be proxied to %MAVEN_URL%
exit /B 0