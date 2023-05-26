#!/bin/bash

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
if [[ ! -d "Test/$predictor" ]]; then
  echo "Predictor directory '$predictor' does not exist."
  exit 1
fi

# Check if the source file exists
if [[ ! -f "$source_file" ]]; then
  echo "Source file '$source_file' does not exist."
  exit 1
fi
