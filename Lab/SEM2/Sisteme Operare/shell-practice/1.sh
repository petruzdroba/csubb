#!/bin/bash

lines=$(cat ps.fake | awk '{print $1}' |sort |uniq -c| sort -n)

echo "$lines"


