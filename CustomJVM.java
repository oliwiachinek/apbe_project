import java.io.IOException;

public class CustomJVM {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java CustomJVM <class file>");
            return;
        }

        String classFileName = args[0];

        try {
            ClassFile classFile = ClassFile.parse(classFileName);
            System.out.println("Parsed Class File: " + classFile);

            System.out.println("\nRunning hardcoded bytecode demonstration...");
            byte[] bytecode = {
                    0x10, 0x05,
                    0x10, 0x04,
                    0x60,
                    (byte) 0xB1
            };

            BytecodeInterpreter interpreter = new BytecodeInterpreter();
            interpreter.execute(bytecode);
        } catch (IOException e) {
            System.out.println("Error reading class file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid class file: " + e.getMessage());
        }
    }
}