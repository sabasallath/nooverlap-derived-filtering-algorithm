#!/usr/bin/env bash

set -euo pipefail
IFS=$'\n\t'

SCRIPT=$(realpath $0)
SCRIPT_PATH=$(dirname ${SCRIPT})
cd ${SCRIPT_PATH}

rm -rf ../output/*