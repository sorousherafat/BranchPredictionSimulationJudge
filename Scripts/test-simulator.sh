#!/usr/bin/env bash

# Define usage function to display script usage instructions
usage() {
  echo "Usage: $0 -p <predictor-name> <file1> [<file2> ...]"
}

# Process command-line arguments
options=$(getopt -o p: --long predictor: -n "$0" -- "$@")
eval set -- "$options"

# Variables to store the command-line arguments
predictor=""
src_files=()

# Loop through the command-line arguments
while true; do
  case "$1" in
    -p | --predictor)
      predictor="$2"
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
if [[ -z "$predictor" || $# -eq 0 ]]; then
  usage
  exit 1
fi

# Check if the predictor directory exists
predictor_path="Test/$predictor"
if [[ ! -d "$predictor_path" ]]; then
  echo "Predictor directory '$predictor' does not exist."
  exit 1
fi

# Check if all src_files exist
for file in "$@"; do
  if [[ ! -f "$file" ]]; then
    echo "File '$file' does not exist."
    exit 1
  fi
  src_files+=("$file")
done
