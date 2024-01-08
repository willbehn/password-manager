public class Main {
    public static void main(String[] args){
        String[] osInfo = System.getProperty("os.name").split(" ");
        
        if (osInfo[0].equals("Mac")){
            System.setProperty( "apple.awt.application.appearance", "system" );
        }
        
        Controller controller = new Controller();
    }
}
