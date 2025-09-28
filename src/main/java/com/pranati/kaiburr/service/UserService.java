package com.pranati.kaiburr.service;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.pranati.kaiburr.model.ExectionTimes;
import com.pranati.kaiburr.model.Task;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final MongoCollection<Document> userCollection;
    public UserService(MongoDatabase database) {
        this.userCollection = database.getCollection("Tasks");
    }
    public void addTask(Task task){
        List<ExectionTimes> exectionTimes = new ArrayList<>();
        Document doc = new Document("id" , task.getId())
                .append("name", task.getName())
                .append("owner", task.getOwner())
                .append("command", task.getCommand())
                .append("taskExecutions", exectionTimes);
        userCollection.insertOne(doc);
    }
    public String deleteTask(String id) {
        Document query = new Document("id", id);
        long deletedCount = userCollection.deleteOne(query).getDeletedCount();

        if (deletedCount > 0) {
            return ("Task with id " + id + " deleted successfully!");
        } else {
            return ("Task with id " + id + " not found!");
        }
    }
    public List<Task> getTasks(String id) {
        List<Task> results = new ArrayList<>();

        if (id != null && !id.isEmpty()) {
            Document query = new Document("id", id);
            Document doc = userCollection.find(query).first();
            if (doc != null) {
                results.add(convertResults(doc));
            }
        } else {
            FindIterable<Document> docs = userCollection.find();
            for (Document doc : docs) {
                results.add(convertResults(doc));
            }
        }
        System.out.println(results);

        return results;
    }
    public Task convertResults(Document doc){
        List<Document> taskExecutions = doc.getList("taskExecutions", Document.class);
        List<ExectionTimes> exectionTimes = new ArrayList<>();
        for (Document taskExecution : taskExecutions) {
            ExectionTimes exectionTime = new ExectionTimes();
            exectionTime.setStartTime(taskExecution.getString("startTime"));
            exectionTime.setEndTime(taskExecution.getString("endTime"));
            exectionTime.setMessage(taskExecution.getString("message"));
            exectionTimes.add(exectionTime);
        }
        Task task = new Task();
        task.setId(doc.getString("id"));
        task.setName(doc.getString("name"));
        task.setOwner(doc.getString("owner"));
        task.setCommand(doc.getString("command"));
        task.setTaskExecutions(exectionTimes);
        return task;
    }
    public String ExecuteCommand(String id, ExectionTimes exectionTimes){
        Document query = new Document("id", id);
        Document doc = new Document()
                .append("startTime", exectionTimes.getStartTime())
                .append("endTime", exectionTimes.getEndTime())
                .append("message", exectionTimes.getMessage());
        userCollection.updateOne(query, new Document("$push", new Document("taskExecutions", doc)));
        return "Sucessfully executed command";
    }
    public ExectionTimes createExecutionTime(){
        ExectionTimes exectionTimes = new ExectionTimes();
        exectionTimes.setStartTime(LocalDateTime.now().toString());
        exectionTimes.setEndTime(LocalDateTime.now().plusSeconds(10).toString());
        exectionTimes.setMessage("Sucessfully executed command");
        return exectionTimes;
    }


}
