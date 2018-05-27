set -e
cd "$(dirname "$0")"
export CCHK="java -classpath ./lib/antlr-4.7.1-complete.jar:./lib/junit-4.12.jar:./bin cat.footoredo.mx.OJ.Tester"
cat > program.m # save everything in stdin to program.txt
$CCHK
