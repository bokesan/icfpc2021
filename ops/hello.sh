#!/bin/bash

if [ ! -f key.txt ]; then
	echo "API key file key.txt not found!"
	exit 1
fi

KEY=`cat key.txt`

curl -s -H "Authorization: Bearer ${KEY}" https://poses.live/api/hello | python -m json.tool
