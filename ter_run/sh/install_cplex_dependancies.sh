#!/usr/bin/env bash

mvn install:install-file -DgroupId=cplex -DartifactId=cplex -Dversion=12.71 -Dpackaging=jar -Dfile=/opt/ibm/ILOG/CPLEX_Studio1271/cplex/lib/cplex.jar
mvn install:install-file -DgroupId=cpo -DartifactId=cpo -Dversion=12.71 -Dpackaging=jar -Dfile=/opt/ibm/ILOG/CPLEX_Studio1271/cpoptimizer/lib/ILOG.CP.jar