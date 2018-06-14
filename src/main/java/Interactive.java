import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Scanner;

class Interactive {

    private Renderer ren = new Renderer<>();
    private NodeHandler handler = null;

    void interpretCommand() {
        System.out.println("Type your command");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        if(input.length() > 8 && input.substring(0, 9).equals("read file")) {
            if (input.length() == 9) {
                System.out.println("Please specify file");
            } else {
                NodeHandler.handlerFromFile(input.substring(10)).ifPresent(handler -> {
                    System.out.println(handler.toString());
                    ren.render(handler);
                    this.handler = handler;
                });
            }
        } else if(input.length() > 3 && input.substring(0, 4).equals("mark")) {
            if (input.length() == 4) {
                System.out.println("Please specify node");
            } else if (!ren.mark(input.substring(5))) {
                System.out.println("Node not found");
            }
        } else if(input.length() > 7 && input.substring(0, 8).equals("add node")) {
            if (input.length() == 8) {
                System.out.println("Please specify name");
            } else {
                ren.addNode(input.substring(9));
            }
        }else if(input.length() > 7 && input.substring(0, 8).equals("add edge")) {
            if (input.length() == 8) {
                System.out.println("Please specify names");
            } else {
                String[] names = input.substring(9).split(" ");
                try {
                    ren.addEdge(names[0], names[1], names[2]);
                } catch(IndexOutOfBoundsException ex) {
                    System.out.println("Wrong number of argument supplied");
                }
            }
        } else {
            System.out.println("Command not found");
        }
    }

    NodeHandler getHandler() {
        return handler;
    }

}
