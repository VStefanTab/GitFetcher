#!/bin/bash

set -e  # Exit if any command fails

# Get the directory of the script (so it works from anywhere)
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
POM_DIR="$SCRIPT_DIR"  # Assume script is in the project root

# Dynamically find JavaFX SDK
JAVAFX_LIB="$(find "$HOME" -type d -name 'javafx-sdk-*' -print -quit)/lib"
if [ ! -d "$JAVAFX_LIB" ]; then
  echo "❌ JavaFX SDK not found in $HOME. Set JAVAFX_LIB manually!"
  exit 1
fi

MAIN_CLASS="com.example.Main"
JAR_FILE="$POM_DIR/target/githubprofileviewer-1-jar-with-dependencies.jar"

cd "$POM_DIR" || exit 1

echo "📦 Building the project..."
mvn clean package

echo "🚀 Running the application..."
java \
  --module-path "$JAVAFX_LIB" \
  --add-modules javafx.controls,javafx.fxml \
  -cp "$JAR_FILE" "$MAIN_CLASS"

