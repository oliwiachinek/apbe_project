import java.util.Stack;

public class BytecodeInterpreter {
    private final Stack<Integer> stack = new Stack<>();

    public void execute(byte[] bytecode) {
        int pc = 0;

        while (pc < bytecode.length) {
            int opcode = bytecode[pc] & 0xFF;
            switch (opcode) {
                case 0x60:
                    int val2 = stack.pop();
                    int val1 = stack.pop();
                    stack.push(val1 + val2);
                    break;
                case 0x10:
                    int value = bytecode[++pc];
                    stack.push(value);
                    break;
                case 0xB1:
                    System.out.println("Execution complete. Final stack: " + stack);
                    return;
                default:
                    throw new UnsupportedOperationException("Opcode " + opcode + " not implemented");
            }
            pc++;
        }
    }
}