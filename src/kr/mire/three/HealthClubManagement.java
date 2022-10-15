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
		//µ¥ÀÌÅ¸º£ÀÌ½º ¿¬°á
		DBConnection dbConn = new DBConnection();
		dbConn.connect();
		

		boolean loopFlag = false;
		
		while(! loopFlag) {
			int menuNo = playMenu();
			switch(menuNo) {
			//È¸¿øµî·Ï
			case INPUT :
				memberInputData();
				break;
			//È¸¿ø½ÃÀÛ½Ã°£, Á¾·á½Ã°£ µî·Ï
			case TIMEINPUT :
				memberTimeInput();
				break;
			//°Ë»ö
			case SEARCH:
				memberSearch();
				break;
			//Ãâ·Â
			case OUTPUT :
				memberOutput();
				break;
			//»èÁ¦
			case DELETE :
				memberDelete();
				break;
			//¼öÁ¤
			case UPDATE :
				memberUpdate();
				break;
			//Á¤·Ä
			case SORT :
				memberSort();
				break;
			//Á¾·á
			case FINISH :
				System.out.println("ÇÁ·Î±×·¥ÀÌ Á¾·áµË´Ï´Ù");
				loopFlag = true;
				break;
			default : System.out.println("¼ýÀÚ 1~9¹ø±îÁö ÀÔ·Â¿ä¸Á");
				break;
			}
		}

	}
	
	public static void memberSort() {
		List<Member> list = new ArrayList<Member>();
		try {
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			
			System.out.print("Á¤·Ä¹æ½Ä || 1 : memberNumber || 2 : name || >>");
			int type = sc.nextInt();
			boolean value = checkInputPattern(String.valueOf(type), 5);
			if(!value) return;
			
			list = dbConn.selectOrderBy(type);
			if(list.size()<= 0) {
		        System.out.println("º¸¿©ÁÙ ¸®½ºÆ®°¡ ¾ø½À´Ï´Ù"+list.size());
		        return;
		     }
			for(Member member : list) {
				System.out.println(member);
			}
			dbConn.close();
		} catch (Exception e) {
			System.out.println("µ¥ÀÌÅ¸º£ÀÌ½º Á¤·Ä ¿À·ù" + e.getMessage());
		}
		return;
	}
	public static void memberUpdate() {
		List<Member> list = new ArrayList<Member>();
		boolean value = false;
		System.out.print("¼öÁ¤ÇÒ È¸¿ø¹øÈ£ ÀÔ·Â :");
		String memberNumber = sc.nextLine();
		value = checkInputPattern(memberNumber, 1);
		if(!value) return;
		
		DBConnection dbConn = new DBConnection();
		dbConn.connect();
		
		list = dbConn.selectSearch(memberNumber);
		if(list.size() <= 0) {
			System.out.println("È¸¿ø Á¤º¸°¡ ¾ø½À´Ï´Ù");
			return;
		}
		for(Member member : list) {
			System.out.println(member);
		}
		Member imsiMember = list.get(0);
		System.out.print("ÀÌ¸§ " +imsiMember.getName()+":");
		String name = sc.nextLine();
		value =  checkInputPattern(name, 2);
		if(!value) return;
		imsiMember.setName(name);
		
		System.out.print("ÀüÈ­¹øÈ£ " +imsiMember.getPhoneNumber()+":");
		String phoneNumber = sc.nextLine();
		value =  checkInputPattern(phoneNumber, 3);
		if(!value) return;
		imsiMember.setPhoneNumber(phoneNumber);
		
		int returnUpdateValue = dbConn.Update(imsiMember);
	     if(returnUpdateValue == -1) {
		        System.out.println("È¸¿ø Á¤º¸°¡ ¼öÁ¤ ¾ÈµÆ½À´Ï´Ù"+returnUpdateValue);
		        return;
		     }
		     System.out.println( "È¸¿ø Á¤º¸°¡ ¼öÁ¤ µÇ¾ú½À´Ï´Ù."+ +returnUpdateValue);
	         
		     dbConn.close();
		}
		

	public static void memberDelete() {
		try {
			System.out.print("»èÁ¦ÇÒ È¸¿ø ¹øÈ£ÀÔ·Â :");
			String memberNumber = sc.nextLine();
			boolean value = checkInputPattern(memberNumber, 1);
			if(!value) return;
			
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			
			int deleteReturnValue = dbConn.delete(memberNumber);
			if(deleteReturnValue == -1) {
				System.out.println("»èÁ¦ ½ÇÆÐ");
			}else if (deleteReturnValue == 0) {
				System.out.println("»èÁ¦ÇÒ È¸¿ø¹øÈ£°¡ Á¸ÀçÇÏÁö ¾Ê½À´Ï´Ù");
			}else {
				System.out.println("»èÁ¦ ¼º°øÀÔ´Ï´Ù");
			}
			dbConn.close();
		} catch(InputMismatchException e) {
			System.out.println("Å¸ÀÔÀÌ ¸ÂÁö ¾Ê½À´Ï´Ù" + e.getMessage());
			return;
		}catch (Exception e) {
			System.out.println("µ¥ÀÌÅ¸º£ÀÌ½º »èÁ¦ ¿À·ù" + e.getMessage());
		}
	}
	public static void memberOutput() {
		List<Member> list = new ArrayList<Member>();
		try {
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			
			list = dbConn.select();
			if(list.size() <= 0) {
				System.out.println("º¸¿©ÁÙ ¸®½ºÆ®°¡ ¾ø½À´Ï´Ù");
				return;
			}
			for(Member member :list) {
				System.out.println(member);
			}
			
	        dbConn.close();
		} catch (Exception e) {
			System.out.println("µ¥ÀÌÅ¸º£ÀÌ½º Ãâ·Â ¿À·ù " + e.getMessage());
		}
		return;
	}
	public static void memberSearch() {
		List<Member> list = new ArrayList<Member>();
		try {
			System.out.print("°Ë»öÇÒ È¸¿ø ¹øÈ£ ÀÔ·Â :");
			String memberNumber = sc.nextLine();
			boolean value = checkInputPattern(memberNumber, 1);
			if(!value) return;
			
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			
			list = dbConn.selectSearch(memberNumber);
			if(list.size()<= 0) {
				System.out.print("°Ë»öÇÑ È¸¿øÁ¤º¸ ¾ø½À´Ï´Ù");
			}
			for(Member member : list) {
				System.out.println(member);
			}
	        System.out.print("µî±Þº° ÀÎ¿ø °Ë»ö (A~F): ");
	        String grade = sc.nextLine();

	        int count = dbConn.getgradeFunc(grade);
	      

	        System.out.println(count); 
			dbConn.close();
		} catch (InputMismatchException e) {
			System.out.println("Å¸ÀÔÀÌ ¸ÂÁö ¾Ê½À´Ï´Ù" + e.getMessage());
		} catch (Exception e) {
			System.out.println("µ¥ÀÌÅ¸º£ÀÌ½º È¸¿ø°Ë»ö ¿¡·¯" + e.getMessage());
		}
	}

	public static void memberTimeInput() {
		List<Member> list = new ArrayList<Member>();
		boolean value = false;
		try {
			System.out.print("È¸¿ø¹øÈ£ ÀÔ·Â : ");
			String memberNumber = sc.nextLine();
			value = checkInputPattern(memberNumber, 1);
			if(!value) return;
			
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			
			list = dbConn.selectSearch(memberNumber);
			if(list.size() <= 0) {
				System.out.println("È¸¿ø Á¤º¸°¡ ¾ø½À´Ï´Ù" + list.size());
				return;
			}
			for(Member member : list) {
				System.out.println(member);
			}
			System.out.print("1.¿îµ¿ ½ÃÀÛ ½Ã°£ÀÔ·Â 2. ¿îµ¿ Á¾·á ½Ã°£ÀÔ·Â ");
			int startFinishTime = sc.nextInt();
			value = checkInputPattern(String.valueOf(startFinishTime), 4);
			if(!value) return;
			
			
			int returnUpdateValue = -1;
			Member imsiMember = list.get(0);
			
			returnUpdateValue = dbConn.timeUpdate(imsiMember, startFinishTime);
			
			if(returnUpdateValue == -1) {
				System.out.println("½Ã°£ ÀÔ·ÂÀÌ ¾ÈµÆ½À´Ï´Ù " + returnUpdateValue);
				return;
			}
			System.out.println("È¸¿ø ½Ã°£ÀÌ µî·ÏµÇ¾ú½À´Ï´Ù " + returnUpdateValue);
			dbConn.close();
		} catch(InputMismatchException e) {
			System.out.println("Å¸ÀÔÀÌ ¸ÂÁö ¾Ê½À´Ï´Ù" + e.getMessage());
		} catch (Exception e) {
			System.out.println("µ¥ÀÌÅ¸º£ÀÌ½º ¿¡·¯" + e.getMessage());
		}
	}


	public static void memberInputData() {
		boolean value = false;
		
		try {
			System.out.print("È¸¿ø¹øÈ£ ÀÔ·Â :");
			String memberNumber = sc.nextLine();
			value = checkInputPattern(memberNumber, 1);
			if(!value) return;
			
			System.out.print("È¸¿øÀÌ¸§ ÀÔ·Â :");
			String name = sc.nextLine();
			value = checkInputPattern(name, 2);
			if(!value) return;
			
			System.out.print("È¸¿øÀüÈ­¹øÈ£ ÀÔ·Â :");
			String phoneNumber = sc.nextLine();
			value = checkInputPattern(phoneNumber, 3);
			if(!value) return;

			Member member = new Member(memberNumber, name, phoneNumber);
			//µ¥ÀÌÅ¸º£ÀÌ½º ¿¬°á
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			int insertReturnValue = dbConn.insert(member);
			if(insertReturnValue == -1) {
				System.out.println("»ðÀÔ½ÇÆÐ ÀÔ´Ï´Ù");
			}else {
				System.out.println("»ðÀÔ ¼º°ø");
			}
			dbConn.close();
		} catch (InputMismatchException e) {
			System.out.println("ÀÔ·ÂÇÑ Å¸ÀÔÀÌ ¸ÂÁö¾Ê½À´Ï´Ù" + e.getMessage());
		}
		
	}

	public static int playMenu() {
		int  number = -1;
		try {
			//¸Þ´º ÀÔ·Â°ª ³Ö±â
			System.out.print("-------------------------------------------------------------------------\n"
					+ "| 1.È¸¿øµî·Ï | 2.È¸¿ø¿îµ¿½Ã°£µî·Ï | 3.°Ë»ö | 4.Ãâ·Â | 5.»èÁ¦ | 6.¼öÁ¤ | 7.Á¤·Ä | 8.Á¾·á |"
					+"\n-------------------------------------------------------------------------\n>>");
			number = sc.nextInt();
			String pattern = "^[1-8]$";
			boolean regex = Pattern.matches(pattern, String.valueOf(number));
		} catch (InputMismatchException e) {
			System.out.println("¼ýÀÚ¸¸ ÀÔ·Â ºÎÅ¹µå¸³´Ï´Ù");
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
			message= "È¸¿ø¹øÈ£ ÀçÀÔ·Â ºÎÅ¹µå¸³´Ï´Ù";
			break;
		case CHECK_MEMBERNAME :
			pattern = "^[°¡-ÆR]{2,4}$";
			message= "È¸¿øÀÌ¸§ ÀçÀÔ·Â ºÎÅ¹µå¸³´Ï´Ù";
			break;
		case CHECK_PHONE :
			pattern = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-](\\d{4})$";
			message = "ÇÚµåÆù ¹øÈ£ ÀçÀÔ·Â ºÎÅ¹µå¸³´Ï´Ù";
			break;
		case CHECK_STARTFINISH :
			pattern = "^[1-2]$";
			message = "¹øÈ£ ÀçÀÔ·Â ºÎÅ¹µå¸³´Ï´Ù";
			break;
		case CHECK_SORT: 
			pattern = "^[1-2]$"; 
			message = "Á¤·ÄÅ¸ÀÔ ÀçÀÔ·Â¿ä¸Á";
			break;

		}
		regex = Pattern.matches(pattern, data);
		if(!regex) {
			System.out.println(message);
		}
		return regex;
	}

}
