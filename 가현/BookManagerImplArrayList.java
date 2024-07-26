package day0724.ws;

import static java.util.Arrays.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/***
 * 오늘의 실습 1 : 객체 배열로 데이터 저장
 * 오늘의 실습 2 : ArrayList<Book> 또는 LinkedList<Book> 저장
 * 오늘의 실습 3 : Map을 활용하여 데이터 저장
 */
public class BookManagerImplArrayList implements  IBookManager{
    private List<Book> books=new ArrayList<>(); //실습 2
//    private Map<String, Book> books; //실습 3

    private BookManagerImplArrayList(){
        //생성자를 private으로 해서 new로 생성 불가능하게
    }
    private static BookManagerImplArrayList instance=new BookManagerImplArrayList();
    public static BookManagerImplArrayList getInstance(){
        return instance;
    } //싱글톤 패턴 완료 -> 객체 하나만 생성, 나머지는 이 객체를 받아감


    @Override
    public void add(Book book) {
        books.add(book);
    }

    @Override
    public void remove(String isbn) {
        //고유번호로 해당 도서를 도서리스트에서 삭제한다.
        for(int i=0;i<books.size();i++){
            if(books.get(i).getIsbn().equals(isbn)){
                books.remove(i); //List의 remove()사용해 삭제
            }
        }
    }

    @Override
    public Book[] getList() {
        //등록된 도서리스트를 반환한다.
        //도서 전체 목록
        Book[] booksArray=books.toArray(new Book[0]);
        return booksArray;
    }

    @Override
    public Book searchByIsbn(String isbn) {
        //고유번호로 해당 도서를 조회한다.
        Book result=new Book();
        for(int i=0;i< books.size();i++){
            if(books.get(i).getIsbn().equals(isbn)){
                result=books.get(i);
            }
        }
        return result;
    }

    @Override
    public Book[] searchByTitle(String title) {
        List<Book> ans=books.stream()
                .filter(s->s.getTitle().contains(title)).toList();
        return ans.toArray(new Book[0]);
    }

    @Override
    public Magazine[] getMagazines() {
        //잡지리스트를 반환한다.
        Magazine[] magazines=new Magazine[100];
        int idx=0;
        for(int i=0;i< books.size();i++){
            if(books.get(i) instanceof Magazine){
                magazines[idx++]= (Magazine) books.get(i);
            }
        }

        Magazine[] result=new Magazine[idx];
        for(int i=0;i< idx;i++){
            result[i]= magazines[i];
        }
        return result;
    }

    @Override
    public Book[] getBooks() {
        Book[] bookTemp=new Book[100];
        int idx=0;
        for(int i=0;i< books.size();i++){
            if(!(books.get(i) instanceof Magazine)){
                bookTemp[idx++]= books.get(i);
            }
        }

        Book[] result=new Book[idx];
        for(int i=0;i< idx;i++){
            result[i]= bookTemp[i];
        }
        return result;

        List<Book> ans=books.stream()
                .filter(s->(!(s instanceof Magazine)))
                .filter(s->s)
    }

    @Override
    public int getTotalPrice() {
        //도서리스트의 가격의 총합을 반환한다.
        int sum=0;
        for(int i=0;i<books.size();i++){
            sum+=books.get(i).getPrice();
        }
        return sum;
    }

    @Override
    public double getPriceAvg() {
        //도서가격의 평균을 반환한다.
        int sum=getTotalPrice();

        return (sum/ books.size());
    }

    @Override
    public void sell(String isbn, int quantity) throws QuantityException, ISBNNotFoundException {
        Book book = searchByIsbn(isbn);
        if(book.getAuthor().equals(null)) throw new ISBNNotFoundException(isbn);
        else{
            if (book.getQuantity() >= quantity) {
                book.setQuantity(book.getQuantity() - quantity);
            } else {
                throw new QuantityException();
            }
        }
    }

    @Override
    public void buy(String isbn, int quantity) throws ISBNNotFoundException {
        try{
            Book book=searchByIsbn(isbn);
            book.setQuantity(book.getQuantity()+quantity);
        } catch (Exception e){
            throw new ISBNNotFoundException(isbn);
        }
    }
}
