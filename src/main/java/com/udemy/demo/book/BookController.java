package com.udemy.demo.book;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.udemy.demo.borrow.Borrow;
import com.udemy.demo.borrow.BorrowRepository;
import com.udemy.demo.user.UserInfo;
import com.udemy.demo.user.UserRepository;
import com.udemy.demo.configuration.MyUserDetailsService;

@RestController
public class BookController {

	@Autowired
	BookRepository bookRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	BorrowRepository borrowRepository;

	@GetMapping(value = "/books")
	public ResponseEntity listBooks(@RequestParam(required = false) BookStatus status, Principal principal) {

		Integer userConnectedId = BookController.getUserConnectedId(principal);
		List<Book> books;

		if (status != null && status == BookStatus.FREE) {
			books = bookRepository.findByBookStatusAndUserIdNotAndDeletedFalse(status, userConnectedId);

		} else {
			books = bookRepository.findByUserIdAndDeletedFalse(userConnectedId);

		}

		return new ResponseEntity(books, HttpStatus.OK);

	}

	  public static Integer getUserConnectedId(Principal principal) {
	        if (!(principal instanceof UsernamePasswordAuthenticationToken)) {
	            throw new RuntimeException(("User not found"));
	        }
	        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) principal;
	        Integer userId = ((MyUserDetailsService.UserPrincipal) token.getPrincipal()).getUser().getId();

	        return userId;
	    }

	@PostMapping(value = "/books")
	public ResponseEntity addBook(@RequestBody @Valid Book book, Principal principal) {

		Integer userConnectedId = BookController.getUserConnectedId(principal);
		Optional<UserInfo> user = userRepository.findById(userConnectedId);
		Optional<Category> category = categoryRepository.findById(book.getCategoryId());
		if (category.isPresent()) {
			book.setCategory(category.get());
		} else {
			return new ResponseEntity("You must provide a valid category", HttpStatus.BAD_REQUEST);
		}

		if (user.isPresent()) {
			book.setUser(user.get());
		} else {
			return new ResponseEntity("You must provide a valid user", HttpStatus.BAD_REQUEST);
		}

		book.setDeleted(false);

		book.setBookStatus(BookStatus.FREE);

		bookRepository.save(book);

		return new ResponseEntity(book, HttpStatus.CREATED);

	}

	@DeleteMapping(value = "/books/{bookId}")
	public ResponseEntity deleteBook(@PathVariable("bookId") String bookId) {

		Optional<Book> bookToDelete = bookRepository.findById(Integer.valueOf(bookId));

		if (!bookToDelete.isPresent()) {
			return new ResponseEntity("Book not found", HttpStatus.BAD_REQUEST);
		}
		Book book = bookToDelete.get();
		List<Borrow> borrows = borrowRepository.findByBookId(book.getId());

		for (Borrow borrow : borrows) {
			if (borrow.getCloseDate() == null) {
				UserInfo borrower = borrow.getBorrower();
				return new ResponseEntity(borrower, HttpStatus.CONFLICT);
			}
		}
		book.setDeleted(true);

		bookRepository.save(book);
		return new ResponseEntity(HttpStatus.NO_CONTENT);

	}

	@PutMapping(value = "/books/{bookId}")
	public ResponseEntity updateBook(@PathVariable("bookId") String bookId, @RequestBody @Valid Book book) {

		Optional<Book> bookToUpdate = bookRepository.findById(Integer.valueOf(bookId));

		if (!bookToUpdate.isPresent()) {
			return new ResponseEntity("Book not existing", HttpStatus.BAD_REQUEST);
		}

		Book bookToSave = bookToUpdate.get();

		Optional<Category> newCategory = categoryRepository.findById(book.getCategoryId());

		if (!newCategory.isPresent()) {
			return new ResponseEntity("Category not existing", HttpStatus.BAD_REQUEST);
		}

		bookToSave.setCategory(newCategory.get());

		bookToSave.setTitle(book.getTitle());

		bookRepository.save(bookToSave);

		return new ResponseEntity(bookToSave, HttpStatus.OK);

	}

	@GetMapping(value = "/books/{bookId}")
	public ResponseEntity loadBook(@PathVariable("bookId") String bookId) {
		Optional<Book> book = bookRepository.findById(Integer.valueOf(bookId));

		if (!book.isPresent()) {
			return new ResponseEntity("Book not found", HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity(book.get(), HttpStatus.OK);
	}

	@GetMapping("/categories")
	public ResponseEntity listCategories() {
		List<Category> categories = (List<Category>) categoryRepository.findAll();
	
		return new ResponseEntity(categories, HttpStatus.OK);

	}

}
