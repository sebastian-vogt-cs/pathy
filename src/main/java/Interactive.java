import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Scanner;

class Interactive {

    private Renderer<Integer> ren = new Renderer<>();

    void interpretCommand() {
        System.out.println("Type your command");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        if(input.equals("read file")) {
            System.out.println("Type filename");
            String in = sc.nextLine();
            NodeHandler.handlerFromFile(in).ifPresent(handler -> {
                System.out.println(handler.toString());
                ren.render(handler);
            });
        } else if(input.length() > 5 && input.substring(0, 4).equals("mark")) {
             if (!ren.mark(input.substring(5))) {
                 System.out.println("Node not found");
             }
        } else {
            System.out.println("Command not found");
        }
    }

}
