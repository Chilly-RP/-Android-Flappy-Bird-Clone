package com.ruipeng.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;


import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	ShapeRenderer shapeRenderer;
	Texture gameOver;
	Texture title;
	Texture rp;
	Texture start;
	Texture death;
	Texture floor;
	Texture bird;

	Texture one;
	Texture zero;
	Texture two;
	Texture three;
	Texture four;
	Texture five;
	Texture six;
	Texture seven;
	Texture eight;
	Texture nine;

	Texture scoreBoard;

	Vector3 tmp;

	Texture[] birds;
	Texture topTube;
	Texture bottomTube;
	int flapState = 0;
	float birdY = 0;
	float birdX = 0;
	float velocity = 0;
	Circle birdCircle;
	int score = 0;
	int scoringTube = 0;
	BitmapFont font;
	int lastIndex = 0;

	int gameState = 0;
	float gravity = 1;
	float gap = 400;
	float maxTubeOffset;

	int sourceX = 0;

	Random randomGenerator;

	float tubeVelocity = 5;


	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	float distanceBetweenTubes;
	Rectangle[] topTubeRectangles;
	Rectangle[] bottomTubeRectangles;

	Rectangle startRectangle;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		gameOver = new Texture("gameover.png");
		title = new Texture("title.png");
		rp = new Texture("rp.png");
		start = new Texture("start.png");
		death = new Texture("death.png");
		floor = new Texture("floor.png");
		bird = new Texture("bird.png");

		one = new Texture("1.png");
		zero = new Texture("0.png");
		two = new Texture("2.png");
		three = new Texture("3.png");
		four = new Texture("4.png");
		five = new Texture("5.png");
		six = new Texture("6.png");
		seven = new Texture("7.png");
		eight = new Texture("8.png");
		nine = new Texture("9.png");

		scoreBoard = new Texture("score.png");

		shapeRenderer = new ShapeRenderer();
		birdCircle = new Circle();
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10f);



		//Create birds array
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");


		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
		randomGenerator = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth()  * 3 / 4;
		topTubeRectangles = new Rectangle[numberOfTubes];
		bottomTubeRectangles = new Rectangle[numberOfTubes];

		startRectangle = new Rectangle();
		floor.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

		startGame();


	}

	public void startGame(){
		birdY = Gdx.graphics.getHeight() / 2 - birds[0].getHeight() / 2;

		for (int i = 0; i < numberOfTubes; i++) {

			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
			tubeX[i] = Gdx.graphics.getWidth() /2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

			topTubeRectangles[i] = new Rectangle();
			bottomTubeRectangles[i] = new Rectangle();
		}
	}

	@Override
	public void render () {

		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


		if(gameState == 1) {

			if(tubeX[scoringTube] < Gdx.graphics.getWidth() / 2){
				score ++;

				//Gdx.app.log("Score", String.valueOf(score));

				if (scoringTube < numberOfTubes - 1){
					scoringTube ++;
				}else{
					scoringTube = 0;
				}
			}

			if(Gdx.input.justTouched()){

				velocity = -20;


			}

			batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
			for (int i = 0; i < numberOfTubes; i++) {
				if(tubeX[i] < - topTube.getWidth()){
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
				}else{

					tubeX[i] = tubeX[i] - tubeVelocity;


				}

				batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
				bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());

				lastIndex = i;
			}


			if(score == 1){
				batch.draw(one, Gdx.graphics.getWidth() / 2 - one.getWidth() / 2,  1400);
			}else if(score == 0){
				batch.draw(zero, Gdx.graphics.getWidth() / 2 - zero.getWidth() / 2,  1400);
			}else if(score == 2){
				batch.draw(two, Gdx.graphics.getWidth() / 2 - two.getWidth() / 2,  1400);
			}else if(score == 3){
				batch.draw(three, Gdx.graphics.getWidth() / 2 - three.getWidth() / 2,  1400);
			}else if(score == 4){
				batch.draw(four, Gdx.graphics.getWidth() / 2 - four.getWidth() / 2,  1400);
			}else if(score == 5){
				batch.draw(five, Gdx.graphics.getWidth() / 2 - five.getWidth() / 2,  1400);
			}else if(score == 6){
				batch.draw(six, Gdx.graphics.getWidth() / 2 - six.getWidth() / 2,  1400);
			}else if(score == 7){
				batch.draw(seven, Gdx.graphics.getWidth() / 2 - seven.getWidth() / 2,  1400);
			}else if(score == 8){
				batch.draw(eight, Gdx.graphics.getWidth() / 2 - eight.getWidth() / 2,  1400);
			}else if(score == 9){
				batch.draw(nine, Gdx.graphics.getWidth() / 2 - nine.getWidth() / 2,  1400);
			}
			else{
				font.draw(batch, String.valueOf(score), Gdx.graphics.getWidth() / 2, 1500);
			}

			if(birdY > 0){
				velocity = velocity + gravity;
				birdY -= velocity;
			}else{
				gameState = 2;
			}



		}else if(gameState == 0){
			batch.draw(rp, Gdx.graphics.getWidth() / 2 + rp.getWidth(), birdY + 150);
			batch.draw(title, Gdx.graphics.getWidth() / 2 - title.getWidth() / 2, birdY + 300);


			if(lastIndex < 100){
				if(lastIndex == 98){
					lastIndex = 200;
					birdX = 50;
				}else{
					birdX = birdX + 0.5f;
					lastIndex++;
				}

			}else{
				if(lastIndex == 102){
					lastIndex = 0;
					birdX = 0;
				}else{
					birdX = birdX - 0.5f;
					lastIndex --;
				}

			}


			batch.draw(bird, Gdx.graphics.getWidth() / 2 - birds[0].getWidth() / 2, birdY + birdX);
			if(Gdx.input.justTouched()) {
				gameState = 1;
			}

		}else if(gameState == 2){
			if(birdY > 7){
				birdY -= 20;
			}
			//batch.draw(topTube, tubeX[lastIndex], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[lastIndex]);
			//batch.draw(bottomTube, tubeX[lastIndex], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[lastIndex]);
			batch.draw(death, Gdx.graphics.getWidth() / 2 - birds[0].getWidth() / 2, birdY);
			batch.draw(gameOver, Gdx.graphics.getWidth() / 2 - gameOver.getWidth() / 2, Gdx.graphics.getHeight() / 2 + gameOver.getHeight() + 200);
			batch.draw(start, Gdx.graphics.getWidth() / 2 - start.getWidth() / 2, Gdx.graphics.getHeight() / 2 - 800);

			batch.draw(scoreBoard, Gdx.graphics.getWidth() / 2 - 150, 640);
			font.draw(batch, String.valueOf(score), Gdx.graphics.getWidth() / 2, 950);


			if(Gdx.input.isTouched())
			{
				tmp = new Vector3(Gdx.input.getX(),Gdx.input.getY(), 0);
				//tmp = new Vector2(Gdx.input.getX(),Gdx.input.getY());
				startRectangle = new Rectangle (Gdx.graphics.getWidth() / 2 - start.getWidth() / 2,Gdx.graphics.getHeight() / 2 - 800
						,start.getWidth(),start.getHeight());

				// texture x is the x position of the texture
				// texture y is the y position of the texture
				// texturewidth is the width of the texture (you can get it with texture.getWidth() or textureRegion.getRegionWidth() if you have a texture region
				// textureheight is the height of the texture (you can get it with texture.getHeight() or textureRegion.getRegionhHeight() if you have a texture region
				if(startRectangle.contains(tmp.x, Gdx.graphics.getHeight() - tmp.y))
				{
					// you are touching your texture
					gameState = 0;
					startGame();
					score = 0;
					scoringTube = 0;
					velocity = 0;
					birdX = 0;
					lastIndex = 0;
				}

			}


		}

		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}


		//batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
		sourceX += 5;

		batch.draw(floor,0, Gdx.graphics.getHeight() - 1850, sourceX,0, Gdx.graphics.getWidth() + 20,130);

		batch.end();

		birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);



		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

		for (int i = 0; i < numberOfTubes; i++) {
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
			//shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());

			if(Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])){
				//Gdx.app.log("Collision", "Yes!");
				gameState = 2;
			}

		}
		//shapeRenderer.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
		rp.dispose();
		title.dispose();
		zero.dispose();
		one.dispose();
		two.dispose();
		three.dispose();
		four.dispose();
		five.dispose();
		six.dispose();
		seven.dispose();
		eight.dispose();
		nine.dispose();
		scoreBoard.dispose();
		bird.dispose();
		birds[1].dispose();
		birds[2].dispose();
		gameOver.dispose();
	}
}
