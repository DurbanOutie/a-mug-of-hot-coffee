#! /bin/bash
rm -rf ./build
mkdir build
pushd build
javac ../src/SnakeMain.java -d .
popd
