package com.udemy.demo.borrow;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowRepository extends CrudRepository<Borrow, Integer>{
	
	List<Borrow> findByBorrowerId(Integer borrowerId);
	List<Borrow> findByBookId(Integer bookId);
}
