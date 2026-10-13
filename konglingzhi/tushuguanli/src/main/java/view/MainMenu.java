package view;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import entity.Book;
import entity.BorrowRecord;
import entity.User;
import service.BookService;
import service.BorrowRecordService;
import service.UserService;
import service.impl.BookServiceImpl;
import service.impl.BorrowRecordServiceImpl;
import service.impl.UserServiceImpl;




public class MainMenu {


    private static final Scanner sc = new Scanner(System.in);
    private static final UserService userService = new UserServiceImpl();
    private static final BookService bookService = new BookServiceImpl();
    private static final BorrowRecordService recordService = new BorrowRecordServiceImpl();
    private static User loginUser;




    public static void main(String[] args) {
        login();
        while (true) {
            System.out.println("\n-------------图书管理系统--------------");
            System.out.println("当前登录:" + loginUser.getRealName());
            System.out.println("1. 查看所有图书");
            System.out.println("2. 按书名搜索");
            System.out.println("3. 借阅图书");
            System.out.println("4. 归还图书");
            System.out.println("5. 我的借阅记录");
            System.out.println("6. 退出系统");
            System.out.print("请选择操作序号:");

            int op = sc.nextInt();
            switch (op) {
                case 1:
                    listAllBooks();
                    break;
                case 2:
                    searchBookByName();
                    break;
                case 3:
                    borrowBook();
                    break;
                case 4:
                    returnBook();
                    break;
                case 5:
                    showMyRecords();
                    break;
                case 6:
                    System.out.println("退出成功");
                    System.exit(0);
                default:
                    System.out.println("输入错误");
            }
        }
    }


    private static void login() {
        System.out.println("-------------用户登录--------------");
        System.out.print("账号:");
        String username = sc.next();
        System.out.print("密码:");
        String password = sc.next();


        loginUser = userService.login(username, password);
        if (loginUser == null) {
            System.out.println("登录失败,账号或密码错误");
            login();
        } else {
            System.out.println("登录成功");
        }
    }


    // 查看所有图书(进入时先提示可看字段)
    public static void listAllBooks() {
        System.out.println("\n【可查看内容:书名、作者、价格、库存】");
        List<Book> list = bookService.findAllBooks();
        if (list.isEmpty()) {
            System.out.println("暂无图书");
            return;
        }
        list.forEach(System.out::println);
    }


    // 按书名搜索(进入时先提示查询方式)
    public static void searchBookByName() {
        System.out.println("\n【可查询方式:按书名关键词模糊匹配】");
        System.out.print("请输入书名关键词:");
        String keyword = sc.next();
        List<Book> list = bookService.findBookLikeName(keyword);
        if (list.isEmpty()) {
            System.out.println("未找到匹配图书");
        } else {
            list.forEach(System.out::println);
        }
    }


    // 借阅图书:输书名关键词→列候选图书(带序号)→选序号借阅,自动用当前登录用户
    public static void borrowBook() {
        System.out.println("\n【借阅方式:输入书名关键词,从匹配列表中选择序号借阅】");
        System.out.print("请输入书名关键词:");
        String keyword = sc.next();
        List<Book> list = bookService.findBookLikeName(keyword);
        if (list.isEmpty()) {
            System.out.println("未找到匹配图书");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            Book b = list.get(i);
            System.out.println((i + 1) + ". " + b.getBookName()
                    + "  作者:" + b.getAuthor()
                    + "  价格:" + b.getPrice()
                    + "  库存:" + b.getStock());
        }
        System.out.print("请选择要借阅的图书序号(0 取消):");
        int idx = sc.nextInt();
        if (idx == 0) {
            return;
        }
        if (idx < 1 || idx > list.size()) {
            System.out.println("序号无效");
            return;
        }
        Book selected = list.get(idx - 1);
        if (selected.getStock() == null || selected.getStock() <= 0) {
            System.out.println("库存不足,借阅失败");
            return;
        }
        boolean ok = bookService.borrowBook(loginUser.getId(), selected.getId());
        System.out.println(ok ? "借阅成功" : "借阅失败");
    }


    // 归还图书:列出当前用户的未还记录(显示书名)→选序号归还
    public static void returnBook() {
        System.out.println("\n【归还方式:从您的未还记录中选择序号归还】");
        List<BorrowRecord> unreturned = new ArrayList<>();
        for (BorrowRecord r : getMyRecords()) {
            if (r.getStatus() != null && r.getStatus() == 1) {
                unreturned.add(r);
            }
        }
        if (unreturned.isEmpty()) {
            System.out.println("您暂无未还图书");
            return;
        }
        Map<Integer, String> bookNameMap = buildBookNameMap();
        for (int i = 0; i < unreturned.size(); i++) {
            BorrowRecord r = unreturned.get(i);
            String bookName = bookNameMap.getOrDefault(r.getBookId(), "未知图书");
            System.out.println((i + 1) + ". " + bookName
                    + "  借阅时间:" + r.getBorrowTime()
                    + "  记录编号:" + r.getId());
        }
        System.out.print("请选择要归还的序号(0 取消):");
        int idx = sc.nextInt();
        if (idx == 0) {
            return;
        }
        if (idx < 1 || idx > unreturned.size()) {
            System.out.println("序号无效");
            return;
        }
        int recordId = unreturned.get(idx - 1).getId();
        System.out.println(bookService.returnBook(recordId) ? "归还成功" : "归还失败");
    }


    // 我的借阅记录:只看自己,把 bookId 转成书名显示
    public static void showMyRecords() {
        System.out.println("\n【可查看内容:书名、借阅时间、归还时间、状态】");
        List<BorrowRecord> mine = getMyRecords();
        if (mine.isEmpty()) {
            System.out.println("您暂无借阅记录");
            return;
        }
        Map<Integer, String> bookNameMap = buildBookNameMap();
        for (BorrowRecord r : mine) {
            String bookName = bookNameMap.getOrDefault(r.getBookId(), "未知图书");
            System.out.println("书名:" + bookName
                    + "  借阅时间:" + r.getBorrowTime()
                    + "  归还时间:" + (r.getReturnTime() == null ? "—" : r.getReturnTime())
                    + "  状态:" + (r.getStatus() == 1 ? "未还" : "已还"));
        }
    }


    // 查询当前登录用户的全部借阅记录(view 层按 userId 过滤)
    private static List<BorrowRecord> getMyRecords() {
        List<BorrowRecord> mine = new ArrayList<>();
        for (BorrowRecord r : recordService.findAll()) {
            if (r.getUserId() != null && r.getUserId().equals(loginUser.getId())) {
                mine.add(r);
            }
        }
        return mine;
    }


    // 构建 bookId -> 书名 的内存映射,用于把借阅记录里的图书编号翻译成书名
    private static Map<Integer, String> buildBookNameMap() {
        Map<Integer, String> map = new HashMap<>();
        for (Book b : bookService.findAllBooks()) {
            if (b.getId() != null) {
                map.put(b.getId(), b.getBookName());
            }
        }
        return map;
    }


}
