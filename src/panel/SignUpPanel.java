package panel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.*;

import captcha.CaptchaUtil;
import users.DatabaseUtil;
import utility.Resource;

public class SignUpPanel extends JPanel {
	private JPanel SignUpPanel;
	private JButton btnJoin;
	private JTextField txtId, txtName, txtCaptcha;
	private JPasswordField txtPw, txtPwConfirm;
	private JLabel lblId, lblPw, lblName, lblPwConfirm, lblCaptcha;

	private BufferedImage bgImage; // 배경 이미지

	private CaptchaUtil captchaUtil;
	private DatabaseUtil database = new DatabaseUtil();

	// SignUpPanel 생성자
	public SignUpPanel() {
		captchaUtil = new CaptchaUtil();
		initComponents();

		try {
			// 배경 이미지 저장
			bgImage = new Resource().getResourceImage("../images/background(main).png");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			database.dbOpen();
		} catch (IOException e) {
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

		// 회원가입 패널 그리기
		SignUpPanel = createPanel("SignUpPanel", "회원가입", "아이디", "비밀번호", "이름", "비밀번호 확인");
		add(SignUpPanel);
		SignUpPanel.setBounds(340, 35, 400, 400);
	}

	// 패널에 요소 추가하는 메소드
	private JPanel createPanel(String name, String btnText, String idLabel, String pwLabel, String nameLabel,
			String pwConfirmLabel) {
		JPanel panel = new JPanel(null);
		panel.setOpaque(false); // 패널 배경 투명

		// 라벨, 텍스트, 버튼 추가
		lblId = new JLabel(idLabel);
		lblId.setFont(new Font("Malgun Gothic", Font.PLAIN, 15));
		lblId.setBounds(20, 10, 120, 20);
		panel.add(lblId);

		lblPw = new JLabel(pwLabel);
		lblPw.setFont(new Font("Malgun Gothic", Font.PLAIN, 15));
		lblPw.setBounds(20, 70, 120, 20);
		panel.add(lblPw);

		lblName = new JLabel(nameLabel);
		lblName.setFont(new Font("Malgun Gothic", Font.PLAIN, 15));
		lblName.setBounds(20, 40, 120, 20);
		panel.add(lblName);

		lblPwConfirm = new JLabel(pwConfirmLabel);
		lblPwConfirm.setFont(new Font("Malgun Gothic", Font.PLAIN, 15));
		lblPwConfirm.setBounds(20, 100, 120, 20);
		panel.add(lblPwConfirm);

		lblCaptcha = new JLabel(new ImageIcon(captchaUtil.createCaptcha()));
		lblCaptcha.setBounds(20, 130, 240, 50);
		panel.add(lblCaptcha);

		txtCaptcha = new JTextField();
		txtCaptcha.setBounds(40, 190, 200, 20);
		panel.add(txtCaptcha);

		txtId = new JTextField();
		txtId.setBounds(130, 10, 120, 20);
		panel.add(txtId);

		txtPw = new JPasswordField();
		txtPw.setBounds(130, 70, 120, 20);
		panel.add(txtPw);

		txtName = new JTextField();
		txtName.setBounds(130, 40, 120, 20);
		panel.add(txtName);

		txtPwConfirm = new JPasswordField();
		txtPwConfirm.setBounds(130, 100, 120, 20);
		panel.add(txtPwConfirm);

		btnJoin = new JButton("회원가입");
		btnJoin.setBounds(20, 220, 230, 40);
		panel.add(btnJoin);

		return panel;
	}

	// 회원가입 성공 이벤트를 처리하기 위한 리스너 인터페이스
	public interface SignUpSuccessListener {
		void onSignUpSuccess();
	}

	// 회원가입 버튼 클릭 이벤트 리스너를 등록하는 메소드
	public void setSignUpSuccessListener(SignUpSuccessListener listener) {
		btnJoin.addActionListener(e -> {
			// 입력된 값 저장
			String userId = txtId.getText();
			String pw = new String(txtPw.getPassword());
			String pwConfirm = new String(txtPwConfirm.getPassword());
			String userName = txtName.getText();
			String captcha = txtCaptcha.getText();
			String captchaAnswer = captchaUtil.getCaptchaText();

			System.out.println("userId: " + userId);
			System.out.println("pw: " + pw);
			System.out.println("pwConfirm: " + pwConfirm);
			System.out.println("userName: " + userName);
			System.out.println("captcha: " + captcha);
			System.out.println("captchaAnswer: " + captchaAnswer);

			// 디비에서 해당 아이디가 존재하는지 확인
			boolean existId = database.isIdExist(userId);
			if (existId) {
				// id가 존재할 때
				JOptionPane.showMessageDialog(SignUpPanel.this, "이미 존재하는 아이디입니다.", "오류", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (!pw.equals(pwConfirm)) {
				// 입력돈 비밀번호, 비밀번호 확인이 다를 때
				JOptionPane.showMessageDialog(SignUpPanel.this, "비밀번호가 일치하지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
			} else if (!captcha.equals(captchaAnswer)) {
				// 캡차 입력값이 답과 다를 때
				JOptionPane.showMessageDialog(SignUpPanel.this, "캡차가 일치하지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
			} else {
				try {
					// 비밀번호와 캡차가 일치할 때 회원가입 진행
					database.insertUser(userId, userName, pw);

					// 리스너 수행
					listener.onSignUpSuccess();

				} catch (Exception ex) {
					System.out.println(ex);
					JOptionPane.showMessageDialog(SignUpPanel.this, "회원가입에 실패하였습니다.", "오류", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}

	// 메인 메소드
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
			JFrame frame = new JFrame("signup");
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.getContentPane().add(new SignUpPanel());
			frame.pack();
			frame.setVisible(true);
		});
	}

}