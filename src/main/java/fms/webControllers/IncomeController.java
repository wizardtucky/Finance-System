package fms.webControllers;


import fms.model.Finances.Income;
import fms.repositories.ExpenseRepository;
import fms.repositories.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/income")
public class IncomeController {

    @Autowired
    IncomeRepository repository;

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Income> getAllItems() {
        return repository.findAll();
    }


    @GetMapping(path = "/get-by-id/{id}")
    public @ResponseBody Income getById(@PathVariable("id") int id) {
        Optional<Income> item = repository.findById(id);
        if (item.isPresent()) {
            return item.get();
        }
        throw new Error("Not found");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable int id) {
        Optional<Income> item = repository.findById(id);
        if (item.isPresent()) {
            repository.delete(item.get());
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
