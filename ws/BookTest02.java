package ws;

import java.io.IOException;

public class BookTest02 {

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        // 도서 리스트를 유지하고 관리하는 BookManager 객체를 생성한다.
        IBookManager bookManager2 = BookManagerImpl.getInstance();
        // BookManager 객체를 이용해  도서정보를 추가한다.
        bookManager2.add(new Book("21424", "Java Pro", "김하나", "jaen.kr", 15000, "Java 기본 문법",10));
        bookManager2.add(new Book("21425", "Java Pro2", "김하나", "jaen.kr", 25000, "Java 응용",20));
        bookManager2.add(new Book("35355", "분석설계", "소나무", "jaen.kr", 30000, "SW 모델링",30));
        bookManager2.add(new Magazine("45678", "월간 알고리즘", "홍길동", "jaen.kr", 10000, "1월 알고리즘", 2021, 1,40));

        System.out.println("**********************불러온 도서 전체 목록**********************");
        Book[] books = bookManager2.getList();
        if(books == null || books.length == 0){		// 도서리스트가 없는 경우
            System.out.println("등록된 도서가 없습니다.");
        }else{										// 도서리스트가 잇는 경우
            for (Book b : books) {
               if(b != null)System.out.println(b);
            }
        }

        bookManager2.saveData(); // 도서리스트를 파일에 저장한다.
    }
}
