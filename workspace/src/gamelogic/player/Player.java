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
	// private boolean devtool = false;
	// private int maxDashes = 1;
	// private boolean isDashing = false;
	// private boolean upKeyDown = false;
	// private int dashCounter = 0;
	// private float dashTimer = 0;
	// private float dashTimedFor = 0;
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
		// ##2
// precondition, waterEffect must be true- it's a trigger
//postcondition- specified 2-3 lines below
		if(!waterEffect){ // waterEffect affected movement means that-this part is for when watereffect isn't on
			//you can't jump and the speed is constant/stabilised
		//	Debug: System.out.println("here");
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
			//##4
			// precondition: you must be in the air for effects to show
			// postcondition: will forcefully make you go down
			if(PlayerInput.isDownKeyDown()){
				movementVector.y = jumpPower;
			}
			//##6 - scrapped
			// precondition: player exists, endpoint exists, and upkey is pressed (or w, which is w, and win, idk)
			// postcondition: will allow the player to fly wherever, and will turn of when the up button is pressed again
//  if (PlayerInput.isUpKeyDown()){
// 	devtool = true;
//  }
//  if (devtool == true){
// 				if(PlayerInput.isUpKeyDown()){
// 				devtool = false;
// 			}
// 			else if(PlayerInput.isLeftKeyDown()){
// 				Player.col--;
// 			}
// 			else if(PlayerInput.isRightKeyDown()){
// 				Player.col++;
// 			}
// 			else if(PlayerInput.isJumpKeyDown()){
// 				Player.row++;
// 			}
// 			else if(PlayerInput.isJumpKeyDown()){
// 				Player.row--;
// 			}
//  }
			//##5 - scrapped because i couldn't get it to work and I'm running out of time
			// precondition: the up, or dash button must be pressedn whole in teh air
			// postcondition: will force the walkspeed to speed up temporarily in a short burst
// if(PlayerInput.isUpKeyDown() && isDashing == true && standingOnGround() == false) {
			
// 			upKeyDown = true;
// 			if 
// 			if(walkSpeed>0){
// 			walkSpeed = 1500;
// 			dashCounter++;
// 			dashTimer = System.currentTimeMillis();
// 			} else if (walkSpeed<0){
// 			walkSpeed = -1500;
// 			dashCounter++;
// 			dashTimer = System.currentTimeMillis();
// 			}

		
// 		if (dashTimer> 1500){
// 			isDashing = false;
// 		}
// 		if(!PlayerInput.isUpKeyDown()){
// 			upKeyDown = false;
// 		}
// 	}
			// System.out.println( walkSpeed + " " + keyHeldTime);

			//##1
			//Precondition: player must already be jumping and
		    // the jump jet must be pressed while that happens- 
			//and must be released before each consecutive jump-
			// and cannot happen while touching water
			//PostConidtion:
			//allows for a triple jump- I'm not making it a double jump 
			//because I love the triple jump
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
// watereffect is turned off to prevent it going endlessly
public void turnOffWaterEffect(){
	waterEffect = false;


}
}