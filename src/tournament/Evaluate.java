package tournament;

import agents.IAgent;
import options.LevelConfig;
import tournament.run.MarioRunResults;

public class Evaluate {
	/**
	 * How many times we should evaluate each level configuration.
	 */
	public static final int MAP_REPETITIONS = 1;
	
	private static String[] getEvaluationOptions(int runs, int seed, String levelOptions, boolean saveResults) {
		return new String[] {
				  "-s", String.valueOf(seed) // "seed"
				, "-o", levelOptions
				, "-c", Integer.toString(runs)  // level-count
				, "-r", String.valueOf(MAP_REPETITIONS)  // one-run-repetitions
				, "-i", "MarioAI"   // agent-id
				, "-d", saveResults ? "./results" : null // result-dir"	
		};
	}
	
    public static MarioRunResults evaluateLevel(
                int runs, int seed, LevelConfig level, boolean saveResults, IAgent agent) {

		System.out.println("Evaluating " + level.name());
        
        String[] options =
            getEvaluationOptions(runs, seed, level.getOptionsVisualizationOff(), saveResults);

        MarioRunResults results = EvaluateAgentConsole.evaluate(options, agent);
		return results;
	}
}
