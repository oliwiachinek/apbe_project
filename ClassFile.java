import java.io.*;

public class ClassFile {
    private int majorVersion;
    private int minorVersion;

    public static ClassFile parse(String fileName) throws IOException {
        try (DataInputStream dis = new DataInputStream(new FileInputStream(fileName))) {
            int magic = dis.readInt();
            if (magic != 0xCAFEBABE) {
                throw new IllegalArgumentException("Invalid class file format");
            }

            int minorVersion = dis.readUnsignedShort();
            int majorVersion = dis.readUnsignedShort();

            int constantPoolCount = dis.readUnsignedShort();
            for (int i = 1; i < constantPoolCount; i++) {
                int tag = dis.readUnsignedByte();
                switch (tag) {
                    case 0: // Unused entry, skip
                        break;
                    case 7:  // CONSTANT_Class
                    case 8:  // CONSTANT_String
                        dis.readUnsignedShort(); // Single index
                        break;
                    case 10: // CONSTANT_Methodref
                    case 11: // CONSTANT_InterfaceMethodref
                    case 12: // CONSTANT_NameAndType
                        dis.readUnsignedShort(); // Two indices
                        dis.readUnsignedShort();
                        break;
                    case 3:  // CONSTANT_Integer
                    case 4:  // CONSTANT_Float
                        dis.readInt(); // Skip 4 bytes
                        break;
                    case 5:  // CONSTANT_Long
                    case 6:  // CONSTANT_Double
                        dis.readLong(); // Skip 8 bytes
                        i++; // Takes up two entries
                        break;
                    case 1:  // CONSTANT_Utf8
                        int length = dis.readUnsignedShort();
                        dis.skipBytes(length); // Skip the UTF-8 string
                        break;
                    default:
                        System.err.println("Warning: Unsupported constant pool tag " + tag + ". Skipping...");
                        // Skip unknown tags (gracefully continue parsing)
                        break;
                }
            }

            return new ClassFile(majorVersion, minorVersion);
        }
    }

    public ClassFile(int majorVersion, int minorVersion) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
    }

    @Override
    public String toString() {
        return "ClassFile [majorVersion=" + majorVersion + ", minorVersion=" + minorVersion + "]";
    }
}