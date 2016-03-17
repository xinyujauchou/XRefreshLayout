package demokit.chaowang.demo.model;

/**
 * Book 实体
 * Created by chao.wang on 2016/3/14.
 */
public class Book {

    private String name;

    private String author;

    private String price;

    public Book(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
