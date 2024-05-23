package panel;

import javax.swing.*;

import users.DatabaseUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import users.DatabaseUtil;
import users.UserDTO;
import utility.Resource;

import java.util.ArrayList;
import java.util.List;

// 랭크 패널
public class RankPanel extends JPanel {
	private BufferedImage bgImage; // 배경 이미지

	// 레이블, 버튼 선언
	private JLabel scoreLabel;
	private JLabel rankTitleLabel;
	private JLabel rankLabel;
	private JLabel rankNameLabel;
	private JLabel rankScoreLabel;
	private JButton btnMain, btnPlay;

	// RankPanel 생성자
	public RankPanel(DatabaseUtil database) {
		bgImage = new Resource().getResourceImage("../images/background.png");

		setOpaque(false);
		setLayout(null); // 레이아웃 매니저를 null로 설정

		// 버튼 초기화
		btnMain = new JButton("로그아웃");
		btnPlay = new JButton("재시작");
	}

	// 랭크 정보 업데이트 메소드
	public void updateRanking(List<UserDTO> ranking) {
		// 기존의 순위 정보를 모두 제거
		removeAll();

		// 점수 라벨 설정
		scoreLabel = new JLabel("Score: 0");
		scoreLabel.setBounds(120, 120, 200, 30);
		scoreLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 25));
		add(scoreLabel);

		// 메인 페이지 버튼
		btnMain.setBounds(90, 200, 100, 30);
		add(btnMain);

		// 리플레이 페이지 버튼
		btnPlay.setBounds(200, 200, 75, 30);
		add(btnPlay);

		// 라벨 설정
		rankTitleLabel = new JLabel("Ranking Top 5");
		rankTitleLabel.setBounds(400, 40, 200, 35);
		rankTitleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 25));
		add(rankTitleLabel);

		rankLabel = new JLabel("순위");
		rankLabel.setBounds(400, 85, 50, 35);
		rankLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 15));
		add(rankLabel);

		rankNameLabel = new JLabel("이름");
		rankNameLabel.setBounds(470, 85, 50, 35);
		rankNameLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 15));
		add(rankNameLabel);

		rankScoreLabel = new JLabel("점수");
		rankScoreLabel.setBounds(540, 85, 50, 35);
		rankScoreLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 15));
		add(rankScoreLabel);

		// 순위 정보 설정
		for (int i = 0; i < Math.min(10, ranking.size()); i++) {
			UserDTO user = ranking.get(i);

			// 랭킹 순위 레이블
			JLabel rankLabel = new JLabel(Integer.toString(i + 1));
			rankLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 15));
			rankLabel.setBounds(400, 120 + i * 30, 50, 30);
			add(rankLabel);

			// 이름 레이블
			JLabel nameLabel = new JLabel(user.getUserName());
			nameLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 15));
			nameLabel.setBounds(470, 120 + i * 30, 100, 30);
			add(nameLabel);

			// 랭킹 점수 레이블
			JLabel scoreLabel = new JLabel(Integer.toString(user.getScore()));
			scoreLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 15));
			scoreLabel.setBounds(540, 120 + i * 30, 50, 30);
			add(scoreLabel);
		}

		// 패널을 다시 그리기
		revalidate();
		repaint();
	}

	// 패널에 배경 이미지 그리는 메소드
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(bgImage, 0, 0, null);
	}

	// 점수 업데이트 메서드
	public void updateScore(int score) {
		scoreLabel.setText("Score: " + score);
	}

	// 로그아웃 이벤트를 처리하기 위한 리스너 인터페이스
	public interface LogoutListener {
		void onLogoutClick();
	}

	// 로그아웃 버튼 클릭 이벤트 리스너를 등록하는 메소드
	public void setLogoutListener(LogoutListener listener) {
		btnMain.addActionListener(e -> {
			listener.onLogoutClick();
		});
	}

	// 재시작 이벤트를 처리하기 위한 리스너 인터페이스
	public interface RestartListener {
		void onRestartClick();
	}

	// 재시작 버튼 클릭 이벤트 리스너를 등록하는 메소드
	public void setRestartListener(RestartListener listener) {
		btnPlay.addActionListener(e -> {
			listener.onRestartClick();
		});
	}
}
