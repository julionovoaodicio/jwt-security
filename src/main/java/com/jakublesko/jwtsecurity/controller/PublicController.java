package com.jakublesko.jwtsecurity.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jakublesko.jwtsecurity.documentdb.sample.dao.TodoDao;
import com.jakublesko.jwtsecurity.documentdb.sample.dao.TodoDaoFactory;
import com.jakublesko.jwtsecurity.documentdb.sample.data.TodoItem;

import lombok.NonNull;

@RestController
@RequestMapping("/api/public")
public class PublicController {


    private static final String anyTodoItemName = "myName";
    private static final String anyTodoItemCategory = "myCategory";
    private static final boolean anyTodoItemComplete = true;
    
    @GetMapping
    public String test() {
    	
    	 String testTodoItemId = createTodoItem(
                 anyTodoItemName, anyTodoItemCategory, anyTodoItemComplete)
                 .getId();
    	 
        if(testTodoItemId != null) {
        	deleteTodoItem(testTodoItemId);
            return "Testing item, id created:  " +testTodoItemId +" and deleted";	
        }

        return "Hello from public API controller, response empty";
    }
    
    @PostMapping(path="/item")
    public String createItem(@RequestBody TodoItem todoItem) {
    	
    	 String testTodoItemId = createTodoItem(todoItem).getId();
    	 
        if(testTodoItemId != null) {
            return "Item created with id:  " +testTodoItemId;	
        }

        return "cannot create an item";
    }
   
    private final TodoDao todoDao = TodoDaoFactory.getDao();

    public TodoItem createTodoItem(@NonNull String name,
            @NonNull String category, boolean isComplete) {
        TodoItem todoItem = TodoItem.builder().name(name).category(category)
                .complete(isComplete).build();
        return todoDao.createTodoItem(todoItem);
    }
    
    public TodoItem createTodoItem(@NonNull TodoItem todoItemParam) {
        TodoItem todoItem = TodoItem.builder().name(todoItemParam.getName()).category(todoItemParam.getCategory())
                .complete(anyTodoItemComplete).build();
        return todoDao.createTodoItem(todoItem);
    }

    public boolean deleteTodoItem(@NonNull String id) {
        return todoDao.deleteTodoItem(id);
    }

}
