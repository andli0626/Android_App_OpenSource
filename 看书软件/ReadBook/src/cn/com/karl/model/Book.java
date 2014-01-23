package cn.com.karl.model;

public class Book {
	private int id;
	private String name;
	private String path;
	private int iscollect;
	public Book(int id, String name, String path, int iscollect) {
		super();
		this.id = id;
		this.name = name;
		this.path = path;
		this.iscollect = iscollect;
	}
	
	public Book(int id, String name, String path) {
		super();
		this.id = id;
		this.name = name;
		this.path = path;
	}

	public Book() {
		super();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getIscollect() {
		return iscollect;
	}
	public void setIscollect(int iscollect) {
		this.iscollect = iscollect;
	}
	
}
