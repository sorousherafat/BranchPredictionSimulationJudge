#!/usr/bin/env bash

readonly ROOT_DIR="$PWD"

err() {
  echo "$1" >&2
}

build-project() {
  cd "$ROOT_DIR" || exit 1
  cd "$1" || exit 1
  mvn clean install
  echo ""
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

if [[ "${#PREDICTORS[@]}" != "${#SRC_FILES[@]}" || "${#SRC_FILES[@]}" != "${#INSTRUCTIONS[@]}" || "${#INSTRUCTIONS[@]}" != "${#RESULTS[@]}" ]]; then
  err "Number of predictors, src files, instruction files and result files are not equal"
  exit 1
fi

tests_count="${#PREDICTORS[@]}"
if [[ "$tests_count" == 0 ]]; then
  err "There are no predictors, src files, instruction files and result files specified in environment"
  exit 1
fi

for array in "${SRC_FILES[@]}" "${INSTRUCTIONS[@]}" "${RESULTS[@]}"; do
  check-all-exist "$array"
done

rm "Predictor/src/main/java/hardwar/branch/prediction/judged/"*
for ((i = 0; i < tests_count; i++)); do
  file="${SRC_FILES[$i]}"
  cp "$file" "Predictor/src/main/java/hardwar/branch/prediction/judged/"
done

for project in "Shared" "Predictor" "Judge"; do
  build-project "$project"
done
cd "$ROOT_DIR" || exit 1

for ((i = 0; i < tests_count; i++)); do
  predictor_name="${PREDICTORS[$i]}"
  instruction_file="${INSTRUCTIONS[$i]}"
  result_file="${RESULTS[$i]}"

  java -jar Judge/target/Judge-1.0-SNAPSHOT-jar-with-dependencies.jar \
    --instruction "$instruction_file" \
    --result "$result_file" \
    --predictor "$predictor_name"
done

rm "Predictor/src/main/java/hardwar/branch/prediction/judged/"*
