# nf-sandbox

This repository contains a minimal Nextflow plugin that can be used as a sandbox for experimenting with plugin development.

## Plugin structure

The plugin provides a minimal structure with:
- Basic plugin class (`SandboxPlugin.groovy`)
- Required manifest configuration
- No observers or extensions
- Gradle build configuration

## Build

To build the plugin:

```bash
./gradlew build
```

## Installation

To install the plugin:

```bash
./gradlew installPlugin
```

This will install the plugin to one of these locations (in order of precedence):
1. Directory specified by `NXF_PLUGINS_DIR` environment variable
2. `$NXF_HOME/plugins` directory (if `NXF_HOME` is set)
3. `$HOME/.nextflow/plugins` directory

## License

Apache License, Version 2.0
