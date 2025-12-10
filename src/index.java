public class index {
    public static void main(String[] args) {
        System.out.println("Hello from Qin!");
        System.out.println("This is the default entry point (src/index.java)");
        
        if (args.length > 0) {
            System.out.println("Arguments: " + String.join(", ", args));
        }
    }
}
