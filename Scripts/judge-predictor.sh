#!/usr/bin/env bash

set -o errexit
set -o nounset

readonly JUDGED_DIR="src/main/java/hardwar/branch/prediction/judged"

# Define usage function to display script usage instructions
usage() {
  echo "Usage: $0 --predictor <predictor-name> --source <source-file>"
}

# Process command-line arguments
options=$(getopt -o p:s: --long predictor:,source: -n "$0" -- "$@")
eval set -- "$options"

# Variables to store the values
predictor=""
source_file=""

# Loop through the command-line arguments
while true; do
  case "$1" in
    -p | --predictor)
      predictor="$2"
      shift 2
    ;;
    -s | --source)
      source_file="$2"
      shift 2
    ;;
    --)
      shift
      break
    ;;
    *)
      usage
      exit 1
    ;;
  esac
done

# Check if the required arguments are provided
if [[ -z "$predictor" || -z "$source_file" ]]; then
  usage
  exit 1
fi

# Check if the predictor directory exists
readonly predictor_path="Test/$predictor"
if [[ ! -d "$predictor_path" ]]; then
  echo "Predictor directory '$predictor' does not exist."
  exit 1
fi

# Check if `instruction.json` and `result.json` files exist
readonly instruction_file="$predictor_path/instruction.json"
readonly result_file="$predictor_path/result.json"
if [[ ! -f "$instruction_file" || ! -f "$result_file" ]]; then
  echo "Instruction or result file does not exist inside the predictor directory '$predictor'"
  exit 1
fi

# Check if the source file exists
if [[ ! -f "$source_file" ]]; then
  echo "Source file '$source_file' does not exist."
  exit 1
fi

# Build and install the source file
cd ToBeJudgedPredictor
rm "$JUDGED_DIR"/* || true
cp "../$source_file" "$JUDGED_DIR"
mvn clean install

echo ""

# Judge the predictor
cd ../Judge
mvn clean test -Dpredictor="../$predictor"                         \
               -Dinstruction="../$predictor_path/instruction.json" \
               -Dresult="../$predictor_path/result.json"
