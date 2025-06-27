#!/bin/bash
if [ $# -ne 1 ] ;then
	echo No directory given
	exit 1
fi

lines=$(find "$1" -type f -name '*.c')
counter=0

for line in $lines;do
	nrLines=$(cat $line|grep -c '.')
	if [ $nrLines -ge 500 ];then
		echo "$line -> $nrLines lines"
		counter=$(($counter+1))
	fi
	if [ $counter -eq 2 ];then
		echo Done
		exit 0
	fi
done

if[ $counter -lt 2];then
	echo Not enough files found with over 500 lines
fi
