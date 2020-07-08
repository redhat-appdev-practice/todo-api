package com.redhat.openapi.todo.dao;

import java.util.List;
import java.util.UUID;

import com.redhat.openapi.todo.models.Todo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends CrudRepository<Todo, UUID> {

  @Query("From Todo t")
  public List<Todo> listAll();
}