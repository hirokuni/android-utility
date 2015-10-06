#!/usr/bin/env bash
./gradlew uploadArchives

if [ $? -eq 0 ]; then
    echo "gradlew upload succeeds"
    git add ./
    git commit
else
    echo "gradlew upload fails"
    exit 1;
fi

if [ $? -eq 0 ]; then
    git push origin master
else
    echo "git command fails"
    exit 1;
fi

exit 0;