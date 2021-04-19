package fms.webControllers;


import fms.model.Finances.Expenses;
import fms.repositories.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    ExpenseRepository repository;

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Expenses> getAllItems() {
        return repository.findAll();
    }


    @GetMapping(path = "/get-by-id/{id}")
    public @ResponseBody Expenses getById(@PathVariable("id") int id) {
        Optional<Expenses> item = repository.findById(id);
        if (item.isPresent()) {
            return item.get();
        }
        throw new Error("Not found");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable int id) {
        Optional<Expenses> item = repository.findById(id);
        if (item.isPresent()) {
            repository.delete(item.get());
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
