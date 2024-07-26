package day0724.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 오늘의 실습 1 : 객체 배열로 데이터 저장
 * 오늘의 실습 2 : ArrayList<Book> 또는 LinkedList<Book> 저장
 * 오늘의 실습 3 : Map을 활용하여 데이터 저장
 */
public class BookManagerImplArrayMap implements  IBookManager{
    private Map<String, Book> books=new HashMap<>(); //실습 3
    //isbn, Book

    private BookManagerImplArrayMap(){
        //생성자를 private으로 해서 new로 생성 불가능하게
    }
    private static BookManagerImplArrayMap instance=new BookManagerImplArrayMap();
    public static BookManagerImplArrayMap getInstance(){
        return instance;
    } //싱글톤 패턴 완료 -> 객체 하나만 생성, 나머지는 이 객체를 받아감


    @Override
    public void add(Book book) {
        books.put(book.getIsbn(),book);
    }

    @Override
    public void remove(String isbn) {
        books.remove(isbn);
    }

    @Override
    public Book[] getList() {
        //등록된 도서리스트를 반환한다.
        //도서 전체 목록
        Book[] booksArray=books.values().toArray(new Book[0]);
        return booksArray;
    }

    @Override
    public Book searchByIsbn(String isbn) {
        //고유번호로 해당 도서를 조회한다.
        return books.get(isbn);
    }

    @Override
    public Book[] searchByTitle(String title) {
        //도서 제목을 포함하고 있는 도서리스트를 반환한다.
        Book[] temp=new Book[100];
        int num=0; //책의 수

        for( String key : books.keySet() ){
            Book value = books.get(key);
            if(value.getTitle().contains(title)){
                temp[num]=books.get(key);
                num++;
            }
        }

        Book[] result=new Book[num];
        for(int i=0;i<num;i++){
            result[i]=temp[i];
        }
        return result;
    }

    @Override
    public Magazine[] getMagazines() {
        //잡지리스트를 반환한다.
        Magazine[] magazines=new Magazine[100];
        int idx=0;

        for( String key : books.keySet() ){
            Book value = books.get(key);
            if(value instanceof Magazine){
                magazines[idx]=(Magazine)books.get(key);
                idx++;
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
        for( String key : books.keySet() ){
            Book value = books.get(key);
            if(!(value instanceof Magazine)){
                bookTemp[idx]=books.get(key);
                idx++;
            }
        }

        Book[] result=new Book[idx];
        for(int i=0;i< idx;i++){
            result[i]= bookTemp[i];
        }
        return result;
    }

    @Override
    public int getTotalPrice() {
        //도서리스트의 가격의 총합을 반환한다.
        int sum=0;
        for( String key : books.keySet() ){
            Book value = books.get(key);
            sum+=value.getPrice();
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
        if(book==null) throw new ISBNNotFoundException(isbn);
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
