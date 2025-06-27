#!/bin/bash

if [ $# -lt 1 ];then
	echo No directory provided
	exit 1
fi

files=$(find "$1" -type f -name '*.log')

for file in $files;do
	lines=$(cat "$file"|sort)
	>"$file"
	for line in $lines;do
		echo "$line">>"$file"
	done
done

exit 0
