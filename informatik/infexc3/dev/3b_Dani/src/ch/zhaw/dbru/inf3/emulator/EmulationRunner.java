/**
 * 
 */
package ch.zhaw.dbru.inf3.emulator;

/**
 * @author Daniel Brun
 *
 */
public class EmulationRunner {

	private CPU cpu;
	private Memory memory;
	
	private AssemblerCompiler compiler;
	
	/**
	 * Creates a new EmulationRunner
	 */
	public EmulationRunner() {
		compiler = new AssemblerCompiler();
		
		memory = new Memory();
		cpu = new CPU(16,3,memory);
	}
	
	public void execute(){
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new EmulationRunner().execute();
	}

}
