package advanceddatabases.assignment.fableshopbackend.repository;

import advanceddatabases.assignment.fableshopbackend.model.Interaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InteractionRepository extends MongoRepository<Interaction, String> {
    List<Interaction> findByUserId(String userId);
}