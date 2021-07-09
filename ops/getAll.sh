#!/bin/bash

if [ -z ${1} ]; then
	echo "parameter #1 required: max problem id"
	exit 1
fi


if [ -z ${2} ]; then
	echo "parameter #2 required: destination directory"
	exit 1
fi

MAXID=${1}
DEST=${2}

for i in $(seq $MAXID);
do
	./getProblem.sh ${i} ${DEST}
done
