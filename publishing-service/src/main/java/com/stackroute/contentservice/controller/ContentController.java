package com.stackroute.contentservice.controller;


import com.stackroute.contentservice.exceptions.ContentAlreadyExistsExceptions;
import com.stackroute.contentservice.exceptions.ContentDoesNotExistException;
import com.stackroute.contentservice.exceptions.InternalServerErrorException;
import com.stackroute.contentservice.exceptions.NullValueFieldException;
import com.stackroute.contentservice.model.Content;
import com.stackroute.contentservice.service.ContentService;
import com.stackroute.contentservice.service.RabbitMQSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@CrossOrigin(origins = "http://localhost", maxAge = 3600)
@RestController
@RequestMapping("/api/v1")
public class ContentController {

    private ContentService contentService;
    private RabbitMQSender rabbitMQSender;
    private ResponseEntity responseEntity;

    @Autowired
    public ContentController(ContentService contentService,RabbitMQSender rabbitMQSender) {
        this.contentService = contentService;
        this.rabbitMQSender = rabbitMQSender;
    }



    @PostMapping(value = "/save")
    public ResponseEntity<Content> registerUser(@RequestBody Content content) throws ContentAlreadyExistsExceptions, InternalServerErrorException, NullValueFieldException {
        content.setId(contentService.getNextSequence("customSequences"));
        return new ResponseEntity<Content> (contentService.saveContent(content), HttpStatus.CREATED);
    }
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") int id) throws InternalServerErrorException, ContentDoesNotExistException {
        contentService.deleteContent(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }

    @PutMapping(value = "/update")
    public ResponseEntity<?> update(@RequestBody Content content) throws InternalServerErrorException, ContentDoesNotExistException {
        contentService.updateContent(content);
        return new ResponseEntity<Content>(content, HttpStatus.OK);
    }
    @GetMapping(value = "/content/{title}")
    public ResponseEntity<?> getContent(@PathVariable("title") String title) throws ContentDoesNotExistException,InternalServerErrorException
    {


        responseEntity=new ResponseEntity<>(contentService.findByTitle(title),HttpStatus.OK);
        return responseEntity;
    }
    @GetMapping(value = "/content/editor/{id}")
    public ResponseEntity<?> getByEditorId(@PathVariable("id") int id) throws InternalServerErrorException
    {


        responseEntity=new ResponseEntity<>(contentService.findByEditorId(id),HttpStatus.OK);
        return responseEntity;
    }
}
