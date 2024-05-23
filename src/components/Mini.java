package components;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import utility.Resource;

public class Mini {

	// Mini의 상태를 나타내는 정적 변수들
	private static int MiniBaseY, MiniTopY, MiniStartX, MiniEndX;
	private static int MiniTop, MiniBottom, topPoint;
	private static boolean topPointReached;
	private static int jumpFactor = 20;

	public static final int STAND_STILL = 1, // 정지
			RUNNING = 2, // 달리는중
			JUMPING = 3, // 점프
			DIE = 4; // 죽음
	private final int LEFT_FOOT = 1, // 왼발
			RIGHT_FOOT = 2, // 오른발
			NO_FOOT = 3; // 없음(정지)

	private static int state; // 현재 Mini의 상태를 나타내는 변수

	private int foot; // 발 뒷부분 상태를 나타내는 변수

	// 이미지 저장 변수
	static BufferedImage image; // Mini
	BufferedImage leftFootMini; // 왼발 들기
	BufferedImage rightFootMini; // 오른발 들기
	BufferedImage deadMini; // 죽음

	// Mini 클래스 생성자
	public Mini() {
		// 각 이미지 저장
		image = new Resource().getResourceImage("../images/dog_right.png");
		leftFootMini = new Resource().getResourceImage("../images/dog_left.png");
		rightFootMini = new Resource().getResourceImage("../images/dog_right.png");
		deadMini = new Resource().getResourceImage("../images/dog_tears.png");

		// Mini의 초기 위치와 상태 설정
		MiniBaseY = Ground.GROUND_Y + 5;
		MiniTopY = Ground.GROUND_Y - image.getHeight() + 5;
		MiniStartX = 100;
		MiniEndX = MiniStartX + image.getWidth();
		topPoint = MiniTopY - 120;

		state = 1; // 초기상태: 정지
		foot = NO_FOOT; // 발 상태:정지
	}

	// Mini의 요소 생성 및 화면에 표시
	public void create(Graphics g) {
		// Mini의 아래쪽 좌표를 계산
		MiniBottom = MiniTop + image.getHeight();

		// Mini의 상태에 따라 다른 그래픽 요소를 그려줍니다.
		switch (state) {

		case STAND_STILL: // 정지
			System.out.println("stand");
			// 정지 상태의 Mini 이미지 그리기
			g.drawImage(image, MiniStartX, MiniTopY, null);
			break;

		case RUNNING: // 달리는중
			if (foot == NO_FOOT) { // 정지상태일때
				foot = LEFT_FOOT;
				// 왼발 들기 상태의 Mini 이미지 그리기
				g.drawImage(leftFootMini, MiniStartX, MiniTopY, null);
			} else if (foot == LEFT_FOOT) { // 왼발일때
				foot = RIGHT_FOOT;
				// 오른발 들기 상태의 Mini 이미지 그리기
				g.drawImage(rightFootMini, MiniStartX, MiniTopY, null);
			} else { // 오른발일때
				// 왼발 들기 상태로 전환 후 Mini 이미지 그리기
				foot = LEFT_FOOT;
				g.drawImage(leftFootMini, MiniStartX, MiniTopY, null);
			}
			break;

		case JUMPING: // 점프
			if (MiniTop > topPoint && !topPointReached) {
				g.drawImage(image, MiniStartX, MiniTop -= jumpFactor, null);
				break;
			}
			if (MiniTop >= topPoint && !topPointReached) {
				topPointReached = true;
				g.drawImage(image, MiniStartX, MiniTop += jumpFactor, null);
				break;
			}
			if (MiniTop > topPoint && topPointReached) {
				if (MiniTopY == MiniTop && topPointReached) {
					state = RUNNING;
					topPointReached = false;
					break;
				}
				g.drawImage(image, MiniStartX, MiniTop += jumpFactor, null);
				break;
			}
		case DIE:
			// Mini가 죽은 상태의 Mini 이미지 글기ㅣ
			g.drawImage(deadMini, MiniStartX, MiniTop, null);
			break;
		}
	}

	// die로 변환하는 메소드
	public void die() {
		// 현재 상태 die로 설정
		state = DIE;
	}

	public static Rectangle getMini() {
		// Mini의 바운딩 박스(경계 상자)를 반환합니다.

		Rectangle Mini = new Rectangle();
		Mini.x = MiniStartX;

		if (state == JUMPING && !topPointReached)
			Mini.y = MiniTop - jumpFactor;
		else if (state == JUMPING && topPointReached)
			Mini.y = MiniTop + jumpFactor;
		else if (state != JUMPING)
			Mini.y = MiniTop;

		Mini.width = image.getWidth();
		Mini.height = image.getHeight();

		return Mini;
	}

	public void startRunning() {
		// running으로 설정
		MiniTop = MiniTopY;
		state = RUNNING;
	}

	public void jump() {
		// 점프 상태로 설정
		MiniTop = MiniTopY;
		topPointReached = false;
		state = JUMPING;
	}

	public void reset() {
		// mini 초기화
		MiniBaseY = Ground.GROUND_Y + 5;
		MiniTopY = Ground.GROUND_Y - image.getHeight() + 5;
		MiniStartX = 100;
		MiniEndX = MiniStartX + image.getWidth();
		topPoint = MiniTopY - 120;

		state = STAND_STILL; // 정지 상태 설정
		foot = NO_FOOT; // 발 정지상태 설정

		MiniTop = MiniTopY;
		topPointReached = false;
	}
}
