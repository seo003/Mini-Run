package utility;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

//이미지 파일을 로드하여 BufferedImage로 변환
public class Resource {
	// 주어진 경로의 이미지 파일을 읽어와 BufferedImage 객체로 반환하는 메소드
	public BufferedImage getResourceImage(String path) {
		BufferedImage img = null;
		try {
			// 상대 경로에서 이미지를 읽어옴
			img = ImageIO.read(getClass().getResource(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}
}
