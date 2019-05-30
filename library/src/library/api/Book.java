package library.api;

public class Book {
	private int isbn;
	private String title;

	public int getIsbn() {
		return isbn;
	}

	public void setIsbn(int isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "Book [isbn=" + isbn + ", title=" + title + "]";
	}

}
