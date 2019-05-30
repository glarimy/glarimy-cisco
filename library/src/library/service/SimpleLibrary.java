package library.service;

import java.util.HashMap;
import java.util.Map;

import library.api.Book;
import library.api.Library;

@Singleton(methodname = "getLibrary")
public class SimpleLibrary implements Library {
	Map<Integer, Book> books = new HashMap<>();
	private static SimpleLibrary instance;

	public synchronized static SimpleLibrary getLibrary() {
		if (instance == null)
			instance = new SimpleLibrary();
		return instance;
	}

	private SimpleLibrary() {
		System.out.println("Simple Library");
	}

	public void add(Book book) throws Exception {
		if (book == null)
			throw new Exception();
		book.setIsbn(123);
		book.setTitle("JAXRS");
		books.put(book.getIsbn(), book);
	}

	@Override
	public Book find(int isbn) throws Exception {
		Book result = books.get(123);
		if (result == null)
			throw new Exception();
		return result;
	}
}
