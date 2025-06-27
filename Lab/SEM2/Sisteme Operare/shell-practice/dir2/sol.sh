#!/bin/bash

if [ $# -ne 1 ];then
	echo "Usage sol.sh <1>, arg 1 - word">&2
	exit 2
fi

N=$1 #this word is the one searched
files=$(find . -type f)
foundFiles=""

echo "Fisiere care au #include <$N> sunt:"
found=0
for file in $files;do
	if grep -q "#include <$N>" $file;then
		echo $file
		foundFiles="$foundFiles$file"$'\n'
		found=1
	fi
done

if [ $found -ne 1 ];then
	echo Nu am gasit nimic
fi

echo "Files with most #inlcude lines"
for file in $foundFiles;do
	nrInc=$(grep -c '#include' $file)
	echo "$nrInc $file"
done|sort -r -n

