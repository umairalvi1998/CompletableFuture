package CompletableFuture;

import CompletableFuture.DTO.Employee;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class EmployeeReminderService {
    ObjectMapper mapper = new ObjectMapper();

    public CompletableFuture<Void> SendReminderToEmployee() {
    CompletableFuture<Void> voidCompletableFuture =
            CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("Fetching Employee Stage Thread Name: " + Thread.currentThread().getName());
                return mapper.readValue(new File("C:\\Users\\Welcome\\Downloads\\employees.json"), new TypeReference<List<Employee>>() {});
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).thenApplyAsync(employees -> {
            System.out.println("Filtering Employees Stage Thread Name: " + Thread.currentThread().getName());
            return employees.stream()
                    .filter(employee -> "TRUE".equals(employee.getNewJoiner()))
                    .collect(Collectors.toList()); // Fixed syntax and added missing import
        }).thenApplyAsync( (employees)->{
            System.out.println("Thread Name for employees whose training is pending: " + Thread.currentThread().getName());
           return employees.stream()
                   .filter(employee -> "TRUE".equals(employee.getLearningPending()))
                   .collect(Collectors.toList());
        }).thenApplyAsync( (employees) -> {
            System.out.println("Fetching emails Stage Thread Name for emails: " + Thread.currentThread().getName());
            return employees.stream().map(employee -> employee.getEmail()).collect(Collectors.toList());
        }).thenAccept((emails)->
                emails.forEach(this::sendEmailTo)
        );

      return  voidCompletableFuture;
    }

     public void sendEmailTo(String email) {
        System.out.println("Sending Training reminder Email to Employee : " + email);
     }

    public static void main(String[] args) {
       EmployeeReminderService employeeReminderService = new EmployeeReminderService();
        try {
            employeeReminderService.SendReminderToEmployee().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
