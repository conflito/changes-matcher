# Changes-Matcher

[![Build Status](https://github.com/conflito/changes-matcher/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/conflito/changes-matcher/actions/workflows/maven.yml)

[Changes-Matcher](https://github.com/conflito/changes-matcher) is a tool that
aims to identify whether any common cause of semantic conflicts (e.g., parallel
modifications to the same method) has occurred in a given merge commit.

[Changes-Matcher](https://github.com/conflito/changes-matcher) starts by
computing the differences between the base version of a merge commit and each
variant (i.e., branch).  Then, it compares these changes to a set of change
patterns that capture potential causes for semantic conflicts.  If the changes
correspond to an instance of a pattern,
[Changes-Matcher](https://github.com/conflito/changes-matcher) has successfully
identified a semantic conflict in the merge commit.

## Requirements

- Java 1.8
- [Apache Maven](https://maven.apache.org) 3.6.1 or later.

## Building Changes-Matcher

[Changes-Matcher](https://github.com/conflito/changes-matcher) uses
[maven](https://maven.apache.org).  To build the
[Changes-Matcher](https://github.com/conflito/changes-matcher) tool on the
command line, install [maven](https://maven.apache.org) and then execute the
following command:

```bash
mvn compile
```

To create a binary distribution that includes all dependencies you can also use
[maven](https://maven.apache.org) as:

```bash
mvn package
```

To ease any usage of the [Changes-Matcher](https://github.com/conflito/changes-matcher) tool,
either in the following example or not, export the following environment variable

```bash
export CHANGES_MATCHER_JAR="$(pwd)/target/org.conflito.changes-matcher-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
```

Tip: before going ahead, verify whether the environment variables are correctly
initialized and pointing to existing files.

```bash
[ -s "$CHANGES_MATCHER_JAR" ] || echo "$CHANGES_MATCHER_JAR does not exist or it is empty!"
```

## Using Changes-Matcher

In a common merge scenario, i.e., a base version, two branches (variant 1 and 2),
and one merge version are required to run the
[Changes-Matcher](https://github.com/conflito/changes-matcher) tool.

As a usage example, let us consider the project https://github.com/netty/netty
and its merge commit
[193acdb36cd3da9bfc62dd69c4208dff3f0a2b1b](https://github.com/netty/netty/tree/193acdb36cd3da9bfc62dd69c4208dff3f0a2b1b).
In detail,

- Repository URL: [https://github.com/netty/netty.git](https://github.com/netty/netty.git)
- Merge commit: [193acdb36cd3da9bfc62dd69c4208dff3f0a2b1b](https://github.com/netty/netty/commit/193acdb36cd3da9bfc62dd69c4208dff3f0a2b1b)
- Variant 1 commit: [b3b096834cafc7f348583786d71567e9fa001b55](https://github.com/netty/netty/commit/b3b096834cafc7f348583786d71567e9fa001b55)
- Variant 2 commit: [c2417c253c48bac942decfe923743d2b09d63a5f](https://github.com/netty/netty/commit/c2417c253c48bac942decfe923743d2b09d63a5f)
- Base commit: [2fc18a00f6ac61a365b73dd498dd2e38f1efa823](https://github.com/netty/netty/commit/2fc18a00f6ac61a365b73dd498dd2e38f1efa823)

Note: the base commit can automatically be extracted from project's git
repository, e.g.,

```bash
git merge-base --octopus b3b096834cafc7f348583786d71567e9fa001b55 c2417c253c48bac942decfe923743d2b09d63a5f
```

### Setup

0. Pre step

```bash
mkdir /tmp/changes-matcher-motivational-example
cd /tmp/changes-matcher-motivational-example
```

1. Clone project's repository

```bash
git clone --bare https://github.com/netty/netty.git netty-repository.git
```

2. Get base, variant 1, variant 2, and merge versions

```bash
# Base
git clone netty-repository.git base
(cd base; git checkout 2fc18a00f6ac61a365b73dd498dd2e38f1efa823)

# Variant 1
git clone netty-repository.git variant-1
(cd variant-1; git checkout b3b096834cafc7f348583786d71567e9fa001b55)

# Variant 2
git clone netty-repository.git variant-2
(cd variant-2; git checkout c2417c253c48bac942decfe923743d2b09d63a5f)

# Merge
git clone netty-repository.git merge
(cd merge; git checkout 193acdb36cd3da9bfc62dd69c4208dff3f0a2b1b)
```

3. Build variant 1, variant 2, and merge versions

```bash
# Variant 1
(cd variant-1; mvn clean compile -Dmaven.repo.local="$(pwd)/.m2")

# Variant 2
(cd variant-2; mvn clean compile -Dmaven.repo.local="$(pwd)/.m2")

# Merge
(cd merge; mvn clean compile -Dmaven.repo.local="$(pwd)/.m2")
```

4. Collect variant 1's, variant 2's, and merge's dependencies

```bash
# Variant 1
(cd variant-1; mvn dependency:copy-dependencies -Dmaven.repo.local="$(pwd)/.m2" -DoutputDirectory="$(pwd)/.deps")

# Variant 2
(cd variant-2; mvn dependency:copy-dependencies -Dmaven.repo.local="$(pwd)/.m2" -DoutputDirectory="$(pwd)/.deps")

# Merge
(cd merge; mvn dependency:copy-dependencies -Dmaven.repo.local="$(pwd)/.m2" -DoutputDirectory="$(pwd)/.deps")
```

5. Collect variant 1's, variant 2's, and merge's classpath

```bash
# Variant 1
variant_1_classpath=$(cd variant-1; echo $(pwd)/target/classes:$(find .deps -type f -name "*.jar" | grep -v "junit" | grep -v "hamcrest" | sed "s|^|$(pwd)/|g" | tr '\n' ':'))

# Variant 2
variant_2_classpath=$(cd variant-2; echo $(pwd)/target/classes:$(find .deps -type f -name "*.jar" | grep -v "junit" | grep -v "hamcrest" | sed "s|^|$(pwd)/|g" | tr '\n' ':'))

# Merge
merge_classpath=$(cd merge; echo $(pwd)/target/classes:$(find .deps -type f -name "*.jar" | grep -v "junit" | grep -v "hamcrest" | sed "s|^|$(pwd)/|g" | tr '\n' ':'))
```

6. Collect the set of `.java` files involved in the merge commit

```bash
# Get set of files involved in the merge commit
modified_files=$(cd merge; git diff --name-only HEAD^1 src/main/java)

# Path to the files involved in the merge commit under base/
base_modified_files=$(echo "$modified_files" | sed "s|^|$(pwd)/base/|g" | tr '\n' ';')

# Path to the files involved in the merge commit under variant-1/
variant_1_modified_files=$(echo "$modified_files" | sed "s|^|$(pwd)/variant-1/|g" | tr '\n' ';')

# Path to the files involved in the merge commit under variant-2/
variant_2_modified_files=$(echo "$modified_files" | sed "s|^|$(pwd)/variant-2/|g" | tr '\n' ';')
```

### Semantic Conflict Detection

[Changes-Matcher](https://github.com/conflito/changes-matcher) relies on a
configuration file that allows one for some customizable options.  For the
project example the configuration file (e.g., `netty-193acdb-configuration.txt`)
can be configured as

```
# Absolute path to the source directory of the base version
base.src.dir=/tmp/changes-matcher-motivational-example/base/src/main/java

# Absolute path to the source directory of the first variant version
var1.src.dir=/tmp/changes-matcher-motivational-example/variant-1/src/main/java

# Absolute path to the source directory of the second variant version
var2.src.dir=/tmp/changes-matcher-motivational-example/variant-2/src/main/java

# Absolute classpath of the first variant version
var1.cp.dir= ... i.e., the content of the $variant_1_classpath variable

# Absolute classpath of the second variant version
var2.cp.dir= ... i.e., the content of the $variant_2_classpath variable

# Absolute classpath of the merge version
merge.cp.dir= ... i.e., the content of the $merge_classpath variable
```

Once the configuration file is in place, the
[Changes-Matcher](https://github.com/conflito/changes-matcher) tool can be
executed as

```bash
java -jar "$CHANGES_MATCHER_JAR" \
  --base <path>[;<path>] \
  --variant1 <path>[;<path>] \
  --variant2 <path>[;<path>] \
  --config <path>
```

where

- `--base` are the absolute paths to the base version of the files modified in the merge version, use `;` to define more than one path
- `--variant1` are the absolute paths to the first variant version of the files modified in the merge version, use `;` to define more than one path
- `--variant2` are the absolute paths to the second version of the files modified in the merge version, use `;` to define more than one path
- `--config` is the absolute path to the configuration file

Upon a successful execution, this command writes to the stdout the (variable,
value) pairings and the testing goals (target class and methods to cover) for
the test generation step.

Note: By default, [Changes-Matcher](https://github.com/conflito/changes-matcher)
attempts to match any of the known patterns.  However, the `--conflict_name <pattern name>`
option could be used to search for a particular change pattern.  One can also
use the ``--list_patterns`` option to list the accepted names.

For the running example, one could run the command above as

```bash
java -jar "$CHANGES_MATCHER_JAR" \
  --base "$base_modified_files" \
  --variant1 "$variant_1_modified_files" \
  --variant2 "$variant_2_modified_files" \
  --config netty-193acdb-configuration.txt \
  --output_file netty-193acdb-matches.txt
```

which write to `netty-193acdb-matches.txt` the following content

```
[
  {
    "conflictName" : "Parallel Changes",
    "variableAssignments" : [
      {
        "variable" : 0,
        "value" : "org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder"
      },
      {
        "variable" : 1,
        "value" : "decode(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, org.jboss.netty.buffer.ChannelBuffer)"
      }
    ],
    "testingGoal" : {
      "targetClass" : "org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder",
      "coverMethods" : [
        "org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder.decode(Lorg/jboss/netty/channel/ChannelHandlerContext;Lorg/jboss/netty/channel/Channel;Lorg/jboss/netty/buffer/ChannelBuffer;)Ljava/lang/Object;"
      ],
      "coverMethodsLine" : "org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder.decode(Lorg/jboss/netty/channel/ChannelHandlerContext;Lorg/jboss/netty/channel/Channel;Lorg/jboss/netty/buffer/ChannelBuffer;)Ljava/lang/Object;"
    }
  },
  {
    "conflictName" : "Change Method 3",
    "variableAssignments" : [
      {
        "variable" : 0,
        "value" : "org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder"
      },
      {
        "variable" : 1,
        "value" : "org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder"
      },
      {
        "variable" : 2,
        "value" : "decode(org.jboss.netty.channel.ChannelHandlerContext, org.jboss.netty.channel.Channel, org.jboss.netty.buffer.ChannelBuffer)"
      },
      {
        "variable" : 3,
        "value" : "failIfNecessary(org.jboss.netty.channel.ChannelHandlerContext)"
      }
    ],
    "testingGoal" : {
      "targetClass" : "org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder",
      "coverMethods" : [
        "org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder.decode(Lorg/jboss/netty/channel/ChannelHandlerContext;Lorg/jboss/netty/channel/Channel;Lorg/jboss/netty/buffer/ChannelBuffer;)Ljava/lang/Object;",
        "org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder.failIfNecessary(Lorg/jboss/netty/channel/ChannelHandlerContext;)V"
      ],
      "coverMethodsLine" : "org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder.decode(Lorg/jboss/netty/channel/ChannelHandlerContext;Lorg/jboss/netty/channel/Channel;Lorg/jboss/netty/buffer/ChannelBuffer;)Ljava/lang/Object;:org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder.failIfNecessary(Lorg/jboss/netty/channel/ChannelHandlerContext;)V"
    }
  }
]
```

Each `JSON` object in the returned list above informs the developer which
patterns (i.e., semantic conflict) matched, and for each pattern, which class (``targetClass``) and
methods (``coverMethods``) should be tested to trigger/reveal the pattern.  For
the running example, two patterns patched: "Parallel Changes" and "Change Method 3".

- To trigger the "Parallel Changes" pattern, a test generator would have
to generate tests for the class `org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder`
and the method `org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder.decode(Lorg/jboss/netty/channel/ChannelHandlerContext;Lorg/jboss/netty/channel/Channel;Lorg/jboss/netty/buffer/ChannelBuffer;)Ljava/lang/Object;`.

- To trigger the "Change Method 3" pattern, a test generator would have
to generate tests for the class `org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder`
and the methods `org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder.decode(Lorg/jboss/netty/channel/ChannelHandlerContext;Lorg/jboss/netty/channel/Channel;Lorg/jboss/netty/buffer/ChannelBuffer;)Ljava/lang/Object;` and `org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder.failIfNecessary(Lorg/jboss/netty/channel/ChannelHandlerContext;)V`.

## Extending Changes-Matcher

TODO how could one extend [Changes-Matcher](https://github.com/conflito/changes-matcher), for example, how could one add more conflict patterns?  Describe the entire process, i.e., from the `.co` file to any change that must be done in the source code (if any).
