#! /bin/bash

if [[ $# -eq 0 ]] ; then
    echo "no project name provided..."
    exit 1
fi

mkdir $1
pushd $1
mkdir src
cp ../build.sh .
cp ../run.sh .
cp ../.gitignore .
popd
