package panel;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.swing.*;

import users.DatabaseUtil;
import users.UserDTO;
import utility.Resource;

//로그인 패널
public class LoginPanel extends JPanel {
	private JPanel loginPanel;
	private JButton btnLogin, btnJoin;
	private JTextField txtLoginId;
	private JPasswordField txtLoginPw;
	private JLabel lblLoginId, lblLoginPw;

	private BufferedImage bgImage;
	private DatabaseUtil database = new DatabaseUtil();
	private UserDTO userDTO = new UserDTO();

	// 회원가입 버튼 클릭 이벤트를 처리하기 위한 리스너 인터페이스
	public interface SignUpListener {
		void onSignUpClick();
	}

	// 회원가입 버튼 클릭 이벤트 리스너를 등록하는 메서드
	public void setSignUpListener(SignUpListener listener) {
		btnJoin.addActionListener(e -> listener.onSignUpClick());
	}

	// userId를 받아오기 위한 메소드
	public String getUserId() {
		return userDTO.getUserId();
	}

	// LoginPanel 생성자
	public LoginPanel() {
		initComponents();

		try {
			// 배경 이미지 저장
			bgImage = new Resource().getResourceImage("../images/background(main).png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 패널에 배경 이미지를 그리는 메소드
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (bgImage != null) {
			g.drawImage(bgImage, 0, 0, null);
		}
	}

	// 패널 요소들을 초기화하는 메소드
	private void initComponents() {
		setLayout(null);

		// 로그인 패널 그리기
		loginPanel = createPanel("LoginPanel", "게임시작", "아이디", "비밀번호");
		add(loginPanel);
		loginPanel.setBounds(340, 90, 297, 228);
		try {
			database.dbOpen();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// 패널에 요소 추가하는 메소드
	private JPanel createPanel(String name, String btnText, String idLabel, String pwLabel) {
		JPanel panel = new JPanel(null);
		panel.setOpaque(false); // 패널 배경 투명

		// 라벨, 텍스트, 버튼 추가
		lblLoginId = new JLabel(idLabel);
		lblLoginId.setFont(new Font("Malgun Gothic", Font.PLAIN, 15));
		lblLoginId.setBounds(20, 30, 100, 20);
		panel.add(lblLoginId);

		lblLoginPw = new JLabel(pwLabel);
		lblLoginPw.setFont(new Font("Malgun Gothic", Font.PLAIN, 15));
		lblLoginPw.setBounds(20, 60, 100, 20);
		panel.add(lblLoginPw);

		txtLoginId = new JTextField();
		txtLoginId.setBounds(100, 30, 120, 20);
		panel.add(txtLoginId);

		txtLoginPw = new JPasswordField();
		txtLoginPw.setBounds(100, 60, 120, 20);
		panel.add(txtLoginPw);

		btnLogin = new JButton(btnText);
		btnLogin.setBounds(21, 90, 90, 25);
		panel.add(btnLogin);

		btnJoin = new JButton("회원가입");
		btnJoin.setBounds(129, 90, 90, 25);
		panel.add(btnJoin);

		return panel;
	}

	// 로그인 성공 이벤트를 처리하기 위한 리스너 인터페이스
	public interface LoginListener {
		void onLoginSuccess();
	}

	// 로그인 버튼 클릭 이벤트 리스너를 등록하는 메소드
	public void setLoginListener(LoginListener listener) {
		btnLogin.addActionListener(e -> {
			// 입력된 값 저장
			String userId = txtLoginId.getText();
			String userPw = new String(txtLoginPw.getPassword());

			// 디비에서 로그인 확인
			try {
				int result = database.loginUser(userId, userPw);
				if (result == 1) {
					// 유저 아이디 설정
					userDTO.setUserId(userId);

					// 버튼 내용 초기화
					txtLoginId.setText("");
					txtLoginPw.setText("");

					// 리스너 수행
					listener.onLoginSuccess();
				} else if (result == 0) {
					JOptionPane.showMessageDialog(LoginPanel.this, "비밀번호가 일치하지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);

				} else if (result == -1) {
					JOptionPane.showMessageDialog(LoginPanel.this, "존재하지 않는 사용자입니다.", "오류", JOptionPane.ERROR_MESSAGE);

				} else {
					JOptionPane.showMessageDialog(LoginPanel.this, "데이터베이스 오류", "오류", JOptionPane.ERROR_MESSAGE);

				}
			} catch (Exception ex) {
				System.out.println(ex);
				JOptionPane.showMessageDialog(LoginPanel.this, "데이터베이스 오류", "오류", JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	// 메인 메서드
	public static void main(String args[]) {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}

		java.awt.EventQueue.invokeLater(() -> {
			JFrame frame = new JFrame("Login");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().add(new LoginPanel());
			frame.pack();
			frame.setVisible(true);
		});
	}

}