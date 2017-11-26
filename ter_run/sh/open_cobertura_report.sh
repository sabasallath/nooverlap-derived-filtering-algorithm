#!/usr/bin/env bash

set -euo pipefail
IFS=$'\n\t'

SCRIPT=$(realpath $0)
SCRIPT_PATH=$(dirname ${SCRIPT})
cd ${SCRIPT_PATH}

REPORT_FILE=../target/site/cobertura/index.html

if [ ! -f ${REPORT_FILE} ]; then
    echo "No report Found"
else
    echo "Opening ${REPORT_FILE}"
    xdg-open ${REPORT_FILE}
fi