import static java.lang.System.out;

import agents.IAgent;
import agents.controllers.keyboard.CheaterKeyboardAgent;
import engine.MarioSimulator;
import options.LevelConfig;
import tools.EvaluationInfo;
import tournament.Evaluate;

public class Mario {
    static void usage() {
        out.println("usage: mario [<agent-classname>] [<option>...]");
        out.println("options:");
        out.println("  -level <level-number>[-<to-level-number>] : level(s) to run or simulate");
        out.println("  -seed <num> : random seed");
        out.println("  -sim <count> : simulate a series of games without visualization");
        out.println("  -v : verbose");
        System.exit(1);
    }

	public static void main(String[] args) throws Exception {
		IAgent agent = null;
        int fromLevel = 6, toLevel = 0;
        int seed = 0;
        boolean seedSpecified = false;
        int sim = 0;
        boolean verbose = false;

        for (int i = 0 ; i < args.length ; ++i) {
            String s = args[i];
            switch (s) {
                case "-level":
                    String[] a = args[++i].split("-");
                    fromLevel = Integer.parseInt(a[0]);
                    toLevel = a.length > 1 ? Integer.parseInt(a[1]) : fromLevel;
                    break;
                case "-seed":
                    seed = Integer.parseInt(args[++i]);
                    seedSpecified = true;
                    break;
                case "-sim":
                    sim = Integer.parseInt(args[++i]);;
                    break;
                case "-v":
                    verbose = true;
                    break;
                default:
                    if (s.startsWith("-"))
                        usage();
                    agent = (IAgent) Class.forName(s).getConstructor().newInstance();
            }
        }

        if (sim > 0) {  // simulate a series of games
            if (agent == null) {
                System.out.println("must specify agent with -sim");
                return;
            }
            if (!seedSpecified)
                seed = 0;
            Evaluate.evaluateLevels(sim, seed, fromLevel, toLevel, false, agent, verbose);
        } else {  // play one game visually
            if (toLevel > fromLevel) {
                System.out.println("level range only works with -sim");
                return;
            }

            if (agent == null)
                agent = new CheaterKeyboardAgent();

            LevelConfig level = LevelConfig.values()[fromLevel];
            System.out.println("Running " + level.name());

            if (!seedSpecified)
                seed = -1;
            EvaluationInfo info = MarioSimulator.run(agent, level, seed);
            
            switch (info.getResult()) {
                case LEVEL_TIMEDOUT:
                    out.println("The level timed out!");
                    break;
                    
                case SIMULATION_RUNNING:
                    out.println("Simulation still running?");
                    throw new RuntimeException("Invalid evaluation info state, simulation should not be running.");
                    
                case VICTORY:
                    out.println("Victory!!!");
                    break;

                default:
            }
        }
            
        System.exit(0);
	}
}
