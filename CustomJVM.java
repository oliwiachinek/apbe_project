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
            System.out.println(classFile);

            byte[] bytecode = {
                    0x10, 0x05,
                    0x10, 0x03,
                    0x60,
                    (byte) 0xB1
            };

            BytecodeInterpreter interpreter = new BytecodeInterpreter();
            interpreter.execute(bytecode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}