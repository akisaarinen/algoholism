#!/bin/sh
export JAVA_OPTS="-Dorg.mortbay.jetty.Request.maxFormContentSize=9999999" 
java $JAVA_OPTS -Xmx512m -jar `dirname $0`/sbt-launch.jar "$@"
