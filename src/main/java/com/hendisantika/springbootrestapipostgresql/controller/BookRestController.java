package com.hendisantika.springbootrestapipostgresql.controller;

import com.hendisantika.springbootrestapipostgresql.entity.Book;
import com.hendisantika.springbootrestapipostgresql.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequestMapping("/api/books")
public class BookRestController {

    private static final Logger logger = LogManager.getLogger(BookRestController.class);

    @Autowired
    private BookRepository repository;
    public BookRestController(BookRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        logger.info("User requested to add book: {}", book);
        Book savedBook = repository.save(book);
        logger.info("Book added: {}", savedBook);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Collection<Book>> getAllBooks() {
        logger.info("User requested to fetch all books");
        Collection<Book> books = repository.findAll();
        logger.info("Fetched {} books", books.size());
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookWithId(@PathVariable Long id) {
        logger.info("User requested to fetch book with id: {}", id);
        Optional<Book> book = repository.findById(id);
        if (book.isPresent()) {
            logger.info("Book found: {}", book.get());
            return new ResponseEntity<>(book.get(), HttpStatus.OK);
        } else {
            logger.warn("Book not found with id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(params = {"name"})
    public ResponseEntity<Collection<Book>> findBookWithName(@RequestParam String name) {
        logger.info("User requested to search books with name: {}", name);
        Collection<Book> books = repository.findByName(name);
        logger.info("Found {} books with name: {}", books.size(), name);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBookFromDB(@PathVariable long id, @RequestBody Book book) {
        logger.info("User requested to update book with id: {}", id);
        Optional<Book> currentBookOpt = repository.findById(id);
        if (currentBookOpt.isPresent()) {
            Book currentBook = currentBookOpt.get();
            currentBook.setName(book.getName());
            currentBook.setDescription(book.getDescription());
            currentBook.setTags(book.getTags());
            Book updatedBook = repository.save(currentBook);
            logger.info("Book updated: {}", updatedBook);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        } else {
            logger.warn("Book not found with id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookWithId(@PathVariable Long id) {
        logger.info("User requested to delete book with id: {}", id);
        repository.deleteById(id);
        logger.info("Book deleted with id: {}", id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteAllBooks() {
        logger.info("User requested to delete all books");
        repository.deleteAll();
        logger.info("All books deleted");
        return ResponseEntity.noContent().build();
    }
}