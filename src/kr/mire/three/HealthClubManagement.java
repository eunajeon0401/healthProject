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
		//����Ÿ���̽� ����
		DBConnection dbConn = new DBConnection();
		dbConn.connect();
		

		boolean loopFlag = false;
		
		while(! loopFlag) {
			int menuNo = playMenu();
			switch(menuNo) {
			//ȸ�����
			case INPUT :
				memberInputData();
				break;
			//ȸ�����۽ð�, ����ð� ���
			case TIMEINPUT :
				memberTimeInput();
				break;
			//�˻�
			case SEARCH:
				memberSearch();
				break;
			//���
			case OUTPUT :
				memberOutput();
				break;
			//����
			case DELETE :
				memberDelete();
				break;
			//����
			case UPDATE :
				memberUpdate();
				break;
			//����
			case SORT :
				memberSort();
				break;
			//����
			case FINISH :
				System.out.println("���α׷��� ����˴ϴ�");
				loopFlag = true;
				break;
			default : System.out.println("���� 1~9������ �Է¿��");
				break;
			}
		}

	}
	
	public static void memberSort() {
		List<Member> list = new ArrayList<Member>();
		try {
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			
			System.out.print("���Ĺ�� || 1 : memberNumber || 2 : name || >>");
			int type = sc.nextInt();
			boolean value = checkInputPattern(String.valueOf(type), 5);
			if(!value) return;
			
			list = dbConn.selectOrderBy(type);
			if(list.size()<= 0) {
		        System.out.println("������ ����Ʈ�� �����ϴ�"+list.size());
		        return;
		     }
			for(Member member : list) {
				System.out.println(member);
			}
			dbConn.close();
		} catch (Exception e) {
			System.out.println("����Ÿ���̽� ���� ����" + e.getMessage());
		}
		return;
	}
	public static void memberUpdate() {
		List<Member> list = new ArrayList<Member>();
		boolean value = false;
		System.out.print("������ ȸ����ȣ �Է� :");
		String memberNumber = sc.nextLine();
		value = checkInputPattern(memberNumber, 1);
		if(!value) return;
		
		DBConnection dbConn = new DBConnection();
		dbConn.connect();
		
		list = dbConn.selectSearch(memberNumber);
		if(list.size() <= 0) {
			System.out.println("ȸ�� ������ �����ϴ�");
			return;
		}
		for(Member member : list) {
			System.out.println(member);
		}
		Member imsiMember = list.get(0);
		System.out.print("�̸� " +imsiMember.getName()+":");
		String name = sc.nextLine();
		value =  checkInputPattern(name, 2);
		if(!value) return;
		imsiMember.setName(name);
		
		System.out.print("��ȭ��ȣ " +imsiMember.getPhoneNumber()+":");
		String phoneNumber = sc.nextLine();
		value =  checkInputPattern(phoneNumber, 3);
		if(!value) return;
		imsiMember.setPhoneNumber(phoneNumber);
		
		int returnUpdateValue = dbConn.Update(imsiMember);
	     if(returnUpdateValue == -1) {
		        System.out.println("ȸ�� ������ ���� �ȵƽ��ϴ�"+returnUpdateValue);
		        return;
		     }
		     System.out.println( "ȸ�� ������ ���� �Ǿ����ϴ�."+ +returnUpdateValue);
	         
		     dbConn.close();
		}
		

	public static void memberDelete() {
		try {
			System.out.print("������ ȸ�� ��ȣ�Է� :");
			String memberNumber = sc.nextLine();
			boolean value = checkInputPattern(memberNumber, 1);
			if(!value) return;
			
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			
			int deleteReturnValue = dbConn.delete(memberNumber);
			if(deleteReturnValue == -1) {
				System.out.println("���� ����");
			}else if (deleteReturnValue == 0) {
				System.out.println("������ ȸ����ȣ�� �������� �ʽ��ϴ�");
			}else {
				System.out.println("���� �����Դϴ�");
			}
			dbConn.close();
		} catch(InputMismatchException e) {
			System.out.println("Ÿ���� ���� �ʽ��ϴ�" + e.getMessage());
			return;
		}catch (Exception e) {
			System.out.println("����Ÿ���̽� ���� ����" + e.getMessage());
		}
	}
	public static void memberOutput() {
		List<Member> list = new ArrayList<Member>();
		try {
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			
			list = dbConn.select();
			if(list.size() <= 0) {
				System.out.println("������ ����Ʈ�� �����ϴ�");
				return;
			}
			for(Member member :list) {
				System.out.println(member);
			}
			
	        dbConn.close();
		} catch (Exception e) {
			System.out.println("����Ÿ���̽� ��� ���� " + e.getMessage());
		}
		return;
	}
	public static void memberSearch() {
		List<Member> list = new ArrayList<Member>();
		try {
			System.out.print("�˻��� ȸ�� ��ȣ �Է� :");
			String memberNumber = sc.nextLine();
			boolean value = checkInputPattern(memberNumber, 1);
			if(!value) return;
			
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			
			list = dbConn.selectSearch(memberNumber);
			if(list.size()<= 0) {
				System.out.print("�˻��� ȸ������ �����ϴ�");
			}
			for(Member member : list) {
				System.out.println(member);
			}
	        System.out.print("��޺� �ο� �˻� (A~F): ");
	        String grade = sc.nextLine();

	        int count = dbConn.getgradeFunc(grade);
	      

	        System.out.println(count); 
			dbConn.close();
		} catch (InputMismatchException e) {
			System.out.println("Ÿ���� ���� �ʽ��ϴ�" + e.getMessage());
		} catch (Exception e) {
			System.out.println("����Ÿ���̽� ȸ���˻� ����" + e.getMessage());
		}
	}

	public static void memberTimeInput() {
		List<Member> list = new ArrayList<Member>();
		boolean value = false;
		try {
			System.out.print("ȸ����ȣ �Է� : ");
			String memberNumber = sc.nextLine();
			value = checkInputPattern(memberNumber, 1);
			if(!value) return;
			
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			
			list = dbConn.selectSearch(memberNumber);
			if(list.size() <= 0) {
				System.out.println("ȸ�� ������ �����ϴ�" + list.size());
				return;
			}
			for(Member member : list) {
				System.out.println(member);
			}
			System.out.print("1.� ���� �ð��Է� 2. � ���� �ð��Է� ");
			int startFinishTime = sc.nextInt();
			value = checkInputPattern(String.valueOf(startFinishTime), 4);
			if(!value) return;
			
			
			int returnUpdateValue = -1;
			Member imsiMember = list.get(0);
			
			returnUpdateValue = dbConn.timeUpdate(imsiMember, startFinishTime);
			
			if(returnUpdateValue == -1) {
				System.out.println("�ð� �Է��� �ȵƽ��ϴ� " + returnUpdateValue);
				return;
			}
			System.out.println("ȸ�� �ð��� ��ϵǾ����ϴ� " + returnUpdateValue);
			dbConn.close();
		} catch(InputMismatchException e) {
			System.out.println("Ÿ���� ���� �ʽ��ϴ�" + e.getMessage());
		} catch (Exception e) {
			System.out.println("����Ÿ���̽� ����" + e.getMessage());
		}
	}


	public static void memberInputData() {
		boolean value = false;
		
		try {
			System.out.print("ȸ����ȣ �Է� :");
			String memberNumber = sc.nextLine();
			value = checkInputPattern(memberNumber, 1);
			if(!value) return;
			
			System.out.print("ȸ���̸� �Է� :");
			String name = sc.nextLine();
			value = checkInputPattern(name, 2);
			if(!value) return;
			
			System.out.print("ȸ����ȭ��ȣ �Է� :");
			String phoneNumber = sc.nextLine();
			value = checkInputPattern(phoneNumber, 3);
			if(!value) return;

			Member member = new Member(memberNumber, name, phoneNumber);
			//����Ÿ���̽� ����
			DBConnection dbConn = new DBConnection();
			dbConn.connect();
			int insertReturnValue = dbConn.insert(member);
			if(insertReturnValue == -1) {
				System.out.println("���Խ��� �Դϴ�");
			}else {
				System.out.println("���� ����");
			}
			dbConn.close();
		} catch (InputMismatchException e) {
			System.out.println("�Է��� Ÿ���� �����ʽ��ϴ�" + e.getMessage());
		}
		
	}

	public static int playMenu() {
		int  number = -1;
		try {
			//�޴� �Է°� �ֱ�
			System.out.print("-------------------------------------------------------------------------\n"
					+ "| 1.ȸ����� | 2.ȸ����ð���� | 3.�˻� | 4.��� | 5.���� | 6.���� | 7.���� | 8.���� |"
					+"\n-------------------------------------------------------------------------\n>>");
			number = sc.nextInt();
			String pattern = "^[1-8]$";
			boolean regex = Pattern.matches(pattern, String.valueOf(number));
		} catch (InputMismatchException e) {
			System.out.println("���ڸ� �Է� ��Ź�帳�ϴ�");
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
			message= "ȸ����ȣ ���Է� ��Ź�帳�ϴ�";
			break;
		case CHECK_MEMBERNAME :
			pattern = "^[��-�R]{2,4}$";
			message= "ȸ���̸� ���Է� ��Ź�帳�ϴ�";
			break;
		case CHECK_PHONE :
			pattern = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-](\\d{4})$";
			message = "�ڵ��� ��ȣ ���Է� ��Ź�帳�ϴ�";
			break;
		case CHECK_STARTFINISH :
			pattern = "^[1-2]$";
			message = "��ȣ ���Է� ��Ź�帳�ϴ�";
			break;
		case CHECK_SORT: 
			pattern = "^[1-2]$"; 
			message = "����Ÿ�� ���Է¿��";
			break;

		}
		regex = Pattern.matches(pattern, data);
		if(!regex) {
			System.out.println(message);
		}
		return regex;
	}

}
