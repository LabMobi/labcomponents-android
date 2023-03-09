# Contributing

Details on how to contribute to the project.

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
4. Document the changes in `CHANGELOG.md` in the project's root directory
5. Document the changes in the changed components documentation page in `docs` dir.
   1. Add a new page to `docs/components` if a new component was added.
   2. Add a link to the component doc in `docs/main.md`.
6. Make sure the build succeeds in `Codemagic`. The project name is `labcomponents-android`.
7. Create a merge request targeting the `develop` branch.
   1. A code review and approval from at least one other person from the Android community.
8. If the review is approved, then merge the merge request to `develop`.
9. Contact the Android community in Slack `#community-android` about releasing a new version of the project. 
   1. TODO: Write a proper release guide and possible automation 
