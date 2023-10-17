# Upcoming tasks

## Semantic color system

The Figma design template will include a so called semantic color system. What this means is that components in Figma design should
reference colors by their semantic meaning (colorPrimary, colorOnPrimary, etc) instead of raw color values.

This has the benefit of clearly defining the connections between components and colors.
- Designers can use this information when creating and
- Developers don't have to think whether 2 components using the same raw color are actually logically connected.

However, since this system is still in progress on the design side, it cannot be properly implemented in this library.

JIRA task (Mobi Lab internal): [https://labmobi.atlassian.net/browse/P42-190](https://labmobi.atlassian.net/browse/P42-190)

## Compose support

Compose support is not a part of the project (yet). There are 2 main reasons for this at the moment

1. View components can be used from Compose with a simple wrapper.
2. The first component (TextField) is complex enough on the View side to avoid fully reimplementing in Compose in the near future.

However, there will be a version of the library that sets up a Theme for Compose and creates wrappers or clones for the existing widgets as needed.

JIRA task (Mobi Lab internal): [https://labmobi.atlassian.net/browse/P42-194](https://labmobi.atlassian.net/browse/P42-194)
