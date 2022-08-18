#!/bin/bash

echo "Creating database..."
../gradlew -q test --tests db_project.buildDb.BuildDb.generateDB > /dev/null

if [[ $? -ne 0 ]]; then
  echo "***Database already created***"
else 
  echo "***Database succesfully created!***"
fi
