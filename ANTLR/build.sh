#!/bin/sh

java -jar /usr/local/lib/antlr-4.7.1-complete.jar -visitor Mx.g4

find . -name '*.java' -exec sed -i '1ipackage cat.footoredo.mx.cst;' {} \; -exec mv {} ../src/main/cat/footoredo/mx/cst/ \;
