package swirv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.canvas.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;


public class AppRunner extends Application {

	public static final int width = 500;  //TODO:	Need to fix some issues that prevent dimension
	public static final int height = 500; //		adjustments.
	public static final int CDFE = (int)((50.0/1080.0)*width); //Circle Distance From Edge
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage stage) {
		
		//SETUP FOR STAGE, SCENE, AND CANVAS GRAPHICS
			stage.setTitle("Swirv v1.0.0");
			
			Group root = new Group();
			Scene swirvBase = new Scene(root);
			
			stage.setScene(swirvBase);
			
			Canvas swirvCanvas = new Canvas(width, height);
			root.getChildren().add(swirvCanvas);
			
			GraphicsContext graphics = swirvCanvas.getGraphicsContext2D();
		
		//GAME OBJECTS SETUP
			//BACKGROUND IMAGE SETUP
				Image background = new Image("/images/background.png", width, height, false, false);
				Image backgroundCircle = new Image("/images/circleBackgroundSquare.png", width, height, false, false);
				Image darkCircle = new Image("/images/darkCircle.png", width, height, false, false);
				
			//CLOCKWISE HANDLES
				HandleImpl handleR = new ClockwiseHandle(new Image("/images/traingleR.png", Handle.width, Handle.height, false, false), width-CDFE-Handle.width, height/2.0-Handle.height/2.0, 0);
				HandleImpl handleL = new ClockwiseHandle(new Image("/images/traingleL.png", Handle.width, Handle.height, false, false), CDFE, height/2.0-Handle.height/2.0, 180);
				HandleImpl handleU = new CClockwiseHandle(new Image("/images/traingleU.png", Handle.width, Handle.height, false, false), width/2.0-Handle.height/2.0, CDFE, 90);
				HandleImpl handleD = new CClockwiseHandle(new Image("/images/traingleD.png", Handle.width, Handle.height, false, false), width/2.0-Handle.height/2.0, height-CDFE-Handle.width, 270);
				
			//DEATH OBSTACLES
				class OB {
					ObstacleImpl ob1;
					ObstacleImpl ob2;
					ObstacleImpl ob3;
					int counter;
					OB() {
						ob1 = new DeathObstacle(225,225);
						ob2 = new DeathObstacle(225,225);
						ob3 = new DeathObstacle(225,225);
						counter = 0;
					}
					public void reset() {
						ob1 = new DeathObstacle(225,225);
						ob2 = new DeathObstacle(225,225);
						ob3 = new DeathObstacle(225,225);
						counter = 0;
					}
				}
				OB ob1 = new OB();
		
		//SCORING STUFF
			class GameKeeper {
				int score;
				boolean gameOver;
				int level;
				boolean started;
				boolean paused;
				boolean hasBegun;
				Font swirvFont;
				Font smallSwirvFont;
				Font smallestSwirvFont;
				public GameKeeper() {
					score = 0;
					gameOver = false;
					started = false;
					paused = true;
					hasBegun = false;
					level = 0;
					swirvFont = Font.font("Times New Roman", FontWeight.BOLD, 50);
					smallSwirvFont = Font.font("Times New Roman", FontWeight.BOLD, 30);
					smallestSwirvFont = Font.font("Times New Roman", FontWeight.BOLD, 15);
				}
				public void reset() {
					gameOver = true;
					started = false;
					paused = true;
					hasBegun = false;
					ObstacleImpl.velocity = ObstacleImpl.initialVelocity;
				}
				public void printScore() {
					graphics.setFont(swirvFont);
					graphics.setFill(Color.BLACK);
					graphics.fillText(""+score, 50, 70);
				}
				public void printGameOver() {
					graphics.setFont(swirvFont);
					graphics.setFill(Color.BLACK);
					graphics.fillText("GAME OVER", width/2.0-155, height/2.0-10);
					graphics.setFont(smallestSwirvFont);
					graphics.fillText("<PRESS SPACE TO PLAY AGAIN>", width/2.0-120, height/2.0+10);
				}
				public void printHighScore() {
					graphics.setFill(Color.BLACK);
					graphics.setFont(smallSwirvFont);
					if (score < 10) {
						graphics.fillText(""+score, width/2.0-10, height/2.0-70);
					} else if (score < 100) {
						graphics.fillText(""+score, width/2.0-25, height/2.0-70);
					} else {
						graphics.fillText(""+score, width/2.0-35, height/2.0-70);
					}
//					graphics.setFill(Color.BLACK);
//					graphics.setFont(smallestSwirvFont);
//					graphics.fillText("NEW HIGH SCORE!!!", width/2.0-74, height/2.0-70);
				}
				public void printLevel() {
					graphics.setFont(swirvFont);
					graphics.setFill(Color.BLACK);
					graphics.fillText("lv"+level, width-100, 70);
				}
				public void pause() {
					graphics.setFont(swirvFont);
					graphics.setFill(Color.BLACK);
					graphics.fillText("PAUSED", width/2.0-95, height/2.0+12);
				}
				public void spaceToBegin() {
					graphics.setFont(smallSwirvFont);
					graphics.setFill(Color.BLACK);
					graphics.fillText("<PRESS SPACE TO BEGIN>", width/2.0-195, height/2.0-10);
					graphics.setFont(smallestSwirvFont);
					graphics.fillText("USE RIGHT/LEFT ARROW KEYS TO ROTATE PADDLES", width/2.0-198, height/2.0+35);
				}
			}
			GameKeeper gameKeeper = new GameKeeper();
				
		//ARRAY LIST FOR USER INPUT
			ArrayList<String> input = new ArrayList<String>();
			
		//EVENT LISTENERS
			//KEY LISTENERS
				swirvBase.setOnKeyPressed(
						new EventHandler<KeyEvent>() {
							@Override
							public void handle(KeyEvent e) {
								String code = e.getCode().toString();
								
								if (code.equals("SPACE")) {
									if (!gameKeeper.hasBegun) {
										gameKeeper.gameOver = false;
										gameKeeper.started = !(gameKeeper.started);
										gameKeeper.hasBegun = true;
										gameKeeper.paused = false;
										gameKeeper.score = 0;
										gameKeeper.level = 0;
									} else {
										gameKeeper.paused = !(gameKeeper.paused);
									}
								}
								
								
								if ((!input.contains(code)) && gameKeeper.started) {
									input.add(code);
								}
							}
						}
				);
				swirvBase.setOnKeyReleased(
						new EventHandler<KeyEvent>() {
							@Override
							public void handle(KeyEvent e) {
								String code = e.getCode().toString();
								
								if (input.contains(code)) {
									input.remove(code);
								}
							}
						}
				);
				
		
		//================================MAIN GAME LOOP==========================================//
			new AnimationTimer() {
				@Override
				public void handle(long currentNanoTime) {
					
/**/								if (gameKeeper.hasBegun && !gameKeeper.paused) {
					//COUNTS TIME
						ob1.counter += 1;
									}
						
					//DRAWS BACKGROUND
						graphics.drawImage(background, 0, 0);
						graphics.drawImage(backgroundCircle, 0, 0);
						graphics.drawImage(darkCircle, 0, 0);
					
					//WAIT FOR SPACE KEY PRESS
						if (gameKeeper.gameOver) {
							gameKeeper.printGameOver();
//							if (gameKeeper.score == getHighScore(gameKeeper.score)) {
								gameKeeper.printHighScore();
//							}
						} else if ((!gameKeeper.started)) {
							gameKeeper.spaceToBegin();
						} else if (gameKeeper.paused) {
							gameKeeper.pause();
						}
						
					//ROTATES HANDLES BASED ON KEYBOARD INPUT
						if (input.contains("RIGHT")) {
							handleR.render(graphics);
							handleL.render(graphics);
						} else {
							handleR.renderStill(graphics);
							handleL.renderStill(graphics);
						}
						if (input.contains("LEFT")) {
							handleU.render(graphics);
							handleD.render(graphics);
						} else {
							handleU.renderStill(graphics);
							handleD.renderStill(graphics);
						}
/**/									if (gameKeeper.hasBegun && !gameKeeper.paused) {
					
					//RENDERS OBSTACLES IF THEY ARE WITHIN CIRCLE
						
						if ( ( (ob1.counter) == 1 )) {
							ob1.ob1.setExists(true);
							ob1.ob2.reset();
						}
						
						if (( ( (ob1.counter) == 60*3 ))){
							ob1.ob2.setExists(true);
							ob1.ob3.reset();
						}
						
						if (  (ob1.counter == 60*6 ))  {
							ob1.ob3.setExists(true);
							ob1.ob1.reset();
						}
						
						if (ob1.counter == 60*9) {
							ob1.counter = 0;
						}
						
						if ( (ob1.ob1.exists)  && (ob1.ob1.getPositionY() >= CDFE) ) {
							ob1.ob1.render(graphics);
							if (ob1.ob1.intersects(handleD) || ob1.ob1.intersects(handleU) || ob1.ob1.intersects(handleR) || ob1.ob1.intersects(handleL)) {
								gameKeeper.printGameOver();
								gameKeeper.reset();
								ob1.reset();
							}
							if (ob1.ob1.getPositionY() <= CDFE+.08) {
								//INCREMENT SCORE
									gameKeeper.score += 1;
								//INCREASE OBSTACLE VELOCITY IF SCORE IS MULTIPLE OF SOME #
									if ( (gameKeeper.score % 9 == 0) ) {
										ObstacleImpl.velocity+=.28;
										gameKeeper.level++;
									}
							}
						}
						
						if ( (ob1.ob2.exists)  && (ob1.ob2.getPositionY() >= CDFE) ) {
							ob1.ob2.render(graphics);
							if (ob1.ob2.intersects(handleD) || ob1.ob2.intersects(handleU) || ob1.ob2.intersects(handleR) || ob1.ob2.intersects(handleL)) {
								gameKeeper.printGameOver();
								gameKeeper.reset();
								ob1.reset();
							}
							if (ob1.ob2.getPositionY() <= CDFE+.08) {
								//INCREMENT SCORE
									gameKeeper.score += 1;
								//INCREASE OBSTACLE VELOCITY IF SCORE IS MULTIPLE OF SOME #
									if ( (gameKeeper.score % 9 == 0) ) {
										ObstacleImpl.velocity+=.28;
										gameKeeper.level++;
									}
							}
						}
						
						if ( (ob1.ob3.exists)  && (ob1.ob3.getPositionY() >= CDFE) ) {
							ob1.ob3.render(graphics);
							if (ob1.ob3.intersects(handleD) || ob1.ob3.intersects(handleU) || ob1.ob3.intersects(handleR) || ob1.ob3.intersects(handleL)) {
								gameKeeper.printGameOver();
								gameKeeper.reset();
								ob1.reset();
							}
							if (ob1.ob3.getPositionY() <= CDFE+.08) {
								//INCREMENT SCORE
									gameKeeper.score += 1;
								//INCREASE OBSTACLE VELOCITY IF SCORE IS MULTIPLE OF SOME #
									if ( (gameKeeper.score % 9 == 0) ) {
										ObstacleImpl.velocity+=.28;
										gameKeeper.level++;
									}
							}
						}
										}
					//PRINT SCORE AND LEVEL
						gameKeeper.printScore();
						gameKeeper.printLevel();
						
										
					
				}
			}.start();
			
		//================================MAIN GAME LOOP ENDS========================================//
		
	//SHOW STAGE
		stage.show();
	}
	
	//THIS IS WHERE I ATTEMPTED TO IMPLEMENT A HIGH SCORE MECHANISM LOL
	
//	private int getHighScore(int score) {
//		boolean newHighScore = false;
//		int newHigh = 0;
//		int oldHigh = 0;
//		File file = new File("highscore");
//		try {
//	    	BufferedReader reader = new BufferedReader(new FileReader(file));
//	        String line = reader.readLine();
//	        int highScore = Integer.parseInt(line.trim());
//	        if (score > highScore) { 
//	        	newHigh = score;
//	        	newHighScore = true;
//	        } else {
//	        	oldHigh = highScore;
//	        }
//	        reader.close();
//		} catch (IOException ex) {
//	    	System.err.println("Error reading score from file..."+ex.toString());
//		}
//
//		if (newHighScore) {    
//			try {
//				
//				BufferedWriter output = new BufferedWriter(new FileWriter(file, false));
//				output.write("" + score);
//				output.close();
//			} catch (IOException ex1) {
//				System.out.printf("Error writing score to file: %s\n", ex1);
//			}
//			return score;
//		} else {
//			return oldHigh;
//		}
//	}

}
