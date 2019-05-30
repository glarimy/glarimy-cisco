package library.ui;

import library.api.Book;
import library.api.Library;

public class LibraryConsole {
	public static void main(String[] args) throws Exception {
		Book book = new Book();
		book.setIsbn(123);
		book.setTitle("JAXRS");

		Library library = (Library) ObjectFactory.getObject("library");

		library.add(book);
		Book result = library.find(123);
		System.out.println(result);
	}
}
