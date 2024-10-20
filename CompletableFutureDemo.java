package CompletableFuture;

import CompletableFuture.DTO.Employee;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class CompletableFutureDemo {

    public Void saveEmployees(File employeeJson) throws ExecutionException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        CompletableFuture<Void> future = CompletableFuture.runAsync( () -> {
            try {
                List<Employee> employees = mapper.readValue(employeeJson, new TypeReference<List<Employee>>() {
                });
                System.out.println("Thread Name " + Thread.currentThread().getName());
                employees.stream().forEach( (employee) -> {System.out.println(employee);});
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        return future.get();
    }

    public  static void main(String[] args) throws ExecutionException, InterruptedException {
        CompletableFutureDemo demo = new CompletableFutureDemo();
        demo.saveEmployees(new File("C:\\Users\\Welcome\\Downloads\\employees.json"));
    }
}
