#!/bin/bash

files=$(find . -type f -name "*.pz")

for file in $files;do
        lines=$(sort $file)
        >"$file"
        for line in $lines;do
               echo "$line">>"$file"
        done
done
