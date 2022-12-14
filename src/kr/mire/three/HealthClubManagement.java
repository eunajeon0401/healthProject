package kr.mire.three;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class HealthClubManagement {

	public static Scanner sc = new Scanner(System.in);
	public static final int INPUT = 1, TIMEINPUT = 2, SEARCH = 3, OUTPUT = 4, 
							DELETE = 5, UPDATE = 6, SORT = 7, FINISH = 8;
	public static final int CHECK_MEMBERNUM = 1, CHECK_MEMBERNAME = 2, CHECK_PHONE =3, CHECK_STARTFINISH = 4, CHECK_SORT =5;
	public static void main(String[] args) {
		//데이타베이스 연결
		DBConnection dbConn = new DBConnection();
		dbConn.connect();
		

		boolean loopFlag = false;
		
		while(! loopFlag) {
			int menuNo = playMenu();
			switch(menuNo) {
			//회원등록
			case INPUT :
				memberInputData();
				break;
			//회원시작시간, 종료시간 등록
			case TIMEINPUT :
				memberTimeInput();
				break;
			//검색
			case SEARCH:
				memberSearch();
				break;
			//출력
			case OUTPUT :
				memberOutput();
				break;
			//삭제
			case DELETE :
				memberDelete();
				break;
			//수정
			case UPDATE :
				memberUpdate();
				break;
			//정렬
			case SORT :
				memberSort();
				break;
			//종료
			case FINISH :
				System.out.println("프로그램이 종료됩니다");
				loopFlag = true;
				break;
			default : System.out.println("숫자 1~9번까지 입력요망");
				break;
			}
		}

	}
	
	public static void memberSort() {
		List<Member> list = new ArrayList<Member>();
		try {
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			
			System.out.print("정렬방식 || 1 : memberNumber || 2 : name || >>");
			int type = sc.nextInt();
			boolean value = checkInputPattern(String.valueOf(type), 5);
			if(!value) return;
			
			list = dbConn.selectOrderBy(type);
			if(list.size()<= 0) {
		        System.out.println("보여줄 리스트가 없습니다"+list.size());
		        return;
		     }
			for(Member member : list) {
				System.out.println(member);
			}
			dbConn.close();
		} catch (Exception e) {
			System.out.println("데이타베이스 정렬 오류" + e.getMessage());
		}
		return;
	}
	public static void memberUpdate() {
		List<Member> list = new ArrayList<Member>();
		boolean value = false;
		System.out.print("수정할 회원번호 입력 :");
		String memberNumber = sc.nextLine();
		value = checkInputPattern(memberNumber, 1);
		if(!value) return;
		
		DBConnection dbConn = new DBConnection();
		dbConn.connect();
		
		list = dbConn.selectSearch(memberNumber);
		if(list.size() <= 0) {
			System.out.println("회원 정보가 없습니다");
			return;
		}
		for(Member member : list) {
			System.out.println(member);
		}
		Member imsiMember = list.get(0);
		System.out.print("이름 " +imsiMember.getName()+":");
		String name = sc.nextLine();
		value =  checkInputPattern(name, 2);
		if(!value) return;
		imsiMember.setName(name);
		
		System.out.print("전화번호 " +imsiMember.getPhoneNumber()+":");
		String phoneNumber = sc.nextLine();
		value =  checkInputPattern(phoneNumber, 3);
		if(!value) return;
		imsiMember.setPhoneNumber(phoneNumber);
		
		int returnUpdateValue = dbConn.Update(imsiMember);
	     if(returnUpdateValue == -1) {
		        System.out.println("회원 정보가 수정 안됐습니다"+returnUpdateValue);
		        return;
		     }
		     System.out.println( "회원 정보가 수정 되었습니다."+ +returnUpdateValue);
	         
		     dbConn.close();
		}
		

	public static void memberDelete() {
		try {
			System.out.print("삭제할 회원 번호입력 :");
			String memberNumber = sc.nextLine();
			boolean value = checkInputPattern(memberNumber, 1);
			if(!value) return;
			
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			
			int deleteReturnValue = dbConn.delete(memberNumber);
			if(deleteReturnValue == -1) {
				System.out.println("삭제 실패");
			}else if (deleteReturnValue == 0) {
				System.out.println("삭제할 회원번호가 존재하지 않습니다");
			}else {
				System.out.println("삭제 성공입니다");
			}
			dbConn.close();
		} catch(InputMismatchException e) {
			System.out.println("타입이 맞지 않습니다" + e.getMessage());
			return;
		}catch (Exception e) {
			System.out.println("데이타베이스 삭제 오류" + e.getMessage());
		}
	}
	public static void memberOutput() {
		List<Member> list = new ArrayList<Member>();
		try {
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			
			list = dbConn.select();
			if(list.size() <= 0) {
				System.out.println("보여줄 리스트가 없습니다");
				return;
			}
			for(Member member :list) {
				System.out.println(member);
			}
			
	        dbConn.close();
		} catch (Exception e) {
			System.out.println("데이타베이스 출력 오류 " + e.getMessage());
		}
		return;
	}
	public static void memberSearch() {
		List<Member> list = new ArrayList<Member>();
		try {
			System.out.print("검색할 회원 번호 입력 :");
			String memberNumber = sc.nextLine();
			boolean value = checkInputPattern(memberNumber, 1);
			if(!value) return;
			
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			
			list = dbConn.selectSearch(memberNumber);
			if(list.size()<= 0) {
				System.out.print("검색한 회원정보 없습니다");
			}
			for(Member member : list) {
				System.out.println(member);
			}
	        System.out.print("등급별 인원 검색 (A~F): ");
	        String grade = sc.nextLine();

	        int count = dbConn.getgradeFunc(grade);
	      

	        System.out.println(count); 
			dbConn.close();
		} catch (InputMismatchException e) {
			System.out.println("타입이 맞지 않습니다" + e.getMessage());
		} catch (Exception e) {
			System.out.println("데이타베이스 회원검색 에러" + e.getMessage());
		}
	}

	public static void memberTimeInput() {
		List<Member> list = new ArrayList<Member>();
		boolean value = false;
		try {
			System.out.print("회원번호 입력 : ");
			String memberNumber = sc.nextLine();
			value = checkInputPattern(memberNumber, 1);
			if(!value) return;
			
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			
			list = dbConn.selectSearch(memberNumber);
			if(list.size() <= 0) {
				System.out.println("회원 정보가 없습니다" + list.size());
				return;
			}
			for(Member member : list) {
				System.out.println(member);
			}
			System.out.print("1.운동 시작 시간입력 2. 운동 종료 시간입력 ");
			int startFinishTime = sc.nextInt();
			value = checkInputPattern(String.valueOf(startFinishTime), 4);
			if(!value) return;
			
			
			int returnUpdateValue = -1;
			Member imsiMember = list.get(0);
			
			returnUpdateValue = dbConn.timeUpdate(imsiMember, startFinishTime);
			
			if(returnUpdateValue == -1) {
				System.out.println("시간 입력이 안됐습니다 " + returnUpdateValue);
				return;
			}
			System.out.println("회원 시간이 등록되었습니다 " + returnUpdateValue);
			dbConn.close();
		} catch(InputMismatchException e) {
			System.out.println("타입이 맞지 않습니다" + e.getMessage());
		} catch (Exception e) {
			System.out.println("데이타베이스 에러" + e.getMessage());
		}
	}


	public static void memberInputData() {
		boolean value = false;
		
		try {
			System.out.print("회원번호 입력 :");
			String memberNumber = sc.nextLine();
			value = checkInputPattern(memberNumber, 1);
			if(!value) return;
			
			System.out.print("회원이름 입력 :");
			String name = sc.nextLine();
			value = checkInputPattern(name, 2);
			if(!value) return;
			
			System.out.print("회원전화번호 입력 :");
			String phoneNumber = sc.nextLine();
			value = checkInputPattern(phoneNumber, 3);
			if(!value) return;

			Member member = new Member(memberNumber, name, phoneNumber);
			//데이타베이스 연결
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			int insertReturnValue = dbConn.insert(member);
			if(insertReturnValue == -1) {
				System.out.println("삽입실패 입니다");
			}else {
				System.out.println("삽입 성공");
			}
			dbConn.close();
		} catch (InputMismatchException e) {
			System.out.println("입력한 타입이 맞지않습니다" + e.getMessage());
		}
		
	}

	public static int playMenu() {
		int  number = -1;
		try {
			//메뉴 입력값 넣기
			System.out.print("-------------------------------------------------------------------------\n"
					+ "| 1.회원등록 | 2.회원운동시간등록 | 3.검색 | 4.출력 | 5.삭제 | 6.수정 | 7.정렬 | 8.종료 |"
					+"\n-------------------------------------------------------------------------\n>>");
			number = sc.nextInt();
			String pattern = "^[1-8]$";
			boolean regex = Pattern.matches(pattern, String.valueOf(number));
		} catch (InputMismatchException e) {
			System.out.println("숫자만 입력 부탁드립니다");
		} finally {
			sc.nextLine();
		}
		return number;
	}

	public static boolean checkInputPattern(String data, int patternType) {	
		String pattern = null;
		boolean regex= false;
		String message = null;
		switch(patternType) {
		case CHECK_MEMBERNUM :
			pattern = "^[0-9]+$";
			message= "회원번호 재입력 부탁드립니다";
			break;
		case CHECK_MEMBERNAME :
			pattern = "^[가-힣]{2,4}$";
			message= "회원이름 재입력 부탁드립니다";
			break;
		case CHECK_PHONE :
			pattern = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-](\\d{4})$";
			message = "핸드폰 번호 재입력 부탁드립니다";
			break;
		case CHECK_STARTFINISH :
			pattern = "^[1-2]$";
			message = "번호 재입력 부탁드립니다";
			break;
		case CHECK_SORT: 
			pattern = "^[1-2]$"; 
			message = "정렬타입 재입력요망";
			break;

		}
		regex = Pattern.matches(pattern, data);
		if(!regex) {
			System.out.println(message);
		}
		return regex;
	}

}
