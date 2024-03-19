#! /bin/bash

if [[ $# -eq 0 ]] ; then
    echo "no project name provided..."
    exit 1
fi
cp -r base $1
