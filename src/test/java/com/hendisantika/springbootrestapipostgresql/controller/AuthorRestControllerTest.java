package com.hendisantika.springbootrestapipostgresql.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.hendisantika.springbootrestapipostgresql.entity.Author;
import com.hendisantika.springbootrestapipostgresql.repository.AuthorRepository;

public class AuthorRestControllerTest {

    private AuthorRestController authorController;
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        authorRepository = mock(AuthorRepository.class);
        authorController = new AuthorRestController(authorRepository);
    }

    @Test
    void testAddAuthor() {
        Author author = new Author("John Doe", "123456", new ArrayList<>());
        when(authorRepository.save(author)).thenReturn(author);

        ResponseEntity<?> responseEntity = authorController.addAuthor(author);

        assertNotNull(responseEntity.getBody());
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testGetAllAuthors() {
        List<Author> authors = new ArrayList<>();
        authors.add(new Author("John Doe", "123456", new ArrayList<>()));
        authors.add(new Author("Jane Smith", "789012", new ArrayList<>()));

        when(authorRepository.findAll()).thenReturn(authors);

        ResponseEntity<Collection<Author>> responseEntity = authorController.getAllAuthors();

        assertNotNull(responseEntity.getBody());
        assertEquals(2, responseEntity.getBody().size());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testGetAuthorWithId() {
        Long authorId = 1L;
        Author author = new Author("John Doe", "123456", new ArrayList<>());
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));

        ResponseEntity<Author> responseEntity = authorController.getAuthorWithId(authorId);

        assertNotNull(responseEntity.getBody());
        assertEquals("John Doe", responseEntity.getBody().getName());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testFindAuthorWithName() {
        String authorName = "John Doe";
        List<Author> authors = new ArrayList<>();
        authors.add(new Author(authorName, "123456", new ArrayList<>()));

        when(authorRepository.findByName(authorName)).thenReturn(authors);

        ResponseEntity<Collection<Author>> responseEntity = authorController.findAuthorWithName(authorName);

        assertNotNull(responseEntity.getBody());
        assertEquals(1, responseEntity.getBody().size());
        assertEquals(authorName, responseEntity.getBody().iterator().next().getName());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testUpdateAuthorFromDB() {
        Long authorId = 1L;
        Author existingAuthor = new Author("John Doe", "123456", new ArrayList<>());
        Author updatedAuthor = new Author("Jane Smith", "789012", new ArrayList<>());

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(existingAuthor));
        when(authorRepository.save(existingAuthor)).thenReturn(updatedAuthor);

        ResponseEntity<Author> responseEntity = authorController.updateAuthorFromDB(authorId, updatedAuthor);

        assertNotNull(responseEntity.getBody());
        assertEquals("Jane Smith", responseEntity.getBody().getName());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testDeleteAuthorWithId() {
        Long authorId = 1L;
        ResponseEntity<Void> responseEntity = authorController.deleteAuthorWithId(authorId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(authorRepository).deleteById(authorId);
    }

    @Test
    void testDeleteAllAuthors() {
        ResponseEntity<Void> responseEntity = authorController.deleteAllAuthors();

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(authorRepository).deleteAll();
    }
}