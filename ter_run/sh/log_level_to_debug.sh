#!/usr/bin/env bash

set -euo pipefail
IFS=$'\n\t'

SCRIPT=$(realpath $0)
SCRIPT_PATH=$(dirname ${SCRIPT})
cd ${SCRIPT_PATH}
cd ..

FILE="src/main/resources/logback-test.xml"
SEARCH='<root level="\(off\|error\|warn\|info\|debug\|trace\)" additivity="false">'
REPLACE='<root level="debug" additivity="false">'
COMMAND="s/${SEARCH}/${REPLACE}/g"

sed -i ${COMMAND} ${FILE}