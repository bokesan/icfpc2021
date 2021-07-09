#!/bin/bash

if [ ! -f key.txt ]; then
	echo "API key file key.txt not found!"
	exit 1
fi

if [ -z ${1} ]; then
	echo "parameter #1 required: problem id"
	exit 1
fi

if [ -z ${2} ]; then
	echo "parameter #2 required: solution file"
	exit 1
fi

if [ ! -f ${2} ]; then
	echo "solution file ${2} not found!"
	exit 1
fi

KEY=`cat key.txt`
ID=${1}

curl -s -H "Authorization: Bearer ${KEY}" -H "Content-Type: application/json" --data @"${2}" https://poses.live/api/problems/${ID}/solutions  | python -m json.tool

