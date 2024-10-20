package CompletableFuture;

import CompletableFuture.DTO.Employee;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class EmployeeReminderService {
    ObjectMapper mapper = new ObjectMapper();

    public void SendReminderToEmployee() {
        CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("Thread Name: " + Thread.currentThread().getName());
                return mapper.readValue(new File("C:\\Users\\Welcome\\Downloads\\employees.json"), new TypeReference<List<Employee>>() {});
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).thenApply(employees -> {
            System.out.println("Thread Name: " + Thread.currentThread().getName());
            return employees.stream()
                    .filter(employee -> "TRUE".equals(employee.getNewJoiner()))
                    .collect(Collectors.toList()); // Fixed syntax and added missing import
        }).thenApply( (employees)->{
            System.out.println("Thread Name for employees whose training is pending: " + Thread.currentThread().getName());
           return employees.stream()
                   .filter(employee -> "TRUE".equals(employee.getLearningPending()))
                   .collect(Collectors.toList());
        }).thenApply( (employees) -> {
            System.out.println("Thread Name for emails: " + Thread.currentThread().getName());
            return employees.stream().map(employee -> employee.getEmail()).collect(Collectors.toList());
        })
    }


    public static void main(String[] args) {

    }
}
