package components;

import utility.Resource;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.*;

public class Ground {
	// 지면의 y 좌표
	public static int GROUND_Y;

	// 배경 이미지
	private BufferedImage backgroundImage;

	// Ground 클래스 생성자
	public Ground(int panelHeight) {
		GROUND_Y = (int) (panelHeight - 0.25 * panelHeight);

		try {
			// 새로운 배경 이미지 저장
			backgroundImage = new Resource().getResourceImage("../images/background.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Graphics 객체에 배경 이미지를 그립니다.
	public void create(Graphics g) {
		// 배경 이미지를 그립니다.
		g.drawImage(backgroundImage, 0, 0, null);
	}
}
