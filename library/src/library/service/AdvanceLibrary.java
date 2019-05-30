package library.service;

import library.api.Book;
import library.api.Library;

public class AdvanceLibrary implements Library {

	public AdvanceLibrary() {
		System.out.println("advance library");
	}

	@Override
	public void add(Book book) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Book find(int isbn) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
