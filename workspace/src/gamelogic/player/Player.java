package gamelogic.player;

import java.awt.Color;
import java.awt.Graphics;

import gameengine.PhysicsObject;
import gameengine.graphics.MyGraphics;
import gameengine.hitbox.RectHitbox;
import gamelogic.Main;
import gamelogic.level.Level;
import gamelogic.tiles.Tile;

public class Player extends PhysicsObject{
	public float walkSpeed = 400;
	public float jumpPower = 1350;
	private long keyHeldTime;
	private boolean leftKeyDown = false;
	private boolean rightKeyDown = false;
	private boolean isJumping = false;
	private int jumpCount = 0;
	private boolean jumpKeyPressed = false;
	private int maxJumps = 2;
	private boolean waterEffect = false;
	public Player(float x, float y, Level level) {
	
		super(x, y, level.getLevelData().getTileSize(), level.getLevelData().getTileSize(), level);
		int offset =(int)(level.getLevelData().getTileSize()*0.1); //hitbox is offset by 10% of the player size.
		this.hitbox = new RectHitbox(this, offset,offset, width -offset, height - offset);
	}

	@Override
	public void update(float tslf) {
		super.update(tslf);
		//normal movement
		movementVector.x = 0;

		if(!waterEffect){
		//	System.out.println("here");
			if(PlayerInput.isLeftKeyDown()){
				if(!leftKeyDown){
					leftKeyDown = true;
					keyHeldTime = System.currentTimeMillis();
				}
			walkSpeed -= ((double)System.currentTimeMillis()-keyHeldTime)/2000*1000;
			if(walkSpeed<-880){
				walkSpeed = -880;
			}
			}
			if(PlayerInput.isRightKeyDown()){
				if(!rightKeyDown){
					rightKeyDown = true;
					keyHeldTime = System.currentTimeMillis();
				}
			walkSpeed += ((double)System.currentTimeMillis()-keyHeldTime)/2000*1000;
			if(walkSpeed>880){
				walkSpeed = 880;
			}
			}
			
			
			if(!PlayerInput.isRightKeyDown()){
				rightKeyDown= false;
				keyHeldTime=0;
			}
			if(!PlayerInput.isLeftKeyDown()){
				leftKeyDown = false;
				keyHeldTime = 0;
			}
			if(!PlayerInput.isLeftKeyDown() && !PlayerInput.isRightKeyDown()){
				walkSpeed = 0;
			}
			System.out.println( walkSpeed + " " + keyHeldTime);

			//##1
			//allows for a triple jump- I'm not making it a double jump because I love the truple jump
		if(PlayerInput.isJumpKeyDown() && !jumpKeyPressed) {
			
			jumpKeyPressed = true;
			if(!isJumping){
			movementVector.y = -jumpPower;
			isJumping = true;
			jumpCount++;
			}
			else if(jumpCount<maxJumps){
				movementVector.y = -jumpPower;
				jumpCount++;
			}
		}
		if(!PlayerInput.isJumpKeyDown()){
			jumpKeyPressed = false;
		}

		// if(PlayerInput.isJumpKeyDown() && isJumping && jumpCount%2 == 1) {
		// 	jumpCount++;
		// }
		
			
	}

	//water movement
	else{
		if (PlayerInput.isRightKeyDown()){
			walkSpeed = 500;
		}
		if (PlayerInput.isLeftKeyDown()){
			walkSpeed = -500;
		}
	}
	//actualy move left/right
	movementVector.x += walkSpeed;
		isJumping = true;
		if(collisionMatrix[BOT] != null) {isJumping = false; jumpCount =0;}
	}


	public boolean standingOnGround(){
		if(collisionMatrix[BOT]!=null){
			return true;
		}
		else{
			return false;
		}
	}
	@Override
	public void draw(Graphics g) {
		g.setColor(Color.YELLOW);
		MyGraphics.fillRectWithOutline(g, (int)getX(), (int)getY(), width, height);
		
		if(Main.DEBUGGING) {
			for (int i = 0; i < closestMatrix.length; i++) {
				Tile t = closestMatrix[i];
				if(t != null) {
					g.setColor(Color.RED);
					g.drawRect((int)t.getX(), (int)t.getY(), t.getSize(), t.getSize());
				}
			}
		}
		
		hitbox.draw(g);
	}
//##2
public void waterEffects(){
	waterEffect = true;
// jumpKeyPressed = false;
// if((!PlayerInput.isRightKeyDown() || !PlayerInput.isLeftKeyDown())) {
// walkSpeed = 0;	

// }
// if (PlayerInput.isRightKeyDown()){
// 	walkSpeed = 500;
// }
// if (PlayerInput.isLeftKeyDown()){
// 	walkSpeed = -500;
// }
// }
// 	public boolean getJumpKeyPressed() {
// 		return jumpKeyPressed;
// 	}
}

public void turnOffWaterEffect(){
	waterEffect = false;


}
}