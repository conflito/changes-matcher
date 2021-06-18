# Changes-Matcher

TODO describe the purpose of this project and how it is organized.

## Requirements

- Java 1.8
- [Apache Maven](https://maven.apache.org) 3.6.1 or later.

## Building Changes-Matcher

Changes-Matcher uses [Maven](https://maven.apache.org).  To build Changes-Matcher on the command line, install `maven` and then execute the following command:

```bash
mvn compile
```

To create a binary distribution that includes all dependencies you can also use [Maven](https://maven.apache.org) as:

```bash
mvn package
```

Note: to build Changes-Matcher in [Eclipse](https://www.eclipse.org), make sure you have the [M2Eclipse](https://www.eclipse.org/m2e) plugin installed, and import Changes-Matcher as Maven project.  This will ensure that Eclipse uses Maven to build the project.

## Using Changes-Matcher

TODO describe the following command, e.g., what it does and what it returns.

```bash
java -jar target/matcher-0.0.1-SNAPSHOT-jar-with-dependencies.jar \
  --base ... \
  --variant1 ... \
  --variant2 ... \
  --config ... \
  --match_only
```

where
- `--base` TODO and must be specified as `<path>;<path>;(...)`
- `--variant1` first variant file paths arg as `<path>;<path>;(...)`
- `--variant2` first variant file paths arg as `<path>;<path>;(...)`
- `--config` path to the configuration file, e.g., TODO

## Extending Changes-Matcher

TODO how could one extend Changes-Matcher, for example, how could one add more conflict patterns?  Describe the entire process, i.e., from the `.co` file to any change that must be done in the source code (if any).
