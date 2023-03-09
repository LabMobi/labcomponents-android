# Resource Visibility

**Components library's internal resources have a `lab_internal` prefix instead of `lab` prefix.**

The library's internal resources are all public by default. We might change this decision in the future, but as of know
it doesn't make much sense to protect ourselves from ourselves since we are the only integrators.

We could make resources private by default but it creates an additional overhead when making changes and making sure there
are no regressions. 
