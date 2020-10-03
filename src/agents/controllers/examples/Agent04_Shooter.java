package agents.controllers.examples;

import java.awt.Graphics;

import agents.AgentOptions;
import agents.IAgent;
import agents.controllers.MarioHijackAIBase;
import engine.MarioSimulator;
import engine.LevelScene;
import engine.VisualizationComponent;
import engine.generalization.Enemy;
import engine.input.MarioInput;
import environments.IEnvironment;
import options.FastOpts;

/**
 * Agent that sprints forward, jumps and shoots.
 * 
 * This agent has successful rate ~ 50%.
 * 
 * @author Jakub 'Jimmy' Gemrot, gemrot@gamedev.cuni.cz
 */
public class Agent04_Shooter extends MarioHijackAIBase {

	@Override
	public void reset(AgentOptions options) {
		super.reset(options);
	}

	private boolean enemyAhead() {
		return
				   e.danger(1, 0) || e.danger(1, -1) 
				|| e.danger(2, 0) || e.danger(2, -1)
				|| e.danger(3, 0) || e.danger(2, -1);
	}
	
	private boolean brickAhead() {
		return
				   t.brick(1, 0) || t.brick(1, -1) 
				|| t.brick(2, 0) || t.brick(2, -1)
				|| t.brick(3, 0) || t.brick(3, -1);
	}

	public MarioInput actionSelectionAI() {
		// ALWAYS RUN RIGHT
		control.runRight();
		
		// ALWAYS SPRINT		
		control.sprint();
		
		// ALWAYS SHOOT (if able ... max 2 fireballs at once!)
		control.shoot();
		
		// ENEMY || BRICK AHEAD => JUMP
		// WARNING: do not press JUMP if UNABLE TO JUMP!
		if (enemyAhead() || brickAhead()) control.jump();		
		
		// If in the air => keep JUMPing
		if (!mario.onGround) control.jump();
		
		return action;
	}
	
	@Override
	public void debugDraw(VisualizationComponent vis, LevelScene level, IEnvironment env, Graphics g) {
		super.debugDraw(vis, level, env, g);
		if (mario == null) return;
		String debug = "";
		if (enemyAhead()) debug += "|ENEMY AHEAD|";
		else debug += "|-----------|";
		if (brickAhead()) debug += "|BRICK AHEAD|";
		else debug += "|-----------|";
		VisualizationComponent.drawStringDropShadow(g, debug, 0, 26, 1);
	}
	
	public static void main(String[] args) {
		String options = FastOpts.VIS_ON_2X + FastOpts.LEVEL_02_JUMPING + FastOpts.L_ENEMY(Enemy.GOOMBA, Enemy.SPIKY) + FastOpts.L_TUBES_ON + FastOpts.L_RANDOMIZE;
		
		MarioSimulator simulator = new MarioSimulator(options);
		
		IAgent agent = new Agent04_Shooter();
		
		simulator.run(agent);
		
		System.exit(0);
	}
}