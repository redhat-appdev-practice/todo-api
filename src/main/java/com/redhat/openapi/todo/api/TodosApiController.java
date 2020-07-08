package com.redhat.openapi.todo.api;

import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import com.redhat.openapi.todo.dao.TodoRepository;
import com.redhat.openapi.todo.models.Todo;

@RestController
@RequestMapping("/")
public class TodosApiController implements TodosApi {

    final TodoRepository todoRepo;

    private final NativeWebRequest request;

    @Autowired
    public TodosApiController(NativeWebRequest request, TodoRepository todoRepo) {
        this.request = request;
        this.todoRepo = todoRepo;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }

    @Override
    public ResponseEntity<Todo> createTodo(@ApiParam(value = "A new `Todo` to be created." ,required=true )  @Valid @RequestBody Todo todo) {
        Todo persisted = todoRepo.save(todo);
        return ResponseEntity.ok(persisted);
    }

    @Override
    public ResponseEntity<Void> deleteTodo(@ApiParam(value = "A unique identifier for a `Todo`.",required=true) @PathVariable("todoId") UUID todoId) {
        todoRepo.deleteById(todoId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Todo> getTodo(@ApiParam(value = "A unique identifier for a `Todo`.",required=true) @PathVariable("todoId") UUID todoId) {
        return ResponseEntity.ok(todoRepo.findById(todoId).get());
    }

    @Override
    public ResponseEntity<List<Todo>> gettodos() {
        return ResponseEntity.ok(todoRepo.listAll());
    }

    @Override
    public ResponseEntity<Todo> updateTodo(@ApiParam(value = "A unique identifier for a `Todo`.",required=true) @PathVariable("todoId") UUID todoId,@ApiParam(value = "Updated `Todo` information." ,required=true )  @Valid @RequestBody Todo todo) {
        Todo persisted = todoRepo.findById(todoId).get();
        persisted.setAuthor(request.getHeader("X-Forwarded-Preferred-Username"));
        persisted.setComplete(todo.getComplete());
        persisted.setDescription(todo.getDescription());
        persisted.setTitle(todo.getTitle());
        persisted.setDueDate(todo.getDueDate());
        todoRepo.save(persisted);
        return ResponseEntity.ok(persisted);
    }
}
