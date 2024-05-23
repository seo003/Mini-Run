package captcha;
import nl.captcha.Captcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.noise.StraightLineNoiseProducer;

import java.awt.image.BufferedImage;

public class CaptchaUtil {
	private String captchaText; //캡챠 텍스트 저장 변수

	//캡챠 이미지 생성
	public BufferedImage createCaptcha() {
		Captcha captcha = new Captcha.Builder(200, 50) //크기
				.addText() //글자
				.addBackground(new GradiatedBackgroundProducer()) //그라데이션 배경
				.addNoise(new StraightLineNoiseProducer()) //직선 노이즈
				.addBorder() //캡챠 테두리
				.build();
		
		this.captchaText = captcha.getAnswer();//캡챠 텍스트 저장
		
		return captcha.getImage(); //캡챠 이미지 반환
	}

	//캡챠 텍스트 반환 메소드
	public String getCaptchaText() {
		return this.captchaText;
	}
}
