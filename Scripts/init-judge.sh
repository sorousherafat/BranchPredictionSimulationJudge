#!/usr/bin/env bash

set -o errexit
set -o nounset

cd Shared
mvn clean install

echo ""

cd ../ToBeJudgedPredictor
mvn clean install

echo ""

cd ../Judge
mvn clean install

cd ..
