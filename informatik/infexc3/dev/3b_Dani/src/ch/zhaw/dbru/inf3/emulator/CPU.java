/**
 * 
 */
package ch.zhaw.dbru.inf3.emulator;

/**
 * @author Daniel Brun
 *
 */
public class CPU implements Runnable{

	private Memory memory;
	private byte[][] registers;
	private byte[] bfz;
	private byte[] bfr;
	//Carry-Flag
	
	/**
	 * 
	 */
	public CPU(int aWordWidth, int aRegisterCount, Memory aMemory) {
		
		registers = new byte[aRegisterCount + 1][];
		bfz = new byte[aWordWidth];
		
		//2er Komplement, 16 Bit, Msb und MSB ganz links
		//Zykluszeit 200ns
		//CPI: 1
		for(int x = 0; x  < registers.length;x++){
			registers
		}
	}

	public void setInitialAddress(byte[] anAdress){
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}
