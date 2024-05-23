package users;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import users.UserDTO;

public class DatabaseUtil {
	String strDriver = "com.mysql.cj.jdbc.Driver"; // MySQL JDBC driver
	String strURL = "jdbc:mysql://localhost:3306/user?characterEncoding=UTF-8&serverTimezone=UTC";
	String strUser = "root"; // user Id
	String strPWD = "root43"; // pw
//	String strURL = "jdbc:mysql://localhost:3306/user?characterEncoding=UTF-8&serverTimezone=UTC";
//	String strUser = "root";
//	String strPWD = "root43";

	Connection DB_con;
	Statement DB_stmt;
	ResultSet DB_rs;

	// 디비 여는 메소드
	public void dbOpen() throws IOException {
		try {
			Class.forName(strDriver); // 드라이버 로드

			DB_con = DriverManager.getConnection(strURL, strUser, strPWD); // 연동
			DB_stmt = DB_con.createStatement();
		} catch (Exception e) {
			System.out.println("SQLException : " + e.getMessage());
		}
	}

	// 디비 닫는 메소드
	public void dbClose() throws IOException {
		try {
			DB_stmt.close();
			DB_con.close();
		} catch (SQLException e) {
			System.out.println("SQLException : " + e.getMessage());
		}
	}

	// 입력된 아이디가 존재하는지 확인하는 메소드
	public boolean isIdExist(String userId) {
		try {
			String query = "SELECT userId FROM users WHERE userId='" + userId + "';";
			ResultSet rs = DB_stmt.executeQuery(query);

			return rs.next(); // 결과가 있으면 userId가 존재함, 그렇지 않으면 존재하지 않음
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			return true; // 예외 발생 시 userId가 존재한다고 가정하고 true 반환
		}
	}

	// 회원가입 시 유저 정보 저장 메소드
	public void insertUser(String userId, String userName, String userPw) {
		try {
			String query = "INSERT INTO users(userId, userName, userPw) VALUES('" + userId + "', '" + userName + "', '"
					+ userPw + "')";
			int result = DB_stmt.executeUpdate(query);

			if (result > 0) {
				System.out.println("회원가입 성공");
			} else {
				System.out.println("회원가입 실패");
			}

		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		}
	}

	// 로그인 시 입력된 아이디로 디비에 있는 비밀번호와 대조하여 일치하는 지 확인하는 메소드
	public int loginUser(String userId, String insertPw) {
		try {
			String query = "SELECT userPw FROM users WHERE userId='" + userId + "';";
			ResultSet rs = DB_stmt.executeQuery(query);
			if (rs.next()) {
				String storedPw = rs.getString("userPw");
				if (storedPw.equals(insertPw)) {
					// 비밀번호 일치
					return 1;
				} else {
					// 비밀번호 불일치
					return 0;
				}
			} else {
				// 해당 userId가 존재하지 않음
				return -1;
			}
		} catch (SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
			return -2;
		}
	}

	// 점수 저장하는 메소드
	public int saveScore(String userId, int currentScore) {
		try {
			String query = "SELECT score FROM users WHERE userId = '" + userId + "';";
			ResultSet rs = DB_stmt.executeQuery(query);
			int score = 0;
			if (rs.next()) {
				score = rs.getInt("score");
			}
			if (score < currentScore) {
				query = "UPDATE users SET score = " + currentScore + " WHERE userId = '" + userId + "';";
				DB_stmt.executeUpdate(query);
			} else {
				System.out.println("기존 점수가 더 큼");
			}
			return 1;
		} catch (Exception e) {
			System.out.println(e);
		}
		return 0;
	}

	// top 5 랭킹 가져오는 메소드
	public ArrayList<UserDTO> getRanking() {
		String sql = "SELECT userName, score FROM users ORDER BY score DESC LIMIT 5";

		ArrayList<UserDTO> ranking = new ArrayList<>();

		try (Connection conn = DriverManager.getConnection(strURL, strUser, strPWD);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				UserDTO user = new UserDTO();
				user.setUserName(rs.getString("userName"));
				user.setScore(rs.getInt("score"));
				ranking.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return ranking;
	}
}
