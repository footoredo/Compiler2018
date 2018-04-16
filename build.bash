set -e
cd "$(dirname "$0")"
mkdir -p bin
find ./src -name *.java | javac -d out -classpath "lib/antlr-4.7.1-complete.jar" @/dev/stdin