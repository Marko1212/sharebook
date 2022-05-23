package com.udemy.demo.borrow;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.udemy.demo.book.Book;
import com.udemy.demo.book.BookController;
import com.udemy.demo.book.BookRepository;
import com.udemy.demo.book.BookStatus;
import com.udemy.demo.user.UserInfo;
import com.udemy.demo.user.UserRepository;

@RestController
public class BorrowController {

	@Autowired
	BorrowRepository borrowRepository;

	@Autowired
	BookRepository bookRepository;

	@Autowired
	UserRepository userRepository;

	@GetMapping(value = "/borrows")
	public ResponseEntity getMyBorrows(Principal principal) {
		Integer userConnectedId = BookController.getUserConnectedId(principal);
		List<Borrow> borrows = borrowRepository.findByBorrowerId(userConnectedId);

		return new ResponseEntity(borrows, HttpStatus.OK);
	}

	@PostMapping("/borrows/{bookId}")
	public ResponseEntity createBorrow(@PathVariable("bookId") String bookId, Principal principal) {
		Integer userConnectedId = BookController.getUserConnectedId(principal);
		Optional<UserInfo> borrower = userRepository.findById(Integer.valueOf(userConnectedId));
		Optional<Book> book = bookRepository.findById(Integer.valueOf(bookId));

		if (borrower.isPresent() && book.isPresent() && book.get().getBookStatus().equals(BookStatus.FREE)) {
			Borrow borrow = new Borrow();
			borrow.setBook(book.get());
			borrow.setBorrower(borrower.get());
			borrow.setLender(book.get().getUser());
			borrow.setAskDate(LocalDate.now());
			borrowRepository.save(borrow);

			book.get().setBookStatus(BookStatus.BORROWED);
			bookRepository.save(book.get());

			return new ResponseEntity(HttpStatus.CREATED);

		}

		return new ResponseEntity(HttpStatus.BAD_REQUEST);
	}

	@DeleteMapping("/borrows/{borrowId}")
	public ResponseEntity deleteBorrow(@PathVariable("borrowId") String borrowId) {
		Optional<Borrow> borrow = borrowRepository.findById(Integer.valueOf(borrowId));
		if (!borrow.isPresent()) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
		Borrow borrow1 = borrow.get();
		borrow1.setCloseDate(LocalDate.now());
		borrowRepository.save(borrow1);
		
		Book book = borrow1.getBook();
		
		book.setBookStatus(BookStatus.FREE);
		
		bookRepository.save(book);
		
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}

}
