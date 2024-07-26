package ws;


import java.io.*;
import java.util.Arrays;
import java.util.Objects;

//오늘의 실습1 : 객체 배열로 데이터 저장
//오늘의 실습2 : ArrayList<Book> 또는 LinkedList<Book> 저장
//오늘의 실습3 : Map 활용하여 데이터 저장
public class BookManagerImpl implements IBookManager {
    private BookManagerImpl(){}
    private static BookManagerImpl instance = new BookManagerImpl();
    public static IBookManager getInstance() {
        return instance;
    }
    ///////////////////////////////////////////////////////

    private static  final int SIZE =100;
    private Book[] books = new Book[SIZE];
    private Magazine[] magazines = new Magazine[SIZE];
    private Book[] genreBooks = new Book[SIZE];

    private int booksindex = 0;
    private int genreBooksindex = 0;
    private int magazinesindex = 0;
    private Book bookTemp;


    @Override
    public void add(Book book) {
        this.books[booksindex] = book;
        booksindex++;
    }

    @Override
    public void remove(String isbn) {
        for (int i = 0; i < booksindex; i++) {
            if (books[i].getIsbn().equals(isbn)) {
                books[i] = books[--booksindex];
                books[booksindex] = null;
                break;
            }
        }
    }

    @Override
    public Book[] getList() {
        return this.books;
    }

    @Override
    public Book searchByIsbn(String isbn) {
        return null;
    }

    @Override
    public Book[] searchByTitle(String title) {
        Book[] searcheds = new Book[booksindex + 1];
        int temp = 0;
        for(Book b : books){
            if( b!=null && b.getTitle().contains(title)){
               searcheds[temp] = b;
               temp++;
            }
        }
        return searcheds;
    }


    public Book[] searchByTitle2(String title){
        return Arrays.stream(books).filter(Objects::nonNull)
                .filter(s->s.getTitle().contains(title)).toArray(Book[]::new);

    }

    @Override
    public Magazine[] getMagazines() {
        for(Book b : books){
            if(b instanceof Magazine){
                magazines[magazinesindex] = (Magazine)b;
                magazinesindex++;
            }
        }
        return this.magazines;
    }

    @Override
    public Book[] getBooks() {
        for(Book b : books){
            if(!(b instanceof Magazine)){
                genreBooks[genreBooksindex] = b;
                genreBooksindex++;
            }
        }
        return this.genreBooks;
    }

    @Override
    public int getTotalPrice() {
        int total = 0;
        for(Book b : books){
            if(b != null){
                total += b.getPrice();
            }
        }
        return total;
    }

    @Override
    public double getPriceAvg() {
        return getTotalPrice() / booksindex;
    }

    @Override
    public void sell(String isbn, int quantity) throws QuantityException, ISBNNotFoundException {
        for(int i = 0; i < booksindex; i++){
            if(books[i].getIsbn().equals(isbn)){
                int temp = books[i].getQuantity();
                if(temp < quantity){
                    throw new QuantityException();
                }
                books[i].setQuantity(temp - quantity);
                return;
            }
        }
        throw new ISBNNotFoundException(isbn);
    }

    @Override
    public void buy(String isbn, int quantity) throws ISBNNotFoundException {
        for(int i = 0; i < booksindex; i++){
            if(books[i].getIsbn().equals(isbn)){
                int temp = books[i].getQuantity();
                books[i].setQuantity(quantity + temp);
                return;
            }
        }
        throw new ISBNNotFoundException(isbn);
    }

    public void saveData(){
       try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("bookDB.data"))){
           oos.writeObject(books);
       } catch (Exception e){
           e.printStackTrace();
       }
    }

    public void loadData(){
        File f = new File("bookDB.data");
        if(!f.exists()) return;
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream("bookDB.data"))){
            books = (Book[]) ois.readObject();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException ioE){
            throw new RuntimeException(ioE);
        }catch (ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }
}
