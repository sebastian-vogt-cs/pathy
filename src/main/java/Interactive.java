import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

class Interactive {

    private Renderer ren = new Renderer<>();
    private Optional<NodeHandler<Integer>> handI = Optional.empty();
    private Optional<NodeHandler<Double>> handD = Optional.empty();
    private Optional<NodeHandler<Long>> handL = Optional.empty();
    private FileSyncer syncer = new FileSyncer(Type.INTEGER);
    private boolean exit = false;

    boolean exit() {
        return exit;
    }

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
                    ren.addFileName(syncer.getFileName());
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
                if (names.length != 3) {
                    System.out.println("Please specify exactly three parameters");
                } else {
                    try {
                        try {
                            handI.ifPresent(handler -> {
                                handler.connect(names[0], names[1], Integer.parseInt(names[2]));
                                try {
                                    syncer.handlerToFile(handler);
                                } catch (IOException ex) {
                                    System.out.println("An error occurred writing to the file");
                                }
                                ren.addEdge(names[0], names[1], names[2]);
                            });
                            handD.ifPresent(handler -> {
                                handler.connect(names[0], names[1], Double.parseDouble(names[2]));
                                try {
                                    syncer.handlerToFile(handler);
                                } catch (IOException ex) {
                                    System.out.println("An error occurred writing to the file");
                                }
                                ren.addEdge(names[0], names[1], names[2]);
                            });
                            handL.ifPresent(handler -> {
                                handler.connect(names[0], names[1], Long.parseLong(names[2]));
                                try {
                                    syncer.handlerToFile(handler);
                                } catch (IOException ex) {
                                    System.out.println("An error occurred writing to the file");
                                }
                                ren.addEdge(names[0], names[1], names[2]);
                            });
                            if(! (handL.isPresent() || handI.isPresent() || handD.isPresent()) ) {
                                System.out.println("Your changes will not be saved! To save your changes create a new file or read an existing file first (type 'help' for help)");
                                ren.addEdge(names[0], names[1], names[2]);
                            }
                        } catch (NumberFormatException ex) {
                            System.out.println("Wrong data type supplied");
                        }
                    } catch (IndexOutOfBoundsException ex) {
                        System.out.println("Wrong number of argument supplied");
                    }
                }
            }
        } else if(input.length() > 3 && input.substring(0, 4).equals("exit")) {
            exit = true;
        } else if(input.length() > 9 && input.substring(0, 10).equals("stylesheet")) {
            if (input.length() == 10) {
                System.out.println("Please specify file name");
            } else {
                try {
                    ren.setStyleSheet(input.substring(11));
                } catch(Exception ex) {
                    System.out.println("File does not exist");
                }
            }
        } else if(input.length() > 3 && input.substring(0, 4).equals("new ")) {
            if(input.length() == 4) {
                System.out.println("Please specify type and file name");
            } else {
                String[] arguments = input.substring(4).split(" ");
                if (arguments.length != 2) {
                    System.out.println("Please specify exactly three parameters");
                } else {
                    if((!arguments[1].contains("config") && syncer.exists(arguments[1] + "-config")) || (arguments[1].contains("config") && syncer.exists(arguments[1]))) {
                        System.out.println("This action would overwrite an existing file, use 'unsafe-new' in order to overwrite it.'");
                    } else {
                        newC(arguments);
                    }
                }
            }
        } else if(input.length() > 10 && input.substring(0, 11).equals("unsafe-new ")) {
            if(input.length() == 11) {
                System.out.println("Please specify type and file name");
            } else {
                String[] arguments = input.substring(11).split(" ");
                if (arguments.length != 2) {
                    System.out.println("Please specify exactly three parameters");
                } else {
                    newC(arguments);
                }
            }
        } else {
            System.out.println("Command not found");
        }
    }

    private void newC(String[] arguments) {
        switch (arguments[0]) {
            case "Integer":
                if (!arguments[1].contains("config")) {
                    syncer.setFileName(arguments[1] + "-config");
                } else {
                    syncer.setFileName(arguments[1]);
                }
                handI = Optional.of(new NodeHandler<>());
                syncer.setType(Type.INTEGER);
                ren.addFileName(syncer.getFileName());
                try {
                    syncer.resetFile();
                } catch (IOException ex) {
                    System.out.println("An error occurred creating the file");
                }
                ren.renderBlank();
                break;
            case "Double":
                if (!arguments[1].contains("config")) {
                    syncer.setFileName(arguments[1] + "-config");
                } else {
                    syncer.setFileName(arguments[1]);
                }
                handD = Optional.of(new NodeHandler<>());
                syncer.setType(Type.DOUBLE);
                ren.addFileName(syncer.getFileName());
                try {
                    syncer.resetFile();
                } catch (IOException ex) {
                    System.out.println("An error occurred creating the file");
                }
                ren.renderBlank();
                break;
            case "Long":
                if (!arguments[1].contains("config")) {
                    syncer.setFileName(arguments[1] + "-config");
                } else {
                    syncer.setFileName(arguments[1]);
                }
                handL = Optional.of(new NodeHandler<>());
                syncer.setType(Type.LONG);
                ren.addFileName(syncer.getFileName());
                try {
                    syncer.resetFile();
                } catch (IOException ex) {
                    System.out.println("An error occurred creating the file");
                }
                ren.renderBlank();
                break;
            default:
                System.out.println("Wrong data type supplied");
                break;
        }

    }

}
