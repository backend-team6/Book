package day0724.ws;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/***
 * 오늘의 실습 1 : 객체 배열로 데이터 저장
 * 오늘의 실습 2 : ArrayList<Book> 또는 LinkedList<Book> 저장
 * 오늘의 실습 3 : Map을 활용하여 데이터 저장
 */
public class BookManagerImplArray implements  IBookManager{
    private static final int SIZE=100; //최대 책 사이즈
    private Book[] books=new Book[SIZE];

    private static int nowIdx=0; //현재 저장된 책의 사이즈
    private static int magazineNum=0;
    private static int bookNum=0;
    ///////실습 1

//    private List<Book> books;
    //////실습 2
//    private Map<String, Book> books; //실습 3

    private BookManagerImplArray(){
        //생성자를 private으로 해서 new로 생성 불가능하게
    }
    private static BookManagerImplArray instance=new BookManagerImplArray();
    public static BookManagerImplArray getInstance(){
        return instance;
    } //싱글톤 패턴 완료 -> 객체 하나만 생성, 나머지는 이 객체를 받아감


    @Override
    public void add(Book book) {
        books[nowIdx]=book;
        nowIdx++;
        if(book instanceof Magazine){
            magazineNum++;
        }
        else{
            bookNum++;
        }
    }

    @Override
    public void remove(String isbn) {
        //고유번호로 해당 도서를 도서리스트에서 삭제한다.
        for(int i=0;i<nowIdx;i++){
            if(books[i].getIsbn().equals(isbn)){
                List<Book> s_list = new ArrayList<>(Arrays.asList(books)); //배열을 List로 변환
                s_list.remove(i); //List의 remove()사용해 삭제
                books=s_list.toArray(new Book[0]); //제거된 List를 다시 배열로 변한
            }
        }
    }

    @Override
    public Book[] getList() {
        //등록된 도서리스트를 반환한다.
        //도서 전체 목록
        Book[] nowBooks=new Book[nowIdx];
        for(int i=0;i<nowIdx;i++){
            nowBooks[i]=books[i];
        }
        return nowBooks; //저장된 book들만 저장. null 값X
    }

    @Override
    public Book searchByIsbn(String isbn) {
        //고유번호로 해당 도서를 조회한다.
        Book result=new Book();
        for(int i=0;i<nowIdx;i++){
            if(books[i].getIsbn().equals(isbn)){
                result=books[i];
            }
        }
        return result;
    }

    @Override
    public Book[] searchByTitle(String title) {
        //도서 제목을 포함하고 있는 도서리스트를 반환한다.
        Book[] temp=new Book[100];
        int num=0; //책의 수
        for(int i=0;i<nowIdx;i++){
            if(books[i].getTitle().contains(title)){
                temp[num]=books[i];
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
        Magazine[] magazines=new Magazine[magazineNum];
        int idx=0;
        for(int i=0;i<nowIdx;i++){
            if(books[i] instanceof Magazine){
                magazines[idx++]= (Magazine) books[i];
            }
        }
        return magazines;
    }

    @Override
    public Book[] getBooks() {
        Book[] books=new Book[bookNum];
        int idx=0;
        for(int i=0;i<nowIdx;i++){
            if(!(this.books[i] instanceof Magazine)){
                books[idx++]= this.books[i];
            }
        }
        return books;
    }

    @Override
    public int getTotalPrice() {
        //도서리스트의 가격의 총합을 반환한다.
        int sum=0;
        for(int i=0;i<nowIdx;i++){
            sum+=books[i].getPrice();
        }
        return sum;
    }

    @Override
    public double getPriceAvg() {
        //도서가격의 평균을 반환한다.
        int sum=getTotalPrice();

        return (sum/nowIdx);
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
