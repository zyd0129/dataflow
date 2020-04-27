import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

public class Test {
    public static void main(String[] args) {
        Person person = new Person();
        person.setName("zyd");
        System.out.println(person);
        Integer a =1;
        System.out.println(a.hashCode());
    }

    public void sink(Person person) {
        System.out.println(person);
    }

    @Data
    private static class Person {
        private String name;
        private Integer age;
    }
}
