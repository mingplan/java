import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.stream.Collectors;

public class FutureTest {

    public static class Shop {
        private String name;

        public Shop(String name) {
            this.name = name;
        }
        public double getPrice(String product) {
            return calculatePrice(product);
        }

        public static void delay() {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        private double calculatePrice(String product) {
            delay();
            return Math.random() * product.charAt(0) * product.charAt(1);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static void main(String[] args) {
        List<Shop> shops = Arrays.asList(
            new Shop("BestPrice"),
            new Shop("LetsSaveBig"),
            new Shop("MyFavoriteShop"),
            new Shop("BuyItAll")
        );
        String product = "xilll";
        long start = System.nanoTime();
        /*
        shops.stream()
        .map(i -> String.format("%s price is %.2f", i.getName(), i.getPrice(product)))
        .collect(Collectors.toList());
        */

        /*
        List<String> r = shops.parallelStream()
        .map(i -> String.format("%s price is %.2f", i.getName(), i.getPrice(product)))
        .collect(Collectors.toList());
        r.stream().forEach(System.out::println);
        */

        Executor executor = 
            Executors.newFixedThreadPool(Math.min(shops.size(), 100), new ThreadFactory(){
            
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setDaemon(true);
                    return t;
                }
            });

        List<CompletableFuture<String>> pf = 
            shops.stream()
            .map(i -> CompletableFuture.supplyAsync(
                () -> String.format("%s price is %.2f", i.getName(), i.getPrice(product)), executor
            ))
            .collect(Collectors.toList());
        List<String> r = pf.stream()
        .map(CompletableFuture::join)
        .collect(Collectors.toList());
        r.stream().forEach(System.out::println);
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done i " + duration + " msecs");
        
    }
}
