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
                    case 7:
                    case 8:
                        dis.readUnsignedShort();
                        break;
                    case 3:
                    case 4:
                        dis.readInt();
                        break;
                    case 5:
                    case 6:
                        dis.readLong();
                        i++;
                        break;
                    case 1:
                        int length = dis.readUnsignedShort();
                        dis.skipBytes(length);
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported constant pool tag: " + tag);
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