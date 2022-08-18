#!/bin/bash
echo "Dropping Database";
../gradlew -q test --tests db_project.buildDb.BuildDb.dropDB > /dev/null

if [[ $? -ne 0 ]]; then
  echo "An error occurred...";
else
  echo "Database dropped succesfully!"
fi
