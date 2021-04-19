package fms.webControllers;


import fms.model.FinanceManagementSystem;
import fms.repositories.FmsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/fms")
public class FMScontoller {

    @Autowired
    FmsRepository repository;

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<FinanceManagementSystem> getAllItems() {
        return repository.findAll();
    }


    @GetMapping(path = "/get-by-id/{id}")
    public @ResponseBody FinanceManagementSystem getById(@PathVariable("id") int id) {
        Optional<FinanceManagementSystem> item = repository.findById(id);
        if (item.isPresent()) {
            return item.get();
        }
        throw new Error("Not found");
    }

    @PutMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createItem(@RequestBody FinanceManagementSystem item) {
        item.setId(-1); // Duomenu bazeje negali buti neigiamos 'id', todel ji automatiskai parinks tinkama id
        repository.save(item);

        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/edit/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> editItem(@RequestBody FinanceManagementSystem item, @PathVariable int id) {
        Optional<FinanceManagementSystem> existingItem = repository.findById(id);
        if (!existingItem.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        FinanceManagementSystem newEntity = existingItem.get();
        newEntity.setName(item.getName());
        newEntity.setCurrentVersion(item.getCurrentVersion());
        repository.save(newEntity);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable int id) {
        Optional<FinanceManagementSystem> item = repository.findById(id);
        if (item.isPresent()) {
            repository.delete(item.get());
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
