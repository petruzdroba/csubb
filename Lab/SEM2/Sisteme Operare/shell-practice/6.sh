#!/bin/bash

if [ $# -ne 1 ];then
	echo No directory provided
	exit 1
fi 

#add them back after removing their write thing
chmod a+w dir/d/c/b/15.c dir/c/b/9.sh

files=$(find "$1" -type f -ls)

w=$(echo "$files"|awk '$3~/.w..w..w./{print $3, $11}')
echo Before removig write permissions
echo "$w"

files=$(echo "$w"|awk '{print $2}')
for file in $files;do
	chmod a-w "$file"
done

echo After
echo "$(find "$1" -type f -ls|awk '$3~/.-..-..-./{print $0}')"

exit 0

