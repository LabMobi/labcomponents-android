# Contributing

Details on how to contribute to the project.

## Links

- [Directory Structure](directory_structure.md)
- [Resource Visibility](resource_visibility.md)
- [Git branching strategy](https://confluence.lab.mobi/display/DEV/Git+branching+strategy)
- [RELEASE_GUIDE.md](../RELEASE_GUIDE.md)

## Workflow

1. Clone the repository
2. Create a new branch using the branching strategy from the guide under links
3. Make changes to the project
   1. Don't forget to update the `Demo` application with new components or updated component behaviour.
4. Document the changes in `CHANGELOG.md` in the project's root directory
5. Document the changes in the changed components documentation page in `docs` dir.
   1. Add a new page to `docs/components` if a new component was added.
   2. Add a link to the component doc in [README.md](../README.md).
6. Make sure the build succeeds in `Codemagic`. The project name is `labcomponents-android`.
   1. Note: If you do not have access to Codemagic then build locally and/or discuss with `Lauris Kruusamäe` and `Harri Kirik` about getting access.

7. Create a merge request targeting the `develop` branch.
   1. A code review and approval from at least one other person from the community.
   1. Tag `Lauris Kruusamäe` and `Harri Kirik` in the request.
8. If the review is approved, then merge the merge request to `develop`.
9. Follow the [RELEASE_GUIDE.md](../RELEASE_GUIDE.md) and release the library
   1. If you do not have access then contact  `Lauris Kruusamäe` and `Harri Kirik`, then can help or make the release themselves

10. OPTIONAL: If Mobi Lab employee, then contact the Android community in Slack `#community-android` about releasing a new version of the project. 

## Dependency versioning

Dependency versioning is using Gradle Version Catalogs feature. See https://docs.gradle.org/current/userguide/platforms.html#sec:version-catalog-plugin
Dependency versions are defined in `gradle/libs.versions.toml`, and helper functions are defined in `dependencies.gradle` file, which can be used to add a set of reusable dependencies. For example, testing dependencies come in a package that can be reused where needed.
