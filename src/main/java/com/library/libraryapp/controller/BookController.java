package com.library.libraryapp.controller;

import com.library.libraryapp.dto.BookDTO;
import com.library.libraryapp.entity.Book;
import com.library.libraryapp.service.BookService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/books")
@AllArgsConstructor
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private BookService bookService;


    @PostMapping("addBook")
    //URL: http://localhost:8080/api/books/addBook
    public ResponseEntity<BookDTO> addBook(@RequestBody BookDTO bookDTO){
        logger.info("Adding a book...");
        BookDTO savedBookDTO = bookService.addBook(bookDTO);
        return new ResponseEntity<>(savedBookDTO, HttpStatus.CREATED);
    }
    @GetMapping("listAll")
    //URL: http://localhost:8080/api/books/listAll
    public ResponseEntity<List<BookDTO>> getAllBooks(){
        List<BookDTO> allBooks = bookService.getAllBooks();
        return new ResponseEntity<>(allBooks,HttpStatus.OK);
    }

    @GetMapping("{id}")
    //URL: http://localhost:8080/api/books/1
    public ResponseEntity<BookDTO> getBookById(@PathVariable("id") Long bookId){
        BookDTO bookDTO = bookService.getBookById(bookId);
        return new ResponseEntity<>(bookDTO,HttpStatus.OK);
    }

    @PatchMapping("updateBook/{id}")
    //URL: http://localhost:8080/api/books/updateBook/1
    public ResponseEntity<BookDTO> updateBook(@PathVariable("id") Long id,@RequestBody BookDTO bookDTO){
        bookDTO.setId(id);
        BookDTO updateBook = bookService.updateBook(bookDTO);
        return new ResponseEntity<>(updateBook,HttpStatus.OK);
    }

    @DeleteMapping("deleteBook/{id}")
    //URL: http://localhost:8080/api/books/deleteBook/1
    public ResponseEntity<String> deleteBook(@PathVariable("id") Long id){
        bookService.deleteBook(id);
        return new ResponseEntity<>("Book Succesfuly deleted",HttpStatus.OK);
    }

    @GetMapping("search-title")
    //URL: http://localhost:8080/api/books/search-title?title=LOTR
    public ResponseEntity<List<BookDTO>> searchBooksByTitle(@RequestParam String title){
        List<BookDTO> books = bookService.findBooksByTitle(title);
        return new ResponseEntity<>(books,HttpStatus.OK);
    }

    @GetMapping("search-title-author")
    //URL: http://localhost:8080/api/books/search-title-author?title=LOTR?author=tolk
    public ResponseEntity<List<BookDTO>> searchBooksByTitleAndAuthor(@RequestParam String title,@RequestParam String author){
        List<BookDTO> books = bookService.findBooksByTitleAndAuthor(title,author);
        return new ResponseEntity<>(books,HttpStatus.OK);
    }

    @GetMapping("search")
    //URL: http://localhost:8080/api/books/search?title=LOTR?author=tolk
    public ResponseEntity<List<BookDTO>> searchBooks(@RequestParam(required = false) String title,
                                                     @RequestParam(required = false) String author,
                                                     @RequestParam(required = false) String isbn,
                                                     @RequestParam(required = false) String barcodeNumber){
        List<BookDTO> books = bookService.findBooksByCriteria(title,author,isbn,barcodeNumber);
        return new ResponseEntity<>(books,HttpStatus.OK);
    }
}
