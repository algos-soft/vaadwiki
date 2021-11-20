JAVA
======================
##Versioni
![](/Users/gac/Documents/IdeaProjects/Operativi/Vaadflow14/doc/oracle.png)


- Java version history [wikipedia](https://en.wikipedia.org/wiki/Java_version_history)
- Versions and Features [marcobehler](https://www.marcobehler.com/guides/a-guide-to-java-versions-and-features)
- Guide to java versions [dzone](https://dzone.com/articles/a-guide-to-java-versions-and-features)
- Java LTS Releases [ippon](https://blog.ippon.tech/comparing-java-lts-releases/)
- Java’s Time-Based Releases [baeldung](https://www.baeldung.com/java-time-based-releases)
-  Java versions [codejava](https://www.codejava.net/java-se/java-se-versions-history)

        Java 1.2 uses major version 46
        Java 1.3 uses major version 47
        Java 1.4 uses major version 48
        Java 5 uses major version 49
        Java 6 uses major version 50
        Java 7 uses major version 51
        Java 8 uses major version 52
        Java 9 uses major version 53
        Java 10 uses major version 54
        Java 11 uses major version 55
        Java 12 uses major version 56
        Java 13 uses major version 57
        Java 14 uses major version 58
        Java 15 uses major version 59
        Java 16 uses major version 60
        Java 17 uses major version 61


##Lambdas
- Staring from 8

In its simple form, a lambda could be represented as a comma-separated list of parameters, the –> symbol and the body.
    
    (parameter) -> body
To use more than one parameter, wrap them in parentheses:     
    
    (parameter1, parameter2) -> expression
Expressions are limited. They have to immediately return a value, and they cannot contain variables, assignments or statements such as if or for. In order to do more complex operations, a code block can be used with curly braces. If the lambda expression needs to return a value, then the code block should have a return statement.

    (parameter1, parameter2) -> { code block } 

- List<Integer> numbers = Arrays.asList(11, 22, 33, 44, 99, 100); 
- numbers.forEach (number -> System.out.print(number + " ")); 
- numbers.forEach (System.out::println);

- Using Lambda Expressions [w3schools](https://www.w3schools.com/java/java_lambda.asp)




##Interfaces
- Staring from 8
- But with higher-order functions, we can perform the following operations:
- We can pass a function to a function.
- We can create a function within function.
- We can return a function from a function.

     
     Function(T,R): Takes one parameter type (T), produces one result (R).
     Consumer(T): Takes one parameter type (T), produces nothing (void).
     Supplier(R): Takes no parameter type (void), produces one result (R).
     Predicate(T): Takes one parameter type (T), produces boolean value.
     Runnable: Takes no parameter type (void), produces nothing (void).
     BiConsumer(T,U): Takes two parameters type (T,U), produces nothing (void).
     BiFunction(T,U,R): Takes two parameters type (T,U), produces one result (R).
     UnaryOperator(T): Takes one parameter type (T), produces one result (T).
     BinaryOperator(T): Takes two parameters of type (T), produces one result (T).


####Function
Function is a functional interface; it takes an argument (object of type T) and returns an object (object of type R). The argument and output can be a different type.
    
    @FunctionalInterface
    public interface Function<T, R> {
          R apply(T t);
    }
- T – Type of the input to the function.
- R – Type of the result of the function.
- 
####Consumer
    
    @FunctionalInterface
    public interface Consumer<T> {
      void accept(T t);
    }

####Predicate
Predicate(T): Takes one parameter type (T), produces boolean value
    
    public static Predicate<Integer> pari = i -> i % 2 == 0;
    public static Predicate<Integer> dispari = Predicate.not(pari);

####Supplier      
    @FunctionalInterface
    public interface Supplier<T> {
        T get();
    }

The Supplier Interface is a part of the java.util.function package which has been introduced since Java 8, to implement functional programming in Java. It represents a function which does not take in any argument but produces a value of type T. Hence this functional interface takes in only one generic namely:-

    T: denotes the type of the result

The lambda expression assigned to an object of Supplier type is used to define its get() which eventually produces a value. Suppliers are useful when we don’t need to supply any value and obtain a result at the same time. The Supplier interface consists of only one function:
    
    T get()
    
    // This function returns a random value. 
    Supplier<Double> randomValue = () -> Math.random(); 
  
    // Print the random value using get() 
    System.out.println(randomValue.get());     
    

This method does not take in any argument but produces a value of type T.

In all scenarios where there is no input to an operation and it is expected to return an output, the in-built functional interface Supplier<T> can be used without the need to define a new functional interface every time.

There are primitive specializations of the Supplier interface:
- IntSupplier having one abstract method getAsInt()
- LongSupplier having one abstract method getAsLong()
- DoubleSupplier having one abstract method getAsDouble()
- BooleanSupplier having one abstract method getAsBoolean()

##Collection
- Staring from 8/9


    List<String> list = List.of("one", "two", "three");
    Set<String> set = Set.of("one", "two", "three");
    Map<String, String> map = Map.of("foo", "one", "bar", "two");
##Streams
- Staring from 8/9


    Stream<String> stream = Stream.iterate("", s -> s + "s").takeWhile(s -> s.length() < 10);
##Optionals
- Staring from 8/9


    user.ifPresentOrElse(this::displayAccount, this::displayLogin);
##JShell
- Staring from 9

      % jshell
      |  Welcome to JShell -- Version 9
      |  For an introduction type: /help intro

      jshell> int x = 10
      x ==> 10
      
##Modules
##Deprecated enhancement
- Staring from 9

      @Deprecated(since = "4.5", forRemoval = true)
          public int calculate(Machine machine) {
    	      return machine.exportVersions().size() * 10;
    	  }
    	  

##var keyword
- Staring from 10

1. We can declare any datatype with the var keyword.
2. var can be used in a local variable declaration.
3. var cannot be used in an instance and global variable declaration.
4. var cannot be used as a Generic type.
5. var cannot be used with the generic type.
6. var cannot be used without explicit initialization.
7. var cannot be used with Lambda Expression.
8. var cannot be used for method parameters and return type.


     var str = "Java 10"; // infers String
     var list = new ArrayList<String>(); // infers ArrayList<String>
     var stream = list.stream(); // infers Stream<String>
     var bos = new ByteArrayOutputStream();
     var list = List.of(1, 2.0, "3") // infers List<? extends Serializable & Comparable<..>>
- var keyword in Java [geeksforgeeks](https://www.geeksforgeeks.org/var-keyword-in-java/)

##Strings
- Staring from 11

- String.repeat(int)
- String.strip*()
- String.stripLeading()
- String.stripLeading()
- String.isBlank()
- String.lines()
- Java 11 String API Additions [baeldung](https://www.baeldung.com/java-11-string-api/)


##Switch Expression
- Staring from 14

- Java 14 extends switch so it can be used as either a statement or an expression


    boolean result = switch (status) {
        case SUBSCRIBER -> true;
        case FREE_TRIAL -> false;
        default -> throw new IllegalArgumentException("something is murky!");
    };    
   
    int numLetters = switch (day) {
        case MONDAY, FRIDAY, SUNDAY -> 6;
        case TUESDAY                -> 7;
        default      -> {
          String s = day.toString();
          int result = s.length();
          yield result;
        }
    };

    enum Person {Mozart, Picasso, Goethe, Dostoevsky, Prokofiev, Dali}

    String title = switch (person) {
    case Dali, Picasso      -> "painter";
    case Mozart, Prokofiev  -> "composer";
    case Goethe, Dostoevsky -> "writer";
    default                 -> "...";                     
    };

- A Look at the New ‘Switch’ Expressions in Java 14 [betterprogramming](https://betterprogramming.pub/a-look-at-the-new-switch-expressions-in-java-14-ed209c802ba0/)

##Multiline Strings
- Staring from 15
- Text Blocks
- We can use Text Blocks by declaring the string with  """ (three double-quote marks):


    public String textBlocks() {
    return """
    Get busy living
    or
    get busy dying.
    --Stephen King""";
    }
- Java Multi-line String [baeldung](https://www.baeldung.com/java-multiline-string/)


##Record
- Staring from 16

    final class Point {
        public final int x;
        public final int y;    

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
      record Point(int x, int y) { }


##InstanceOf      
- Staring from 16
    
    
    if (obj instanceof String) {
        String s = (String) obj;
        // use s
    }
    
    if (obj instanceof String s) {
        System.out.println(s.contains("hello"));
    }
    

    else if(customer instanceof BusinessCustomer bCustomer)
    {
    customerName = bCustomer.getLegalName();
    }

##Parser
Gson, Jackson, and Moshi 
- gson-vs-jackson-vs-moshi [ericdecanini](https://www.ericdecanini.com/2020/09/29/gson-vs-jackson-vs-moshi-the-best-android-json-parser/)
- gson-vs-jackson [javacodegeeks](https://examples.javacodegeeks.com/jackson-vs-gson-a-deep-dive/)
##Tutorial
- Java tutorials [howtodoinjava](https://howtodoinjava.com/)
- Java tutorials [mkyong]https://mkyong.com/tutorials/java-8-tutorials/)
