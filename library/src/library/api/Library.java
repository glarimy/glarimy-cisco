package library.api;

public interface Library {
	public void add(Book book) throws Exception;
	public Book find(int isbn) throws Exception;
}
