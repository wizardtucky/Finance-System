package fms.webControllers;


import fms.repositories.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import fms.model.Categories.Category;

import java.util.Optional;

@Controller
@RequestMapping(path = "/category")
public class CategoryController {
    @Autowired
    private CategoriesRepository categoriesRepository;

    @GetMapping(path="/allcategories")
    public @ResponseBody Iterable<Category> getAllUsers() {
        return categoriesRepository.findAll();
    }


    @GetMapping("/get-by-id/{id}")
    public @ResponseBody Category getById(@PathVariable("id") int id) {
        Optional<Category> item = categoriesRepository.findById(id);
        if (item.isPresent()) {
            return item.get();
        }
        throw new Error("Not found");
    }

    @PutMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createItem(@RequestBody Category item) {
        item.setId(-1); // Duomenu bazeje negali buti neigiamos 'id', todel ji automatiskai parinks tinkama id
        categoriesRepository.save(item);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Object> deleteItem(@PathVariable int id) {
        Optional<Category> item = categoriesRepository.findById(id);
        if (item.isPresent()) {
            categoriesRepository.delete(item.get());
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}
