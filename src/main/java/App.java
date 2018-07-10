public class App {
    public static void main(String[] args) {
        new App().mainLoop();
    }

    private Interactive interactive = new Interactive();

    private void mainLoop() {
        while(true) {
            interactive.interpretCommand();
        }
    }

}
