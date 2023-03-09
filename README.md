Mobi Lab Components

# Mobi Lab Components README

Mobi Lab Components (LabComponents) help developers execute our internal design system's components.

These components are based on [Material Components for Android](https://github.com/material-components/material-components-android).

The components provide implementations for [Mobi Lab's design system](https://www.figma.com/file/gxt4iyWGyliILJSOCLXonl/P42-design-system-template-(Duplicate-this!)).

## Description

The project consists of the component library in `lib` module and components demo application in `app-demo` module.

## Dependency versioning

Dependency versioning is using Gradle Version Catalogs feature. See https://docs.gradle.org/current/userguide/platforms.html#sec:version-catalog-plugin
Dependency version are defined in `gradle/libs.versions.toml` and helper functions defined in `dependencies.gradle` file which can be used to add a set of reusable dependencies. For example testing dependencies come in a package that can be reused where needed.

## Links

- [Component Documentation](docs/main.md)
- [Directory Structure](docs/directory_structure.md)
- [Resource Visibility](docs/resource_visibility.md)
- [Contributing](docs/contributing.md)
- [Release Guide](docs/release_guide.md)
- [Material Components for Android](https://github.com/material-components/material-components-android) (external)
