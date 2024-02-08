#!/bin/bash -x
VERSION="1.0-SNAPSHOT"
#allow ** for recursive subdirectories
shopt -s globstar
OLD_PATH=$(pwd)
echo $0
BUILD_PATH=$(dirname $0)
cd "$BUILD_PATH"
mkdir -p target/classes

javac -d "./target/classes" --source-path "./src/main/java" ./src/main/java/**/*.java

jar cvfm target/StopWatch-${VERSION}.jar MANIFEST.txt -C "./target/classes" .

cd "$OLD_PATH"