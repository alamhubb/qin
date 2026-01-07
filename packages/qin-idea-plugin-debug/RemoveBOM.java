import java.nio.file.*;

public class RemoveBOM {
    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.err.println("Usage: java RemoveBOM <file>");
            System.exit(1);
        }
        Path file = Paths.get(args[0]);
        byte[] bytes = Files.readAllBytes(file);
        if (bytes.length >= 3 && bytes[0] == (byte) 0xEF && bytes[1] == (byte) 0xBB && bytes[2] == (byte) 0xBF) {
            byte[] newBytes = new byte[bytes.length - 3];
            System.arraycopy(bytes, 3, newBytes, 0, newBytes.length);
            Files.write(file, newBytes);
            System.out.println("BOM removed from " + file);
        } else {
            System.out.println("No BOM found in " + file);
        }
    }
}
