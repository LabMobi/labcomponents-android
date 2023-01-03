Mobi Lab

# ComponentsDemo README

Here we demo all custom UI components and additions, that we have made.

## Description

TODO: A small description of the project: what is it about and what does it allow to do.


## Links

### Issue tracker

Project at JIRA: TODO

### Documentation

Project at Confluence: TODO

## Workflow

### Release guide and management

See the Release_Guide.md

### Branching strategy

Default Mobi Lab branching strategy, see https://confluence.lab.mobi/display/DEV/Git+branching+strategy

### Versioning rules


See the `app/build.gradle`


### Testing

TODO: rules and requirements, which types of tests are setup and used, which devices are used for testing, access to physical and emulated testing hardware.

There are sample unit tests written in both `app/src/test` and `app-domain/src/test`


## Project structure

The project uses Kotlin as the main language. See https://kotlinlang.org/docs/reference/ for more information about Kotlin.

`Ktlint` is added to the project to keep code style and formatting uniform. Ktlint is run with regular lint checks during a build (specifically `gradle check` command). For more information about Ktlint, see https://github.com/shyiko/ktlint

We have different modules for different parts of the application:
* app -> the actual Android application code
* app-infrastructure -> Implementations for communicating with the outside world. Storage, network, Android platform specifics, etc. For example, Gateway implemenations that UseCases use or Android specific biometrics, location service implementations.
* app-domain -> Usecases and entities used within the application. This module defines what it wants to use via interfaces called Gateways. Implementations are usually provided by module_infrastructure (Providers).
* app-common -> Common elements used throughout the modules
* lib-* -> library modules. Copied and modified libraries that we need

### Reminders

* `app-infrastructure` has different packages for dto classes. Use @Keep annotation to keep these classes when R8 runs

### Setup

TODO: Info about the project setup and architecture. Link to architecture plan.

TODO: Frameworks if any.

#### Dependency licenses

TODO: Add a task to backlog to update / validate this section before the public release of your application.

TODO: All the template dependency libraries have either an Apache or MIT licenses. The general rule is that you need to discuss it with your team and/or client if you include anything with a different type of a license. See the Client Journey for more.

#### Dependency versioning

Dependency versioning is using Gradle Version Catalogs feature. See https://docs.gradle.org/current/userguide/platforms.html#sec:version-catalog-plugin
Dependency version are defined in `gradle/libs.versions.toml` and helper functions defined in `dependencies.gradle` file which can be used to add a set of reusable dependencies. For example testing dependencies come in a package that can be reused where needed.

TODO: Sub-module information if any.

TODO: Info about agreed-on technical limitations

### Application variants

TODO: App flavors / targets / environments
