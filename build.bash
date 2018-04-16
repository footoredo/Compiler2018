set -e
cd "$(dirname "$0")"
mkdir -p bin
find ./src -name *.java | javac -d bin -classpath "lib/antlr-4.7.1-complete.jar:lib/junit-4.12.jar" @/dev/stdin
