package components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;

import utility.Resource;

public class Obstacles {
	// 장애물 객체
	private class Obstacle {
		BufferedImage image;
		int x;
		int y;

		// 장애물 좌표와 크기를 나타내는 사각형 반환하는 메소드
		Rectangle getObstacle() {
			Rectangle obstacle = new Rectangle();
			obstacle.x = x;
			obstacle.y = y;
			obstacle.width = image.getWidth();
			obstacle.height = image.getHeight();

			return obstacle;
		}
	}

	// 첫 번째 장애물의 초기 위치 및 간격, 이동 속도
	private int firstX;
	private int obstacleInterval;
	private int movementSpeed;

	// 이미지와 장애물 리스트
	private ArrayList<BufferedImage> imageList;
	private ArrayList<Obstacle> obList;

	// 충돌된 위치를 저장하는 변수
	private Obstacle blockedAt;

	// Obstacles 생성자
	public Obstacles(int firstPos) {
		obList = new ArrayList<Obstacle>();
		imageList = new ArrayList<BufferedImage>();

		firstX = firstPos;
		obstacleInterval = 200;
		movementSpeed = 11;

		// 장애물 이미지 리스트에 추가
		imageList.add(new Resource().getResourceImage("../images/stone1.png"));
		imageList.add(new Resource().getResourceImage("../images/stone2.png"));
		imageList.add(new Resource().getResourceImage("../images/stone3.png"));
		imageList.add(new Resource().getResourceImage("../images/stone4.png"));

		int x = firstX;

		// 이미지 리스트를 기반으로 장애물 객체 생성 및 리스트에 추가
		for (BufferedImage bi : imageList) {

			Obstacle ob = new Obstacle();

			ob.image = bi;
			ob.x = x;
			ob.y = Ground.GROUND_Y - bi.getHeight() + 5;
			x += obstacleInterval;

			obList.add(ob);
		}
	}

	// 장애물 위치 업데이트 메소드
	public void update() {
		Iterator<Obstacle> looper = obList.iterator();

		Obstacle firstOb = looper.next();
		firstOb.x -= movementSpeed;

		while (looper.hasNext()) {
			Obstacle ob = looper.next();
			ob.x -= movementSpeed;
		}

		Obstacle lastOb = obList.get(obList.size() - 1);

		// 첫 번째 장애물이 화면 왼쪽으로 벗어나면 리스트의 맨 뒤로 이동
		if (firstOb.x < -firstOb.image.getWidth()) {
			obList.remove(firstOb);
			firstOb.x = obList.get(obList.size() - 1).x + obstacleInterval;
			obList.add(firstOb);
		}
	}

	// 장애물 그리는 메소드
	public void create(Graphics g) {
		for (Obstacle ob : obList) {
			g.setColor(Color.black);
			g.drawImage(ob.image, ob.x, ob.y, null);
		}
	}

	// Mini와 장애물 간 충돌 확인 메소드
	public boolean hasCollided() {
		for (Obstacle ob : obList) {
			if (Mini.getMini().intersects(ob.getObstacle())) {
				System.out.println("Mini = " + Mini.getMini() + "\nObstacle = " + ob.getObstacle() + "\n\n");
				blockedAt = ob;
				return true; // 충돌하면 true
			}
		}
		return false;
	}

	// 게임 재시작할 시 장애물 리스트 초기화 메소드
	public void resume() {
		int x = firstX / 2;
		obList = new ArrayList<Obstacle>();

		// 이미지 리스트를 기반으로 장애물 객체를 생성하고 리스트에 추가
		for (BufferedImage bi : imageList) {

			Obstacle ob = new Obstacle();

			ob.image = bi;
			ob.x = x;
			ob.y = Ground.GROUND_Y - bi.getHeight() + 5;
			x += obstacleInterval;

			obList.add(ob);
		}
	}
}