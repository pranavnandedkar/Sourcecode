/*
 Computer System Design Project 1
 Code written and implemented by:
 Manjari Deshpande [student ID 23576343] and
 Neha upadhyay [student ID 23597397]
 */
package sourceCode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class MainFile {
	int[] M = new int[31];// array to store memory allocations
	int[] Regs = new int[5];// array to store values in registers
	int i = 0, j = 0, totalInstructions = 14;
	int Result, RtResult, ImmResult, RsResult, RdResult, shamtResult, counter, execute, loopStart, gotoLineNum;
	String OpCode, Rs, Rt, funct, Imm, Rd, shamt;

	public static void main(String[] args) {
		MainFile Obj = new MainFile();
		// to call non static members/functions from static main
		String Filepath = new File("Instructions.txt").getAbsolutePath();
		// gets the file path

		try {
			BufferedReader br = new BufferedReader(new FileReader(Filepath));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();// fetches the next line

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				Obj.InstructionFetch(line);// fetches the current instruction
				Obj.InstructionDecode(line);// decodes the current execution
				Obj.InstructionExecute();// executes teh current execution
				line = br.readLine();// fetches the next line
			}
			Obj.WriteBack();
			String everything = sb.toString();
			br.close();// closes the opened fine reader
		} catch (Exception e) {
			System.out.println("Error" + e);// catches any exception
		} finally {

		}
		try {
			System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// ---------------------------------------------------------------------------------------------
	public void InstructionFetch(String IR) {
		System.out.println("Fetching the instruction: " + IR);
		// prints the current instruction in binary format
	}

	// ---------------------------------------------------------------------------------------------

	public void InstructionDecode(String IR) {
		OpCode = IR.substring(0, 6);
		Rs = IR.substring(6, 11);
		Rt = IR.substring(11, 16);
		Result = Integer.parseInt(OpCode, 2);
		RtResult = Integer.parseInt(Rt, 2);
		if (Result != 0)// I or J format
		{
			if (RtResult != 0)// I format
			{
				Imm = IR.substring(16, 32);
				// int Temp=Integer.parseInt(Imm.substring(0, 1));
				RsResult = Integer.parseInt(Rs, 2);
				int decimalValue;
				if (Imm.charAt(0) == '1') {
					// Call our invert digits method
					String invertedInt = invertDigits(Imm);
					// Change this to decimal format.
					ImmResult = Integer.parseInt(invertedInt, 2);
					// Add 1 to the curernt decimal and multiply it by -1
					// because we know it's a negative number
					ImmResult = (ImmResult + 1) * -1;
					// return the final result
				} else {
					// Else we know it's a positive number, so just convert
					// the number to decimal base.
					ImmResult = Integer.parseInt(Imm, 2);
				}
				switch (Result)// supplying the opcode
				{
				case 8:// addi
					System.out.println("addi " + Rt + "," + Rs + " " + Imm);
					System.out.println("addi R" + RtResult + ",R" + RsResult + ", " + ImmResult);
					break;

				case 0:
					System.out.println("Add R" + RsResult + ",R" + RsResult + ", R" + RtResult);

				case 35:// lw
					System.out.println("Load " + Rt + "," + Rs + " " + Imm);
					// ImmResult = ImmResult;
					System.out.println("Load R" + RtResult + "," + Regs[RsResult] + "(R" + RsResult + ")");
					break;

				case 43:// sw
					System.out.println("Store " + Rt + "," + Rs + " " + Imm);
					break;

				case 5:// bne
					gotoLineNum = ImmResult;
					System.out.println("branch when unequal " + Rs + "," + Rt + " " + Imm);
					execute = execute + 1;
					break;

				default:
					System.out.println("Error Fetching instruction");

				}
			} else// it is J format
			{
				RsResult = Integer.parseInt(Rs, 2);
				Imm = IR.substring(6, 32);
			}
		} else// R format
		{
			RsResult = Integer.parseInt(Rs, 2);
			Rd = IR.substring(16, 21);
			RdResult = Integer.parseInt(Rd, 2);
			shamt = IR.substring(21, 26);
			shamtResult = Integer.parseInt(shamt, 2);
			Imm = IR.substring(26, 32);
			ImmResult = Integer.parseInt(Imm, 2);
			System.out.println("add " + Rd + "," + Rt + " " + Rs);
			System.out.println("add R" + RdResult + ",R" + RtResult + ", R" + RsResult);

		}
	}

	// ---------------------------------------------------------------------------------------------
	public void InstructionExecute() {
		switch (Result) {

		case 8:// addi

			System.out.println("\n R[" + RtResult + "]= " + ImmResult);
			if (RtResult == RsResult) {
				Regs[RtResult] = Regs[RtResult] + ImmResult;
			} else {
				Regs[RtResult] = RsResult + ImmResult;
			}
			System.out.println("--------***************-------------\n");
			break;

		case 0:// add
			System.out.println("R" + RtResult + "=" + Regs[RtResult]);
			Regs[RtResult] = Regs[RtResult] + Regs[RsResult];
			System.out.println("R" + RsResult + "=" + Regs[RsResult]);
			System.out.println("R" + RtResult + "=" + Regs[RtResult]);
			System.out.println("--------***************-------------\n");
			break;

		case 35:// lw
			Regs[RtResult] = M[Regs[RsResult]];
			System.out.println("\n R[" + RtResult + "]= " + M[Regs[RsResult]]);
			System.out.println("\n M[" + Regs[RsResult] + "]= " + M[Regs[RsResult]]);
			System.out.println("--------***************-------------\n");
			i = i + ImmResult;
			break;

		case 43:// sw
			System.out.println("\n R[" + RtResult + "]= " + Regs[RtResult]);
			i = ImmResult + Regs[RsResult];
			M[i] = Regs[RtResult];
			System.out.println("Store R" + RtResult + "," + i + "(R" + RsResult + ")");
			System.out.println("\n M[" + i + "]= " + M[i]);

			System.out.println("--------***************-------------\n");
			break;

		case 5:// bne
			System.out.println("branch when unequal R" + RsResult + ",R" + RtResult + " " + ImmResult);
			System.out.println("--------***************-------------\n");
			if (execute < 3) {
				MemoryAccess();
			}
			break;

		default:
			System.out.println("Error reading file");

		}
	}

	// ---------------------------------------------------------------------------------------------
	public void MemoryAccess() {
		// setting the current counter
		loopStart = gotoLineNum + totalInstructions;// setting the value to the
													// start of the loop
		for (i = loopStart; i <= totalInstructions; i++)// re executing
														// instructions
														// 10,11,12,13 and 14
		{

			BufferedReader br;
			try {
				String Filepath = new File("Instructions.txt").getAbsolutePath();// gets
																					// the
																					// file
																					// path
				br = new BufferedReader(new FileReader(Filepath));
				StringBuilder sb = new StringBuilder();
				String line;
				// line = br.readLine();
				counter = 0;
				while ((line = br.readLine()) != null) {
					if (counter >= loopStart) {
						sb.append(line);
						sb.append(System.lineSeparator());
						InstructionFetch(line);
						InstructionDecode(line);
						InstructionExecute();
						// line = br.readLine();
					}
					counter = counter + 1;
				}

			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// ---------------------------------------------------------------------------------------------
	public void WriteBack() {
		int size_Memory = M.length;
		int size_Register = Regs.length;
		for (int m = 0; m < size_Memory; m++) {
			System.out.println("M[" + m + "]=" + M[m]);
			m = m + 3;
		}
		System.out.println("--------***************-------------\n");
		for (int r = 0; r < size_Register; r++) {
			System.out.println("R[" + r + "]=" + Regs[r]);
		}
	}

	// ---------------------------------------------------------------------------------------------
	public String invertDigits(String binaryInt) {
		String result = binaryInt;
		result = result.replace("0", " "); // temp replace 0s
		result = result.replace("1", "0"); // replace 1s with 0s
		result = result.replace(" ", "1"); // put the 1s back in
		return result;
	}

}