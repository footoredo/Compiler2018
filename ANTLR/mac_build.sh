#!/bin/sh

java -jar ../lib/antlr-4.7.1-complete.jar -visitor Mx.g4

find . -name '*.java' -exec sed -i '' '1i\
package cat.footoredo.mx.cst;\
\
' {} \; -exec mv {} ../src/main/cat/footoredo/mx/cst/ \;
