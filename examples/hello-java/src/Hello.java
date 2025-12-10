public class Hello {
    public static void main(String[] args) {
        System.out.println("Hello from Qin managed project!");
        
        if (args.length > 0) {
            System.out.println("Arguments: " + String.join(", ", args));
        }
    }
    
    public static int add(int a, int b) {
        return a + b;
    }
    
    public static String greet(String name) {
        return "Hello, " + name + "!";
    }
}
