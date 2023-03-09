# Contributing

Details how to contribute to the project.

## Links

- [Directory Structure](docs/directory_structure.md)
- [Resource Visibility](docs/resource_visibility.md)
- [Git branching strategy](https://confluence.lab.mobi/display/DEV/Git+branching+strategy)
- [Release Guide](docs/release_guide.md)

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
