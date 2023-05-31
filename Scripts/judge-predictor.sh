#!/usr/bin/env bash

readonly ROOT_DIR="$PWD"

err() {
  echo "$1" >&2
}

build-project() {
  cd "$ROOT_DIR" || exit 1
  cd "$1" || exit 1
  mvn clean install
  echo "Build done!"
}

check-all-exist() {
  local array="$*"
  for file in "${array[@]}"; do
    if [[ ! -f "$file" ]]; then
      err "File $file does not exist"
      exit 1
    fi
  done
}

IFS=',' read -r -a PREDICTORS <<< "$predictors"
IFS=',' read -r -a INSTRUCTIONS <<< "$instructions"
IFS=',' read -r -a RESULTS <<< "$results"
echo "Got $predictors as predictors"
echo "Got $instructions as instructions"
echo "Got $results as result"

tests_count="${#PREDICTORS[@]}"
if [[ "$tests_count" == 0 ]]; then
  err "There are no predictors, src files, instruction files and result files specified in environment"
  exit 1
fi

for array in "${INSTRUCTIONS[@]}" "${RESULTS[@]}"; do
  check-all-exist "$array"
done

for project in "Shared" "Predictor" "Judge"; do
  build-project "$project"
done
cd "$ROOT_DIR" || exit 1

for ((i = 0; i < tests_count; i++)); do
  predictor_name="${PREDICTORS[$i]}"
  instruction_file="${INSTRUCTIONS[$i]}"
  result_file="${RESULTS[$i]}"
  
  echo "Running test $i"
  java -jar Judge/target/Judge-1.0-SNAPSHOT-jar-with-dependencies.jar \
    --instruction "$instruction_file" \
    --result "$result_file" \
    --predictor "$predictor_name" &> grade.txt
  echo "Ran test $i"
done

