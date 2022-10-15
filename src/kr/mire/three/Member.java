package kr.mire.three;

import java.io.Serializable;

public class Member implements Comparable<Member>, Serializable{
	//�ʵ� (ȸ����ȣ, �̸�, ��ȭ��ȣ, ���۽ð�, �����ð�, �ѽð�, ���, ���)
	private String memberNumber;
	private String name;
	private String phoneNumber;
	private String startTime;
	private String finishTime;
	private String totalTime;
	private String grade;
	
	//������
	public Member(String memberNumber, String name, String phoneNumber) {
		this(memberNumber,name,phoneNumber,null,null,null,null);
	}

	public Member(String memberNumber, String name, String phoneNumber, String startTime, String finishTime,String totalTime,
			String grade) {
		super();
		this.memberNumber = memberNumber;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.startTime = startTime;
		this.finishTime = finishTime;
		this.totalTime = totalTime;
		this.grade = grade;
	}

	public String getMemberNumber() {
		return memberNumber;
	}

	public void setId(String memberNumber) {
		this.memberNumber = memberNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}

	public String getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(String totalTime) {
		this.totalTime = totalTime;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	@Override
	public int hashCode() {
		return this.memberNumber.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Member)) return false;
		return this.equals(((Member)obj).memberNumber);
	}
	
	@Override
	public int compareTo(Member member) {
		return this.memberNumber.compareToIgnoreCase(member.memberNumber);
	}

	@Override
	public String toString() {
		
		return "====================================================================================================================\n"
				+ "ȸ����ȣ\tȸ���̸�\tȸ���ڵ�����ȣ\t����۽ð�\t\t�����ð�\t\t�ѿ�ð�\t\t\t�Ϸ������\n"+
				memberNumber + "\t" + name + "\t" + phoneNumber + "\t"+ startTime
				+ "\t"+ finishTime + "\t"+  totalTime + "\t"+ grade + 
				"\n====================================================================================================================";
	}

}
