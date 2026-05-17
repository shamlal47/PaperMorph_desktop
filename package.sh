#!/bin/bash
# ====================================
# Paper Morph - Build & Package Script
# ====================================

set -e

JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export JAVA_HOME

echo "=== Step 1: Building fat JAR ==="
mvn -q clean package -DskipTests
echo "✓ JAR built: target/image-to-pdf-1.0-SNAPSHOT-jar-with-dependencies.jar"

echo ""
echo "=== Step 2: Creating native .deb installer ==="
mkdir -p target/installer

$JAVA_HOME/bin/jpackage \
  --name "PaperMorph" \
  --app-version "1.0.0" \
  --vendor "Shamlal" \
  --description "Convert Images to PDF without any Ads" \
  --input target \
  --main-jar image-to-pdf-1.0-SNAPSHOT-jar-with-dependencies.jar \
  --main-class com.converter.Main \
  --type deb \
  --dest target/installer \
  --linux-shortcut \
  --linux-menu-group "Utility"

echo ""
echo "✓ Installer created in target/installer/"
ls -lh target/installer/*.deb
echo ""
echo "=== To install, run: ==="
echo "sudo dpkg -i target/installer/papermorph_1.0.0-1_amd64.deb"
