#!/bin/bash

submission_dir=${1:?"Please enter directory with student jars"};
submission_dir="${submission_dir%/}"

source_dir="${submission_dir}_sources"
recompile_dir="${submission_dir}_recompiled"

script_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Be sure to put the runtime jar and any dependencies under a lib/ directory
game_jar="$script_dir/lib/treasurehunt.jar"

jar_count=0

rm -rf "$source_dir" "$recompile_dir"
mkdir -p "$recompile_dir"

# Recompile function in case student didn't submit jar with class file
function recompile()
{
  rm -rf "$submission_dir"/temp*
  
  mkdir "$submission_dir/temp"
  unzip "$1" -d "$submission_dir/temp" -x '*.class'
  source=$(find "$submission_dir/temp" -name "*.java")
  for s in $source; do
    javac -cp "$game_jar" -sourcepath "$submission_dir/temp" $s
  done

  cd "$submission_dir/temp"
  zip -r "../temp.jar" .
  cd -
}


# Loop through all jars in provided directory
find "$submission_dir" -type f -name "*.jar" -print0 | while IFS= read -r -d '' jar; 
do

  # Get name without spaces if students were evil enough to submit a jar with spaces
  jar_name="$(echo "$jar" | sed -e "s/.*\///g")"
  source_dir_name="$(echo "$jar_name" | sed -e "s/\./_/g")"
  echo "Running $jar_count: $jar_name"

  # Extract source code out for viewing later
  mkdir -p "$source_dir/$source_dir_name"
  unzip "$jar" "*.java" -d "$source_dir/$source_dir_name" || echo "No java source files found."
 
  # Recompile
  recompile "$jar"
  temp="$submission_dir/temp.jar"

  cp "$temp" "$recompile_dir/$jar_name"
done

echo
echo "See $source_dir for corresponding source code"
echo
