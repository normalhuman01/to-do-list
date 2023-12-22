package com.todo.app.conroller;

import com.todo.app.entity.Todo;
import com.todo.app.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.todo.app.util.RestConstants.TODO_URL;

@RestController
@RequiredArgsConstructor
@RequestMapping(TODO_URL)
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    public List<Todo> getUserTodos(@PathVariable String username) {
        return todoService.getTodos(username);
    }

    @GetMapping("/{id}")
    public Todo getTodo(@PathVariable String username, @PathVariable Integer id) {
        return todoService.getTodoByIdAndUser(id, username);
    }

    @PostMapping
    public ResponseEntity<Void> createTodo(@PathVariable String username, @RequestBody Todo todo) {
        todo.setId(null);
        todo.setUser(username);
        todo.setDone(false);
        Todo createdTodo = todoService.saveTodo(todo);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTodo.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public Todo updateTodo(@PathVariable String username, @PathVariable Integer id, @RequestBody Todo todo) {
        todoService.getTodoByIdAndUser(id, username); // check exists
        todo.setId(id);
        return todoService.saveTodo(todo);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTodo(@PathVariable String username, @PathVariable Integer id) {
        todoService.getTodoByIdAndUser(id, username); // check exists
        todoService.deleteTodo(id);
    }
}
