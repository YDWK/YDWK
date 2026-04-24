#!/usr/bin/env sh
set -eu

# Qodana sometimes fails Gradle import when IDE Gradle JVM points to an unavailable SDK.
# Force project-level Gradle settings to use JAVA_HOME (JDK 21 in CI), required by Gradle 9.x.
rm -rf .idea
mkdir -p .idea
cat > .idea/gradle.xml <<'XML'
<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="GradleSettings">
    <option name="linkedExternalProjectsSettings">
      <GradleProjectSettings>
        <option name="distributionType" value="DEFAULT_WRAPPED" />
        <option name="externalProjectPath" value="$PROJECT_DIR$" />
        <option name="gradleJvm" value="#JAVA_HOME" />
        <option name="modules">
          <set>
            <option value="$PROJECT_DIR$" />
          </set>
        </option>
      </GradleProjectSettings>
    </option>
  </component>
</project>
XML

cat > .idea/misc.xml <<'XML'
<?xml version="1.0" encoding="UTF-8"?>
<project version="4">
  <component name="ProjectRootManager" version="2" languageLevel="JDK_21" project-jdk-name="21" project-jdk-type="JavaSDK" />
</project>
XML

echo "Prepared Qodana IDE config with Gradle JVM=#JAVA_HOME"

