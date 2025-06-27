#!/bin/bash

if [ $# -lt 1 ];then
	echo No file provided
	exit 1
fi

users=$(cat "$1")
userString=""

for user in $users;do
	userString+="$user@scs.ubbcluj.ro ,"
done

userString=$(echo "$userString"|sed 's/,$//')
echo $userString
