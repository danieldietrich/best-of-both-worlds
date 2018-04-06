[![Build Status](https://travis-ci.org/danieldietrich/best-of-both-worlds.png)](https://travis-ci.org/danieldietrich/best-of-both-worlds)
[![codecov](https://codecov.io/gh/danieldietrich/best-of-both-worlds/branch/master/graph/badge.svg)](https://codecov.io/gh/danieldietrich/best-of-both-worlds)

This project contains the source code of the german [Java Magazin 3.18](https://jaxenter.de/ausgaben/java-magazin-3-18) article ["Das Beste aus beiden Welten: Objektfunktionale Programmierung mit Vavr"](https://entwickler.de/leseproben/vavr-objektfunktional-programmieren-579827775.html) by [Daniel Dietrich](https://twitter.com/danieldietrich).

## Prerequisites

In order to run the examples, [Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git) and [JDK 8+](http://www.oracle.com/technetwork/java/javase/downloads/index.html) are needed.

Then clone the source code:

```bash
git clone https://github.com/danieldietrich/best-of-both-worlds.git
cd best-of-both-worlds
```

## Run the examples

The build is based on Gradle. The following commands run the tests:

```bash
# windows
gradlew test

# linux, mac
./gradlew test 
```

## Notes on how to get further

Most of Java's functional interfaces do not handle checked exceptions, like [java.util.function.Function](https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html).

If you are interested in improving the code of this repository, you could make operations like `map` and `flatMap` of [Try](https://github.com/danieldietrich/best-of-both-worlds/blob/master/src/main/java/io/vavr/bobw/Try.java) and [Future](https://github.com/danieldietrich/best-of-both-worlds/blob/master/src/main/java/io/vavr/bobw/Future.java) safe in the way that they take a new type `CheckedFunction` (see below) instead of [Function](https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html).

```java
@FunctionalInterface
interface CheckedFunction<T, R> {
    R apply(T t) throws Exception;    
}
```

Please also take a look at [Vavr](https://github.com/vavr-io/vavr).