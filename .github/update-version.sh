#!/bin/sh

VERSION=${1}

[[ "$VERSION" != "" ]] || (echo "The version has to be passed as the first argument" && exit)

for m in . xoom-petclinic xoom-vs-spring-perf/xoom xoom-e2e-sys-airline-ops ; do
  mvn versions:set -DnewVersion=$VERSION -DprocessAllModules=true -DgenerateBackupPoms=false -f $m/pom.xml
  mvn versions:use-dep-version -Dincludes=io.vlingo.xoom -DdepVersion=$VERSION -DforceVersion=true -DgenerateBackupPoms=false -f $m/pom.xml
  mvn versions:set-property -Dproperty=vlingo.xoom.version -DnewVersion=$VERSION -DgenerateBackupPoms=false -f $m/pom.xml
  mvn versions:set-property -Dproperty=xoom.version -DnewVersion=$VERSION -DgenerateBackupPoms=false -f $m/pom.xml
done

git add pom.xml */pom.xml */*/pom.xml
