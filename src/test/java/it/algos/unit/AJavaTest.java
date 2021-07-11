package it.algos.unit;

import it.algos.test.*;
import static org.junit.Assert.*;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.time.*;
import java.time.format.*;
import java.util.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;

/**
 * Project vaadflow14
 * Created by Algos
 * User: gac
 * Date: gio, 19-nov-2020
 * Time: 20:14
 * Unit test di una classe di servizio (di norma) <br>
 * Estende la classe astratta ATest che contiene le regolazioni essenziali <br>
 * Nella superclasse ATest vengono iniettate (@InjectMocks) tutte le altre classi di service <br>
 * Nella superclasse ATest vengono regolati tutti i link incrociati tra le varie classi classi singleton di service <br>
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("JavaServiceTest")
@DisplayName("Test di unit")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AJavaTest extends ATest {


    private static void printNamesConsumer(String consumer) {
        System.out.println(consumer);
    }

    /**
     * Qui passa una volta sola, chiamato dalle sottoclassi <br>
     * Invocare PRIMA il metodo setUpStartUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeAll
    void setUpAll() {
        super.setUpStartUp();
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Qui passa ad ogni test delle sottoclassi <br>
     * Invocare PRIMA il metodo setUp() della superclasse <br>
     * Si possono aggiungere regolazioni specifiche <br>
     */
    @BeforeEach
    void setUpEach() {
        super.setUp();
    }

    @Test
    @Order(1)
    @DisplayName("1 - Function (from 8)")
    void function() {
        Function<Long, Long> adder = value -> value + 5;
        Long resultLambda = adder.apply((long) 8);
        System.out.println("resultLambda = " + resultLambda);

        Function<String, String> upper = value -> value.toUpperCase();
        ottenuto = upper.apply("sopra");
        System.out.println("resultLambda = " + ottenuto);
    }

    @Test
    @Order(2)
    @DisplayName("2 - Lambda (from 8)")
    void lambda() {
        List<Integer> numbers = Arrays.asList(5, 9, 8, 1);
        numbers.forEach(n -> System.out.println(n));

        List<String> lista = Arrays.asList("alfa", "beta", "gamma", "delta");
        lista.forEach(n -> System.out.println(n));

        Runnable funzione = () -> System.out.println("Funziona");
        funzione.run();
    }

    @Test
    @Order(3)
    @DisplayName("3 - Supplier (from 8)")
    void supplier() {
        // This function returns a random value.
        Supplier<Double> randomValue = () -> Math.random();

        // Print the random value using get()
        System.out.println(randomValue.get());
        System.out.println(randomValue.get());
    }

    @Test
    @Order(4)
    @DisplayName("4 - Supplier again")
    void supplier4() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Supplier<LocalDateTime> s = () -> LocalDateTime.now();
        LocalDateTime time = s.get();

        System.out.println("Non formattato: " + time);

        Supplier<String> s1 = () -> dtf.format(LocalDateTime.now());
        String time2 = s1.get();

        System.out.println("Formattato: " + time2);
    }

    @Test
    @Order(5)
    @DisplayName("5 - Supplier more")
    void supplier5() {
        Supplier<String> supplier = () -> "Marcella bella";
        System.out.println(supplier.get());
    }

    @Test
    @Order(6)
    @DisplayName("6 - Supplier in stream")
    void supplier6() {
        Supplier<Integer> randomNumbersSupp = () -> new Random().nextInt(10);
        Stream.generate(randomNumbersSupp)
                .limit(5)
                .forEach(System.out::println);
    }

    @Test
    @Order(7)
    @DisplayName("7 - Supplier student")
    void supplier7() {
        Supplier<Student> studentSupplier = () -> new Student(1, "Beretta", "M", 19);
        Student student = studentSupplier.get();
        System.out.println(student);

        studentSupplier = () -> new Student(2, "Mantovani", "F", 21);
        student = studentSupplier.get();
        System.out.println(student);
    }

    //    @Test
    //    @Order(8)
    //    @DisplayName("8 - Supplier strings")
    //    void supplier8() {
    //        System.out.println("Java8 Supplier strings\n");
    //
    //        List<String> names = Arrays.asList("Harry", "Daniel", "Lucifer", "April O' Neil");
    //        names.stream().forEach((item) -> { printNamesSupplier(() -> item); });
    //    }

    @Test
    @Order(9)
    @DisplayName("9 - Consumer (from 8)")
    void consumer() {
        System.out.println("Java8 Consumer\n");

        Consumer<String> consumer = AJavaTest::printNamesConsumer;
        consumer.accept("C++");
        consumer.accept("Java");
        consumer.accept("Python");
        consumer.accept("Ruby On Rails");
    }

    @Test
    @Order(10)
    @DisplayName("10 - Var keyword (from 10)")
    void keyword() {
        var lista = List.of("one", "two", "three");
        lista.forEach(System.out::println);
        Object obj = null;
        //        if (obj instanceof String ) {
        //            System.out.println(s.contains("hello"));
        //        }

        //        System.out.println("Java10 \n");
        //
        //        var str = "a test string";
        //        var aVariable = "Marco";
        //        System.out.println(aVariable);
        //        var anotherVariable = 182;
        //        System.out.println(anotherVariable);
    }

    @Test
    @Order(11)
    @DisplayName("11 - find regex")
    void regex() {
        boolean status;
        String tag1 = "{|class=\"wikitable";
        String tag2 = "{| class=\"wikitable";
        String tag3 = "{|  class=\"wikitable";
        String tag4 = "{|class=\"sortable wikitable";
        String tag5 = "{| class=\"sortable wikitable";
        String tag6 = "{|  class=\"sortable wikitable";
        String tag7 = "{|   class=\"sortable wikitable";
        String tagEnd = "|}\n";

        String sor0 = "Mario non sapeva cosa fare";
        String sor1 = "Mario non sapeva " + tag1 + " cosa fare";
        String sor2 = "Mario non sapeva " + tag2 + " cosa fare";
        String sor3 = "Mario non sapeva " + tag3 + " cosa fare";
        String sor4 = "Mario non sapeva " + tag4 + " cosa fare";
        String sor5 = "Mario non sapeva " + tag5 + " cosa fare";
        String sor6 = "Mario non sapeva " + tag6 + " cosa fare";
        String sor7 = "Mario non sapeva " + tag7 + " cosa fare";

        List<String> tags = Arrays.asList(tag1, tag2, tag3, tag4, tag5, tag6);
        List<String> sorgs = Arrays.asList(sor1, sor2, sor3, sor4, sor5, sor6);

        for (String sor : sorgs) {
            status = false;
            for (String tag : tags) {
                if (sor.contains(tag)) {
                    status = true;
                }
            }
            assertTrue(status);
        }

        String patterns = Pattern.quote("wikitable");
        Pattern pattern = Pattern.compile(patterns);
        Matcher matcher = pattern.matcher((String) sor1);
        int num = matcher.groupCount();
        matcher.matches();
        //        assertTrue(matcher.matches());
    }


    /**
     * Qui passa al termine di ogni singolo test <br>
     */
    @AfterEach
    void tearDown() {
    }


    /**
     * Qui passa una volta sola, chiamato alla fine di tutti i tests <br>
     */
    @AfterEach
    void tearDownAll() {
    }


    public class Student {

        private int id;

        private String name;

        private String gender;

        private int age;

        public Student(int id, String name, String gender, int age) {
            super();
            this.id = id;
            this.name = name;
            this.gender = gender;
            this.age = age;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "Student [id=" + id + ", name=" + name + ", gender=" + gender + ", age=" + age + "]";
        }

    }

}