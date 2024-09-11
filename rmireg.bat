@echo off
REM Navegue até o diretório onde o `rmiregistry` deve ser executado
cd /d target\classes

REM Inicie o `rmiregistry` com o classpath definido
"%JAVA_21_HOME%\bin"\rmiregistry