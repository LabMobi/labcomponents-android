# Directory structure

Overview of how project directories are structured. 

## Project structure

The project uses Kotlin as the main language. See https://kotlinlang.org/docs/reference/ for more information about Kotlin.

`Ktlint` is added to the project to keep code style and formatting uniform. Ktlint is run with regular lint checks during a build (specifically `gradle check` command). For more information about Ktlint, see https://github.com/shyiko/ktlint

We have different modules for different parts of the application:
* app-demo -> Component catalog
* lib -> Lab Design library

## Library module structure

The `lib` module has custom source sets defined in `lib/build.gradle`. Custom source sets are needed so that we can separate
component sources and resources into different folders while using a single namespace for resources. This would also work
with a transitive R class, but this might not be supported in the future.

**When adding a new source directory (most likely a new component), the new path must be added to the `srcDirs` list in `lib/build.gradle`.**

The result is that all Kotlin/Java classes will have package names based on the folder structure, but all resources will be available
in the same R class.
