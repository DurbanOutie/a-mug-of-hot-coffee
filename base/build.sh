#! /bin/bash
rm -rf ./build
mkdir build
pushd build
javac ../src/*.java -d .
popd
