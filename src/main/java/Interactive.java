import java.util.Optional;
import java.util.Scanner;

class Interactive {

    private Renderer ren = new Renderer<>();
    private Optional<NodeHandler<Integer>> handI = Optional.empty();
    private Optional<NodeHandler<Double>> handD = Optional.empty();
    private Optional<NodeHandler<Long>> handL = Optional.empty();
    private FileSyncer syncer = new FileSyncer(Type.INTEGER);

    void interpretCommand() {
        System.out.println("Type your command");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        if(input.length() > 8 && input.substring(0, 9).equals("read file")) {
            if (input.length() == 9) {
                System.out.println("Please specify file");
            } else {
                NodeHandler.handlerFromFile(input.substring(10)).ifPresent(json -> {
                    handI = json.getHandI();
                    handD = json.getHandD();
                    handL = json.getHandL();
                    handI.ifPresent(handler -> {
                        syncer.setType(Type.INTEGER);
                        System.out.println(handler.toString());
                        ren.render(handler);
                    });
                    handD.ifPresent(handler -> {
                        syncer.setType(Type.DOUBLE);
                        System.out.println(handler.toString());
                        ren.render(handler);
                    });
                    handL.ifPresent(handler -> {
                        syncer.setType(Type.LONG);
                        System.out.println(handler.toString());
                        ren.render(handler);
                    });
                    if(!input.contains("config")) {
                        syncer.setFileName(input.substring(10, input.length() - 5) + "-config");
                    } else {
                        syncer.setFileName(input.substring(10, input.length() - 5));
                    }
                });
            }
        } else if(input.length() > 3 && input.substring(0, 4).equals("mark")) {
            if (input.length() == 4) {
                System.out.println("Please specify node");
            } else if(!ren.mark(input.substring(5))) {
                System.out.println("Node not found");
            }
        } else if(input.length() > 7 && input.substring(0, 8).equals("add edge")) {
            if (input.length() == 8) {
                System.out.println("Please specify names");
            } else {
                String[] names = input.substring(9).split(" ");
                try {
                    try {
                        handI.ifPresent(handler -> {
                            handler.connect(names[0], names[1], Integer.parseInt(names[2]));
                            syncer.handlerToFile(handler);
                        });
                        handD.ifPresent(handler -> {
                            handler.connect(names[0], names[1], Double.parseDouble(names[2]));
                            syncer.handlerToFile(handler);
                        });
                        handL.ifPresent(handler -> {
                            handler.connect(names[0], names[1], Long.parseLong(names[2]));
                            syncer.handlerToFile(handler);
                        });
                        ren.addEdge(names[0], names[1], names[2]);
                    } catch (NumberFormatException ex) {
                        System.out.println("Wrong data type supplied");
                    }
                } catch(IndexOutOfBoundsException ex) {
                    System.out.println("Wrong number of argument supplied");
                }
            }
        } else {
            System.out.println("Command not found");
        }
    }

}
