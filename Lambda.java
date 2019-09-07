import java.util.List;
import java.util.Arrays;
import static java.util.stream.Collectors.toList;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.util.stream.Collectors.groupingBy;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.function.IntSupplier;

import static java.util.Comparator.comparing;

public class Lambda {
    public static class Trader{     
        private final String name;     
        private final String city;     
        public Trader(String n, String c){         
            this.name = n;         
            this.city = c;     
        }     
        public String getName(){         
            return this.name;     
        }     
        public String getCity(){         
            return this.city;
        }
        public String toString() {
            return this.city + " : " + this.name;
        }
    }

    public static class Transaction{     
        private final Trader trader;     
        private final int year;     
        private final int value;     
        public Transaction(Trader trader, int year, int value){         
            this.trader = trader;         
            this.year = year;         
            this.value = value;     
        }     
        public Trader getTrader(){         
            return this.trader;     
        }     
        public int getYear(){         
            return this.year;     
        }     
        public int getValue(){         
            return this.value;     
        }     
        public String toString(){         
            return "{" + this.trader + ", " +                
            "year: " + this.year + ", " +
            "value: " + this.value + "}";
        }
    }

    public static void main(String[] args) {
        Trader raoul = new Trader("Raoul", "Cambridge"); 
        Trader mario = new Trader("Mario","Milan"); 
        Trader alan = new Trader("Alan","Cambridge"); 
        Trader brian = new Trader("Brian","Cambridge"); 
        List<Transaction> transactions = Arrays.asList(     
            new Transaction(brian, 2011, 300),     
            new Transaction(raoul, 2012, 1000),     
            new Transaction(raoul, 2011, 400),     
            new Transaction(mario, 2012, 710),     
            new Transaction(mario, 2012, 700),     
            new Transaction(alan, 2012, 950));
        List<Transaction> l1 = transactions.stream()
        .filter((i) -> i.getYear() == 2011)
        .sorted((x,y) -> Integer.compare(x.getValue(), y.getValue()))
        .collect(toList());
        System.out.println(l1);

        // prefect
        List<Transaction> l11 = transactions.stream()
        .filter((i) -> i.getYear() == 2011)
        .sorted(comparing(Transaction::getValue))
        .collect(toList());
        System.out.println(l11);


        List<Trader> traders = Arrays.asList(raoul, mario, alan, brian);
        List<String> l2 = traders.stream().map((i) -> i.getCity()).distinct().collect(toList());
        System.out.println(l2);

        List<String> l22 = transactions.stream()
        .map((i) -> i.trader.getCity())
        .distinct()
        .collect(toList());
        System.out.println(l22);

        List<Trader> l3 =  traders.stream()
        .filter((i) -> i.getCity() == "Cambridge")
        .sorted((x,y) -> x.getName().compareTo(y.getName()))
        .collect(toList());
        System.out.println(l3);

        List<Trader> l33 =  transactions.stream()
        .filter((i) -> i.trader.getCity() == "Cambridge")
        .map((i) -> i.trader)
        .distinct()
        .sorted(comparing(Trader::getName))
        .collect(toList());
        System.out.println(l33);

        List<String> l4 = traders.stream()
        .map((i) -> i.getName())
        .sorted((x,y) -> x.compareTo(y))
        .collect(toList());
        System.out.println(l4);

        List<String> l44 = transactions.stream()
        .map((i) -> i.trader.getName())
        .distinct()
        .sorted()
        .collect(toList());
        System.out.println(l44);
        

        boolean l5 = traders.stream().anyMatch((i)-> i.getCity() == "Milan");
        System.out.println(l5);

        long c = transactions.stream()
        .filter((i) -> i.trader.getCity() == "Milan")
        .count();
        System.out.println(c > 0);
        // transactions.stream().anyMatch((i) -> i.trader.getCity() == "Milan")

        List<Integer> l6 = traders.stream()
        .filter((i) -> i.getCity() == "Cambridge")
        .map((i) -> transactions.stream()
            .filter((j) -> j.trader.equals(i))
            .map((j) -> j.getValue())
            .reduce(0, (x, y) -> x+y))
        .collect(toList());
        System.out.println(l6);


        Optional<Integer> l7 =  transactions.stream()
        .map((i)->i.getValue())
        .reduce(Integer::max);

        System.out.println(l7);

        Optional<Integer> min = transactions.stream()
        .map((i)-> i.getValue())
        .reduce(Integer::min);
        if (min.isPresent()) {
            Optional<Transaction> l8 = transactions.stream()
            .filter((i) -> i.getValue() == min.get())
            .findAny();
            System.out.println(l8);
        }

        Optional<Transaction> k =  transactions.stream()
        .reduce((x,y) -> x.getValue() < y.getValue() ? x :y);
        System.out.println(k);

        Optional<Transaction> kk = transactions.stream()
        .min(comparing(Transaction::getValue));
        System.out.println(kk);

        IntStream even =  IntStream.rangeClosed(1, 100)
        .filter((i) -> i % 2 == 0);
        System.out.println(even.count());

        Stream<int[]> dd =  IntStream.rangeClosed(1, 100).boxed()
        .flatMap(i ->
            IntStream.rangeClosed(i, 100)
            .filter(j -> Math.sqrt(i*i + j*j) % 1 == 0 )
            .mapToObj(j -> new int[]{i, j, (int)Math.sqrt(i*i+j*j)})
        );

        /*
        dd.forEach((i) -> {
            for (int var : i) {
                System.out.print(var);
                System.out.print(" ");
            }
            System.out.println("");
        });
        */
        dd.forEach((t) -> System.out.println(t[0] + "," + t[1] + "," + t[2]));

        Stream<double[]> ans =  IntStream.rangeClosed(1, 100).boxed().flatMap(i->
            IntStream
            .rangeClosed(i, 100)
            .mapToObj(j ->
                new double[]{i, j, Math.sqrt(i*i + j*j)}
            ).filter(l -> l[2] % 1 == 0)
        );

        ans.forEach(i -> System.out.println(i[0] + "," + i[1] + "," + i[2]));
    
        Stream<String> s = Stream.of("Java 8 ", "Lambdas ", "In ", "Action");
        s.map(String::toUpperCase).forEach(System.out::println);
        Stream<String> s1 = Stream.empty();
        System.out.println(s1.count());

        int[] numbers = {2,3,5,7,11,13};
        int sum = Arrays.stream(numbers).sum();
        System.out.println(sum);

        long uniqueWords = 0;
        try (Stream<String> lines = Files.lines(Paths.get("data.txt"), Charset.defaultCharset())) {
            uniqueWords = lines
            .flatMap(line -> {
                // System.out.println(line.split(" "));
                Arrays.stream(line.split(" ")).forEach(System.out::println);
                System.out.println("");
                return Arrays.stream(line.split(" "));
            })
            .distinct()
            .count();
        } catch (IOException e) {
            //TODO: handle exception
            System.out.println(e.getMessage());
        }
        System.out.println(uniqueWords);

        Stream.iterate(new int[]{0,1}, t -> new int[]{t[1], t[0]+t[1]})
        .limit(20)
        .map(t -> t[0])
        .forEach(System.out::println);
        
        Stream.generate(Math::random)
        .limit(5)
        .forEach(System.out::println);

        
        IntStream twos = IntStream.generate(new IntSupplier(){
            public int getAsInt() {
                return 2;
            }
        });
        twos.limit(3).forEach(System.out::println);
        
        IntSupplier fib = new IntSupplier(){
        
            private int prev = 0;
            private int cur = 1;
            @Override
            public int getAsInt() {
                int oldprev = prev;
                int ans = prev + cur;
                prev = cur;
                cur = ans;
                return oldprev;
            }
        };

        System.out.println("fib:");
        IntStream fff = IntStream.generate(fib).limit(30);
        fff.forEach(System.out::println);

    }
}
