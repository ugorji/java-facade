#!/bin/sh

echo hi
mycp=".;$CLASSPATH;$WORKDIR/project/deploy/3rdparty/jython.jar;$WORKDIR/project/deploy/jdk_extensions/oxy-jdk-extensions.jar;$WORKDIR/project/deploy/common/oxy-common.jar;$WORKDIR/project/deploy/facade/oxy-facade.jar;$WORKDIR/project/deploy/3rdparty/commons-collections.jar"
echo $mycp

mkdir -p $TMPDIR/jythoncachedir
java -classpath "$mycp" "-Dpython.cachedir=$TMPDIR/jythoncachedir" org.python.util.jython
