# Contributing

Details how to contribute to the project.

## Links

- [Release Guide](docs/release_guide.md)
- [Git branching strategy](https://confluence.lab.mobi/display/DEV/Git+branching+strategy)

## Workflow

1. Clone the repository
2. Create a new branch using the branching strategy from the guide under links
3. Make changes to the project
   1. Don't forget to update the `Demo` application with new components or updated component behaviour.
4. Document the changes in `CHANGELOG.md` in project's root directory
5. Make sure the build succeeds in `Codemagic`. Project name is `labcomponents-android`.
5. Create a merge request targeting `develop` branch.
   1. A code review and approval from at least one other person from the Android community.
7. If the review is approved, then merge the merge request to `develop`.
8. Contact the Android community in Slack `#community-android` about releasing a new version of the project. 
   1. TODO: Write a proper release guide and possible automation 

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

The result is that all Kotlin/Java classes will have package names based on the folder structure, but all resources will be available
in the same R class.
