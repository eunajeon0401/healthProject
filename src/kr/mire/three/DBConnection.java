package kr.mire.three;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

	public class DBConnection {
		private Connection connection = null;
		private Statement statement = null;
		private ResultSet  resultSet = null;
		
		//connection
	public void connect() {
		//properties load
		Properties properties = new Properties();
		FileInputStream  fis;
	
		try {
			//properties 위치 가져오기
			fis = new FileInputStream("C:\\java_test\\healthProject\\src\\kr\\mire\\three\\db.properties");
			properties.load(fis);
		} catch (FileNotFoundException e) {
			System.out.println("FileInputStream Error" + e.getMessage());
		}catch (IOException e) {
			System.out.println("Properties Error" + e.getMessage());
		}
	
		try {
			//드라이버 로드
			Class.forName(properties.getProperty("driver"));
			//데이타베이스 접속요청
			connection = DriverManager.getConnection(properties.getProperty("url"), 
					properties.getProperty("userid"),
					properties.getProperty("password"));
		} catch (ClassNotFoundException e) {
			System.out.println("class.forName load Error" + e.getMessage());
		} catch (SQLException e) {
			System.out.println("Connection Error" + e.getMessage());
		}
			
	}
		//insert Statement
	public int insert(Member member) {
		PreparedStatement ps = null;
		int insertReturnValue = -1;
		String insertQuery ="insert into membertbl(memberNumber,name,phoneNumber) "
							+"values(?,?,?)";
	
		try {
			ps = connection.prepareStatement(insertQuery);
			ps.setString(1, member.getMemberNumber());
			ps.setString(2, member.getName());
			ps.setString(3, member.getPhoneNumber());
	
	
			insertReturnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println("오류발생"+e.getMessage());
		} finally {
			try {
				if(ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("preparStatement or FesultSet Close Error" + e.getMessage());
			}
		}
		return insertReturnValue;
	}
	
	public List<Member> selectSearch(String data) {
		List<Member> list = new ArrayList<Member>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String selectSearchQuery = "select * from membertbl where memberNumber like ? ";
	
		try {
			ps = connection.prepareStatement(selectSearchQuery);
			String memberNumberPattern = "%"+data+"%";
			ps.setString(1, memberNumberPattern);
	
			rs = ps.executeQuery();
			if(!(rs != null || rs.isBeforeFirst())) {
				return list;
			}
			while(rs.next()) {
				String memberNumber = rs.getString("memberNumber");
				String name = rs.getString("name");
				String phoneNumber = rs.getString("phoneNumber");
				String startTime = rs.getString("startTime");
				String finishTime = rs.getString("finishTime");
				String totalTime = rs.getString("totalTime");
				String grade = rs.getString("grade");
			
				list.add(new Member(memberNumber, name, phoneNumber, startTime, finishTime,
					totalTime, grade));
			}
		} catch (SQLException e) {
			System.out.println("selectSearch 오류 발생" + e.getMessage());
		} finally {
			try {
				if(ps != null) {
					ps.close();
				}
			} catch(SQLException e) {
				System.out.println("PrepasStatement or FesultSet Close Error" + e.getMessage());
			}
		}
		return list;
	}
	
	public int timeUpdate(Member member, int timetype) {
		  List<Member> list = new ArrayList<Member>();
		  PreparedStatement ps = null;
		  ResultSet rs = null;
		  int updateReturnValue = -1;
	      String insertQuery = null;
	      
	      try {
	      switch(timetype) {
	      case 1 : insertQuery = "update membertbl set startTime = now() where memberNumber = ? ";
	         ps = connection.prepareStatement(insertQuery);
	         ps.setString(1, member.getMemberNumber());
	         break;
	      case 2 : insertQuery = "CALL procedure_update_membertbl(?,?,now())";
	  		 ps = connection.prepareStatement(insertQuery);
	         ps.setString(1, member.getMemberNumber());
	         ps.setString(2, member.getStartTime());
	         break;
	      default : System.out.println("잘못된 입력 타입입니다");
	      	break;
	      }
	
	     updateReturnValue = ps.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("update 오류발생" + e.getMessage());
		} finally {
			try {
				if(ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("PreparStatement or ResultSet Close" + e.getMessage());
			}
		}	
		return updateReturnValue;
	}
	
	public List<Member> select() {
		List<Member> list = new ArrayList<Member>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String selectQuery = "select * from membertbl";
	
		try {
			ps = connection.prepareStatement(selectQuery);
			rs = ps.executeQuery();
			if(!(rs != null || rs.isBeforeFirst())) {
				return list;
			}
			while(rs.next()){
				String memberNumber = rs.getString("memberNumber");
				String name = rs.getString("name");
				String phoneNumber = rs.getString("phoneNumber");
				String startTime = rs.getString("startTime");
				String finishTime = rs.getString("finishTime");
				String totalTime = rs.getString("totalTime");
				String grade = rs.getString("grade");
				
				list.add(new Member(memberNumber,name,phoneNumber,startTime,finishTime,totalTime,grade));
			}
		} catch (Exception e) {
			System.out.println("select 오류 발생" + e.getMessage());
		} finally {
			try {
				if(ps != null) {
					ps.close();
				}
			}catch (SQLException e) {
				System.out.println("PreparStatement or ResultSet Close Error" + e.getMessage());
			}
		}
		return list;
	}
	
	public int delete(String memberNumber) {
		PreparedStatement ps = null;
		int deleteReturnValue = -1;
		String deleteQuery = "delete from membertbl where memberNumber = ?";
	
		try {
			ps = connection.prepareStatement(deleteQuery);
			ps.setString(1, memberNumber);
			
			deleteReturnValue = ps.executeUpdate();
		} catch(Exception e) {
			System.out.println("delete 오류 발생" + e.getMessage());
		} finally {
			try {
				if(ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("PreparStatement or ResultSet Close Error" + e.getMessage());
			}
		}
		return deleteReturnValue;
	}
		
	public int Update(Member member) {
		PreparedStatement ps = null;
		int updateReturnValue = -1;
		String insertQuery = "update membertbl set name = ?, phoneNumber = ? where memberNumber = ?";
		
		try {
			ps = connection.prepareStatement(insertQuery);
			ps.setString(1, member.getName());
			ps.setString(2, member.getPhoneNumber());
			ps.setString(3, member.getMemberNumber());
			
			updateReturnValue = ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(" update 오류발생 "+ e.getMessage());
		} finally {
			try {
				if(ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("PreparStatement or ResultSet Close Error" + e.getMessage());
			}
		}
		return updateReturnValue;	
	}
	
	public List<Member> selectOrderBy(int type) {
		List<Member> list = new ArrayList<Member>();
		PreparedStatement ps = null;
		ResultSet rs = null;
		String selectOrderByQuery = "select * from membertbl order by ";
	
		try {
			switch(type) {
			case 1 : selectOrderByQuery += "memberNumber asc "; break;
			case 2 : selectOrderByQuery += "name asc "; break;
			default : System.out.println("정렬타입 오류");
			return list;
			}
	
			ps = connection.prepareStatement(selectOrderByQuery);
			rs = ps.executeQuery();
			
			if(!(rs != null || rs.isBeforeFirst())) {
				return list;
			}
			while(rs.next()) {
				String memberNumber = rs.getString("memberNumber");
			String name = rs.getString("name");
			String phoneNumber = rs.getString("phoneNumber");
			String startTime = rs.getString("startTime");
			String finishTime = rs.getString("finishTime");
			String totalTime = rs.getString("totalTime");
			String grade = rs.getString("grade");
				
			list.add(new Member(memberNumber, name, phoneNumber, startTime, finishTime,
						totalTime, grade));
			}
		} catch(Exception e ) {
			System.out.println("select 정렬 오류 발생" +e.getMessage());
		}finally {
			try {
				if(ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				System.out.println("PreparStatement or ResultSet Close Error" + e.getStackTrace());
			}
		}
		return list;
	}
		
	public int getgradeFunc(String grade) {
		Statement st = null;
	    ResultSet rs = null;
	    int count = 0;
	    String gradeQuery = "SELECT getgradeFunc('"+grade+"')";
	    try {
	    	st = connection.prepareStatement(gradeQuery);
	        rs = st.executeQuery(gradeQuery);
	        if(!(rs != null || rs.isBeforeFirst())) {
	           return count;
	        }
	        if(rs.next()) {
	           count = rs.getInt("getgradeFunc('"+grade+"')");
	        }
	    } catch (Exception e) {
	         System.out.println("DBConnection getgradeFunc Error" + e.getMessage());
	    }finally {
	    	try {
	            if (st != null) {
	               st.close();
	            }
	        } catch (SQLException e) {
	            System.out.println("Statement Close Error" + e.getMessage());
	        }
	    }
	    return count;
	}

	public void close() {
		try {
			if(connection != null) {
				connection.close();
			} 
		} catch (SQLException e) {
			System.out.println("Statement or FesultSet  Close Error"+ e.getMessage());
		}
	}
}
	
	 
