package com.pranati.kaiburr.service;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.pranati.kaiburr.model.Task;
import org.bson.Document;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final MongoCollection<Document> userCollection;
    public UserService(MongoDatabase database) {
        this.userCollection = database.getCollection("Tasks");
    }
    public void addTask(Task task){
        Document doc = new Document("id" , task.getId())
                .append("name", task.getName())
                .append("owner", task.getOwner())
                .append("command", task.getCommand());
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
    public List<Document> getTasks(String id) {
        List<Document> results = new ArrayList<>();

        if (id != null && !id.isEmpty()) {
            Document query = new Document("id", id);
            Document doc = userCollection.find(query).first();
            if (doc != null) {
                results.add(doc);
            }
        } else {
            FindIterable<Document> docs = userCollection.find();
            for (Document doc : docs) {
                results.add(doc);
            }
        }
        System.out.println(results);

        return results;
    }

}
