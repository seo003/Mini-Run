package panel;

import javax.swing.*;

import users.DatabaseUtil;
import users.UserDTO;

import java.awt.*;

class UserInterface {
	JFrame mainWindow = new JFrame("Mini Run");
	public static int WIDTH = 700;
	public static int HEIGHT = 400;
	public static DatabaseUtil database = new DatabaseUtil();
	UserDTO userDTO = new UserDTO();

	// GUI 생성 및 표시 메서드
	public void createAndShowGUI() {
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container container = mainWindow.getContentPane();

		// 카드레이아웃 생성
		CardLayout cardLayout = new CardLayout();
		container.setLayout(cardLayout);

		// 로그인 패널 추가
		LoginPanel loginPanel = new LoginPanel();
		container.add(loginPanel, "LoginPanel");

		// 회원가입 패널 추가
		SignUpPanel signUpPanel = new SignUpPanel();
		container.add(signUpPanel, "SignUpPanel");

		// 랭킹 패널 추가
		RankPanel rankPanel = new RankPanel(database);
		container.add(rankPanel, "RankPanel");

		// 게임 패널 추가
		// GamePanel 생성 시 JFrame 인스턴스 넘겨주기
		GamePanel gamePanel = new GamePanel(userDTO, rankPanel, mainWindow);
		gamePanel.addKeyListener(gamePanel);
		gamePanel.setFocusable(true);
		container.add(gamePanel, "GamePanel");

		// 로그인 성공 이벤트 리스너 설정
		loginPanel.setLoginListener(new LoginPanel.LoginListener() {
			@Override
			public void onLoginSuccess() {
				// 패널에 로그인 아이디를 전달하기 위해 로그인 패널에서 아이디 가져옴
				String userId = loginPanel.getUserId();
				System.out.println("userId: " + userId);
				// 아이디 DTO에 저장
				userDTO.setUserId(userId);
				// 게임 패널로 전환
				cardLayout.show(container, "GamePanel");
				gamePanel.requestFocusInWindow();
			}
		});

		// 회원가입 성공 이벤트 리스너 설정
		signUpPanel.setSignUpSuccessListener(new SignUpPanel.SignUpSuccessListener() {
			@Override
			public void onSignUpSuccess() {
				// 로그인 패널로 전환
				cardLayout.show(container, "LoginPanel");
			}
		});

		// 회원가입 버튼 클릭 이벤트 리스너 설정
		loginPanel.setSignUpListener(new LoginPanel.SignUpListener() {
			@Override
			public void onSignUpClick() {
				// 회원가입 버튼을 누르면 회원가입 패널로 전환
				cardLayout.show(container, "SignUpPanel");
			}
		});

		// 랭킹 패널 로그아웃 이벤트 리스너 설정
		rankPanel.setLogoutListener(new RankPanel.LogoutListener() {
			@Override
			public void onLogoutClick() {
				// 로그인 패널로 전환
				cardLayout.show(container, "LoginPanel");
			}
		});

		// 랭킹 패널 재시작 이벤트 리스너 설정
		rankPanel.setRestartListener(new RankPanel.RestartListener() {
			@Override
			public void onRestartClick() {
				// 게임 패널로 전환
				cardLayout.show(container, "GamePanel");
			}
		});

		mainWindow.setSize(WIDTH, HEIGHT);
		mainWindow.setResizable(false);
		mainWindow.setVisible(true);
	}

	// 메인 메소드
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new UserInterface().createAndShowGUI();
			}
		});
	}
}
