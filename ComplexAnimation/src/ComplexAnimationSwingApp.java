import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class ComplexAnimationSwingApp extends JFrame implements ActionListener {

	private static final long serialVersionUID = 623383181217513898L;
	int x = 0;
	int xBackground = 0;
	int y = 510;
	int velocityX;
	int velocityY;
	int speed = 0;
	int directionBefore = 0;
	Timer timer = new Timer(100, this);
	DrawPanel draw = new DrawPanel();

	boolean isWalking = false;
	boolean isJumping = false;
	boolean isCrouching = false;
	boolean isCrouchingAndWalking = false;

	Thread animator;

	boolean k = false;
	boolean doneJumping = false;
	boolean isAtPeak = false;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ComplexAnimationSwingApp gui = new ComplexAnimationSwingApp();
				gui.playMusic();
			}
		});
	}

	public ComplexAnimationSwingApp() {

		JFrame frame = new JFrame();
		frame.addKeyListener(new ListenToKeys());
		frame.setFocusable(true);
		frame.setFocusTraversalKeysEnabled(false);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(draw);
		frame.setSize(1280, 800);
		frame.setVisible(true);
	}

	public synchronized void playMusic() {
		try {
			InputStream in = new FileInputStream("aeon_0.wav");
			AudioStream as = new AudioStream(in);
			AudioPlayer.player.start(as);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void walk() {

		x = x + velocityX;
		if (x > 1300 && velocityX > 0) {
			x = -45;
			x = x + velocityX;
		} else if (x < -45 && velocityX < 0) {
			x = 1300;
			x = x + velocityX;
		}
		
		if (velocityX > 0){
			xBackground = xBackground + 2;
			draw.repaint();
		}
		else if (velocityX < 0){
			xBackground = xBackground - 2;
			draw.repaint();
		}
		draw.repaint();
	}

	public void jump() {

		if (isAtPeak == false) {
			isJumping = true;
			y -= 4;
		}
		if (y <= 380) {
			isAtPeak = true;
		}
		if (isAtPeak == true && y <= 510) {
			isJumping = true;
			y += 4;
			if (y == 510) {
				isJumping = false;
				doneJumping = true;
			}
		}
		draw.repaint();
	}

	class DrawPanel extends JPanel {

		private static final long serialVersionUID = -9091687230503558275L;
		private Image[] imagesRight = new Image[3];
		private Image[] imagesLeft = new Image[3];
		private Image[] imagesCrouchingLeft = new Image[3];
		private Image[] imagesCrouchingRight = new Image[3];

		private int current = 0;

		public Image getNextImageRight() {
			if (current == imagesRight.length) {
				current = 0;
			}
			return imagesRight[current++];
		}

		public Image getNextImageLeft() {
			if (current == imagesLeft.length) {
				current = 0;
			}
			return imagesLeft[current++];
		}

		public Image getNextImageCrouchingLeft() {
			if (current == imagesCrouchingLeft.length) {
				current = 0;
			}
			return imagesCrouchingLeft[current++];
		}

		public Image getNextImageCrouchingRight() {
			if (current == imagesCrouchingRight.length) {
				current = 0;
			}
			return imagesCrouchingRight[current++];
		}

		public void paintComponent(Graphics g) {

			if (velocityY == 10 && k == false) {
				k = true;
				animator = new Thread(new animator());
				animator.start();
			}
			timer.start();
			// Color lightBlue = new Color(135, 206, 250);
			// Color grassGreen = new Color(34, 139, 34);
			// Color gold = new Color(255, 215, 0);

			Image background = new ImageIcon("background.png").getImage();

			imagesRight[0] = new ImageIcon("walk1.png").getImage();
			imagesRight[1] = new ImageIcon("walk2.png").getImage();
			imagesRight[2] = new ImageIcon("walk3.png").getImage();
			imagesLeft[0] = new ImageIcon("walk2left.png").getImage();
			imagesLeft[1] = new ImageIcon("walk1left.png").getImage();
			imagesLeft[2] = new ImageIcon("walk3left.png").getImage();
			imagesCrouchingLeft[0] = new ImageIcon("crouchleft.png").getImage();
			imagesCrouchingLeft[1] = new ImageIcon("crouch2left.png")
					.getImage();
			imagesCrouchingLeft[2] = new ImageIcon("crouch3left.png")
					.getImage();
			imagesCrouchingRight[0] = new ImageIcon("crouch.png").getImage();
			imagesCrouchingRight[1] = new ImageIcon("crouch2right.png")
					.getImage();
			imagesCrouchingRight[2] = new ImageIcon("crouch3right.png")
					.getImage();
			Image manWalkingRight = new ImageIcon(getNextImageRight())
					.getImage();
			Image manWalkingLeft = new ImageIcon(getNextImageLeft()).getImage();
			Image manCrouchingAndWalkingLeft = new ImageIcon(
					getNextImageCrouchingLeft()).getImage();
			Image manCrouchingAndWalkingRight = new ImageIcon(
					getNextImageCrouchingRight()).getImage();
			Image manJumpingRight = new ImageIcon("jump.png").getImage();
			Image manStillRight = new ImageIcon("walk2.png").getImage();
			Image manCrouchingRight = new ImageIcon("crouch.png").getImage();
			Image manJumpingLeft = new ImageIcon("jumpleft.png").getImage();
			Image manStillLeft = new ImageIcon("walk2left.png").getImage();
			Image manCrouchingLeft = new ImageIcon("crouchleft.png").getImage();
			g.drawImage(background, xBackground, 0, this);

			// Image cloud1 = new ImageIcon("cloud.png").getImage();
			try {
				if (velocityX > 0) {
					if (isWalking == true && isJumping == false
							&& isCrouching == false)
						g.drawImage(manWalkingRight, x, y, this);
					else if (isWalking == false && isJumping == false
							&& isCrouching == false
							&& isCrouchingAndWalking == false)
						g.drawImage(manStillRight, x, y, this);
					else if (isJumping == true)
						g.drawImage(manJumpingRight, x, y, this);
					else if (isCrouching == true)
						g.drawImage(manCrouchingRight, x, y + 27, this);
					else if (isCrouchingAndWalking == true)
						g.drawImage(manCrouchingAndWalkingRight, x, y + 27,
								this);
				} else if (velocityX < 0) {
					if (isWalking == true && isJumping == false
							&& isCrouching == false)
						g.drawImage(manWalkingLeft, x, y, this);
					else if (isWalking == false && isJumping == false
							&& isCrouching == false
							&& isCrouchingAndWalking == false)
						g.drawImage(manStillLeft, x, y, this);
					else if (isJumping == true)
						g.drawImage(manJumpingLeft, x, y, this);
					else if (isCrouching == true)
						g.drawImage(manCrouchingLeft, x, y + 27, this);
					else if (isCrouchingAndWalking == true)
						g.drawImage(manCrouchingAndWalkingLeft, x, y + 27, this);
				} else if (velocityX == 0 && directionBefore == 1) {
					if (isCrouching == true)
						g.drawImage(manCrouchingRight, x, y + 27, this);
					else if (isJumping == true)
						g.drawImage(manJumpingRight, x, y, this);
					else if (isWalking == false && isJumping == false
							&& isCrouching == false
							&& isCrouchingAndWalking == false)
						g.drawImage(manStillRight, x, y, this);
				} else if (velocityX == 0 && directionBefore == -1) {
					if (isCrouching == true)
						g.drawImage(manCrouchingLeft, x, y + 27, this);
					else if (isJumping == true)
						g.drawImage(manJumpingLeft, x, y, this);
					else if (isWalking == false && isJumping == false
							&& isCrouching == false
							&& isCrouchingAndWalking == false)
						g.drawImage(manStillLeft, x, y, this);
				} else if (velocityX == 0 && directionBefore == 0) {
					if (isCrouching == true)
						g.drawImage(manCrouchingRight, x, y + 27, this);
					else if (isJumping == true)
						g.drawImage(manJumpingRight, x, y, this);
					else if (isWalking == false && isJumping == false
							&& isCrouching == false
							&& isCrouchingAndWalking == false)
						g.drawImage(manStillRight, x, y, this);
				}
			} finally {
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	class ListenToKeys implements KeyListener {

		private final Set<Integer> pressed = new HashSet<Integer>();

		public synchronized void keyPressed(KeyEvent e) {
			pressed.add(e.getKeyCode());
			if (pressed.contains(KeyEvent.VK_UP)
					&& pressed.contains(KeyEvent.VK_RIGHT)) {
				velocityX = 10 + speed;
				velocityY = 10;
				isWalking = false;
				isJumping = true;
				isCrouching = false;
				isCrouchingAndWalking = false;
				directionBefore = 1;
				walk();
				jump();

			}

			else if (pressed.contains(KeyEvent.VK_UP)
					&& pressed.contains(KeyEvent.VK_LEFT)) {
				// repaint();
				velocityX = -10 - speed;
				velocityY = 10;
				isWalking = false;
				isJumping = true;
				isCrouching = false;
				isCrouchingAndWalking = false;
				directionBefore = -1;
				walk();
				jump();

			} else if (pressed.contains(KeyEvent.VK_DOWN)
					&& pressed.contains(KeyEvent.VK_RIGHT)) {
				velocityX = 10 + speed;
				isWalking = false;
				isJumping = false;
				isCrouching = false;
				isCrouchingAndWalking = true;
				directionBefore = 1;
				walk();
			}

			else if (pressed.contains(KeyEvent.VK_DOWN)
					&& pressed.contains(KeyEvent.VK_LEFT)) {
				velocityX = -10 - speed;
				isWalking = false;
				isJumping = false;
				isCrouching = false;
				isCrouchingAndWalking = true;
				directionBefore = -1;
				walk();
			}

			else if (pressed.contains(KeyEvent.VK_RIGHT)) {
				// repaint();
				velocityX = 10 + speed;
				// velocityY = 10;
				isWalking = true;
				isJumping = false;
				isCrouching = false;
				isCrouchingAndWalking = false;
				directionBefore = 1;
				walk();
				// jump();

			}

			else if (pressed.contains(KeyEvent.VK_LEFT)) {
				// repaint();
				velocityX = -10 - speed;
				// velocityY = 10;
				isWalking = true;
				isJumping = false;
				isCrouching = false;
				isCrouchingAndWalking = false;
				directionBefore = -1;
				walk();
				// jump();

			} else if (pressed.contains(KeyEvent.VK_UP)) {
				velocityY = 10;
				jump();
				isWalking = false;
				isJumping = true;
				isCrouching = false;
				isCrouchingAndWalking = false;
				// directionBefore = 1;
			} else if (pressed.contains(KeyEvent.VK_DOWN)) {
				velocityX = 0;
				isWalking = false;
				isJumping = false;
				isCrouching = true;
				isCrouchingAndWalking = false;
				walk();
			} else if (pressed.contains(KeyEvent.VK_D)) {
				speed = speed + 2;
			} else if (pressed.contains(KeyEvent.VK_A)) {
				speed = speed - 2;
				if (speed < 0) {
					speed = 0;
				}
			}
		}

		public synchronized void keyReleased(KeyEvent e) {
			// int code = e.getKeyCode();
			if (pressed.contains(KeyEvent.VK_UP)) {
				velocityY = 0;
			}
			pressed.remove(e.getKeyCode());

		}

		public synchronized void keyTyped(KeyEvent e) {
		}
	}

	public class animator implements Runnable {

		@Override
		public void run() {
			long beforeTime, deltaTime, sleepTime;
			beforeTime = System.currentTimeMillis();
			while (doneJumping == false) {
				jump();
				deltaTime = System.currentTimeMillis() - beforeTime;
				sleepTime = 10 - deltaTime;
				if (sleepTime < 0) {
					sleepTime = 2;
				}
				try {
					Thread.sleep(sleepTime);
				} catch (Exception e) {
				}
				beforeTime = System.currentTimeMillis();
			}
			doneJumping = false;
			isAtPeak = false;
			k = false;
		}

	}
}
