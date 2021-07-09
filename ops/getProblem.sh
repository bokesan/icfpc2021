#!/bin/bash

if [ ! -f key.txt ]; then
	echo "API key file key.txt not found!"
	exit 1
fi

if [ -z ${1} ]; then
	echo "parameter required: problem id"
	exit 1
fi

KEY=`cat key.txt`
ID=${1}

curl -s -H "Authorization: Bearer ${KEY}" https://poses.live/api/problems/${ID} -o "problem_${ID}.json"

if [ ! -z ${2} ]; then
	mv "problem_${ID}.json" "${2}problem_${ID}.json"
fi
