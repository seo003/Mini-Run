package panel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import components.Ground;
import components.Mini;
import components.Obstacles;
import users.DatabaseUtil;
import users.UserDTO;

//게임 화면 패널
class GamePanel extends JPanel implements KeyListener, Runnable {
	private RankPanel rankPanel;
	private JFrame frame;

	// 게임 화면의 너비와 높이
	public static int WIDTH;
	public static int HEIGHT;

	// 애니메이션을 위한 스레드
	private Thread animator;

	// 게임 상태
	private boolean running = false;
	private boolean gameOver = false;

	// 게임 컴포넌트 객체
	Ground ground;
	Mini Mini;
	Obstacles obstacles;

	// 점수
	private int score;

	private UserDTO userDTO = new UserDTO();
	private DatabaseUtil database = new DatabaseUtil();

	// GamePanel 생성자
	public GamePanel(UserDTO userDTO, RankPanel rankPanel, JFrame frame) { // 생성자에서 JFrame 인스턴스를 받아옴
		this.rankPanel = rankPanel;
		this.userDTO = userDTO;
		this.frame = frame; // JFrame 인스턴스를 멤버 변수에 저장

		WIDTH = UserInterface.WIDTH;
		HEIGHT = UserInterface.HEIGHT;

		ground = new Ground(HEIGHT);
		Mini = new Mini();
		obstacles = new Obstacles((int) (WIDTH * 1.5));

		score = 0;
	}

	//패널 그리는 메소드
	public void paint(Graphics g) {
		super.paint(g);

		// 배경 이미지 그리기
		ground.create(g);
		Mini.create(g);
		obstacles.create(g);

		// 텍스트를 배경 이미지 위에 표시
		g.setFont(new Font("Courier New", Font.BOLD, 25));
		FontMetrics fm = g.getFontMetrics();
		int textWidth = fm.stringWidth(Integer.toString(score));
		int x = (WIDTH - textWidth) / 2; // 가운데 정렬
		int y = 40; // 맨 위에 표시

		g.drawString(Integer.toString(score), x, y);
	}

	//게임 스레드에서 실행되는 메소드
	public void run() {
		running = true;

		while (running) {
			updateGame();
			repaint();

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				System.err.println("Thread interrupted during sleep. Stopping the game.");
				running = false;
			}
		}
	}

	//게임 상태 업데이트 메소드
	public void updateGame() {
		String userId = userDTO.getUserId();
		score += 1;

		obstacles.update();

		if (obstacles.hasCollided()) {
			Mini.die();
			repaint();
			running = false;
			gameOver = true;
			System.out.println("collide");
			System.out.println("score: " + score);
			System.out.println("userId: " + userId);
			
			// 디비 저장 코드
			try {
				database.dbOpen();
				int result = database.saveScore(userId, score);
				if (result == 1) {
					System.out.println("저장 성공");

					// 게임이 종료되면 1초 지연 후 RankPanel로 이동
					try {
						Thread.sleep(1000); 
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					// 랭킹을 데이터베이스에서 가져와서 RankPanel에 업데이트
					ArrayList<UserDTO> ranking = new ArrayList<>();
					ranking = database.getRanking();
					rankPanel.updateRanking(ranking);

					// RankPanel의 점수 업데이트
					rankPanel.updateScore(score);
					reset();
					
					// RankPanel로 이동
					CardLayout cardLayout = (CardLayout) frame.getContentPane().getLayout();
					cardLayout.show(frame.getContentPane(), "RankPanel");
				} else {
					System.out.println("저장 실패");
				}
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

	//게임 리셋 메소드
	//재시작 후 스페이스바 입력 시 게임이 진행되지 않는다면 이클립스 코드창 클릭 후 다시 스페이스바 입력
	public void reset() {
		score = 0;
		System.out.println("reset");
		obstacles.resume();
		gameOver = false;

		running = false;

	}

	//키 타이핑 시 호출되는 메소드
	public void keyTyped(KeyEvent e) {
		System.out.println("Key typed: " + e.getKeyChar());
		if (e.getKeyChar() == ' ') {
			if (gameOver)
				reset();
			if (animator == null || !running) {
				System.out.println("Game starts");
				animator = new Thread(this);
				animator.start();
				Mini.startRunning();
			} else {
				Mini.jump();
			}
		}
	}

	//키 눌렸을 시 호출되는 메소드
	public void keyPressed(KeyEvent e) {
		// System.out.println("keyPressed: "+e);
	}
	
	//키 떼질 시 호출되는 메소드
	public void keyReleased(KeyEvent e) {
		// System.out.println("keyReleased: "+e);
	}
}