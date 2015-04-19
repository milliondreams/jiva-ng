Jiva (which is written in Scala) is designed to provide first class support for Java code. This means, for example, that Java code is able to see fully-typed (and generified) core domain objects within a GA run, as opposed to seeing Objects which need to be type-casted to interesting forms at runtime.

Java support comes in the form of:

  * Support for writing [Fitness Functions](FitnessFunctionSupport.md)
  * Support for writing Event [Notification Handlers](EventNotificationSupport.md)

Some factors that complicate the Java support in Jiva are:
  * Scala generics are not visible in Java code _(this is no longer true as of Scala 2.7.2)_. To get around this, the core GA domain interfaces seen by Java are defined in Java-land, and are then implemented by the Scala classes that do the real work.
  * Scala numbers start out as primitives, and are automatically boxed and unboxed as needed. So generic Scala classes that are parameterized with numbers are not type compatible with the Java generic types (which can't work with primitives) that they are supposed to match. For example, a `Gene[boolean]` is not type compatible with a `JGene<Boolean>`. To get around this, the core GA types within Jiva are parameterized with aliases for the corresponding Java types. So, for example, Jiva uses the jbool type for boolean Genes:
```
type jbool = java.lang.Boolean
class BooleanGene (allelex: boolean) extends Gene[jbool](allelex){/*snip*/}
```