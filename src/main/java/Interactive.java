import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import static org.fusesource.jansi.Ansi.*;

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
        printText("Type your command");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        if(input.length() > 8 && input.substring(0, 9).equals("read file")) {
            if (input.length() == 9) {
                printFailure("Please specify file");
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
                        syncer.setFileName(input.substring(10, input.length()) + "-config");
                    } else {
                        syncer.setFileName(input.substring(10, input.length()));
                    }
                    ren.addFileName(syncer.getFileName());
                    printSuccess("File read");
                });
            }
        } else if(input.length() > 4 && input.substring(0, 5).equals("mark ")) {
            if (input.length() == 5) {
                printFailure("Please specify node");
            } else if(!ren.mark(input.substring(5))) {
                printFailure("Node not found");
            } else {
                printSuccess("Node marked");
            }
        } else if(input.length() > 8 && input.substring(0, 9).equals("add edge ")) {
            if (input.length() == 9) {
                printFailure("Please specify names");
            } else {
                String[] names = input.substring(9).split(" ");
                if (names.length != 3) {
                    printFailure("Please specify exactly three parameters");
                } else {
                    try {
                        try {
                            handI.ifPresent(handler -> {
                                handler.connect(names[0], names[1], Integer.parseInt(names[2]));
                                try {
                                    syncer.handlerToFile(handler);
                                    printSuccess("Edge added");
                                } catch (IOException ex) {
                                    printFailure("An error occurred writing to the file. Your changes won't be saved after this session");
                                }
                                ren.addEdge(names[0], names[1], names[2]);
                            });
                            handD.ifPresent(handler -> {
                                handler.connect(names[0], names[1], Double.parseDouble(names[2]));
                                try {
                                    syncer.handlerToFile(handler);
                                    printSuccess("Edge added");
                                } catch (IOException ex) {
                                    printFailure("An error occurred writing to the file. Your changes won't be saved after this session");
                                }
                                ren.addEdge(names[0], names[1], names[2]);
                            });
                            handL.ifPresent(handler -> {
                                handler.connect(names[0], names[1], Long.parseLong(names[2]));
                                try {
                                    syncer.handlerToFile(handler);
                                    printSuccess("Edge added");
                                } catch (IOException ex) {
                                    printFailure("An error occurred writing to the file. Your changes won't be saved after this session");
                                }
                                ren.addEdge(names[0], names[1], names[2]);
                            });
                            if(! (handL.isPresent() || handI.isPresent() || handD.isPresent()) ) {
                                printFailure("Your changes will not be saved! To save your changes create a new file or read an existing file first (type 'help' for help)");
                                ren.addEdge(names[0], names[1], names[2]);
                            }
                        } catch (NumberFormatException ex) {
                            printFailure("Wrong data type supplied");
                        }
                    } catch (IndexOutOfBoundsException ex) {
                        printFailure("Wrong number of argument supplied");
                    }
                }
            }
        } else if(input.length() > 10 && input.substring(0, 11).equals("stylesheet ")) {
            if (input.length() == 11) {
                printFailure("Please specify file name");
            } else {
                try {
                    ren.setStyleSheet(input.substring(11));
                    printSuccess("Stylesheet applied");
                } catch(Exception ex) {
                    printFailure("File does not exist");
                }
            }
        } else if(input.length() > 3 && input.substring(0, 4).equals("new ")) {
            if(input.length() == 4) {
                printFailure("Please specify type and file name");
            } else {
                String[] arguments = input.substring(4).split(" ");
                if (arguments.length != 2) {
                    printFailure("Please specify exactly three parameters");
                } else {
                    if((!arguments[1].contains("config") && syncer.exists(arguments[1] + "-config")) || (arguments[1].contains("config") && syncer.exists(arguments[1]))) {
                        printFailure("This action would overwrite an existing file, use 'unsafe-new' in order to overwrite it.'");
                    } else {
                        newC(arguments);
                    }
                }
            }
        } else if(input.length() > 10 && input.substring(0, 11).equals("unsafe-new ")) {
            if(input.length() == 11) {
                printFailure("Please specify type and file name");
            } else {
                String[] arguments = input.substring(11).split(" ");
                if (arguments.length != 2) {
                    printFailure("Please specify exactly two parameters");
                } else {
                    newC(arguments);
                }
            }
        } else if(input.length() > 4 && input.substring(0, 5).equals("path ")) {
            if(input.length() == 5) {
                printFailure("Please specify start and end node");
            } else {
                String[] arguments = input.substring(5).split(" ");
                if (arguments.length != 2) {
                    printFailure("Please specify exactly two parameters");
                } else {
                    try {
                        handI.ifPresent(handler -> {
                            ArrayList<Node<Integer>> list = handler.getAlgorithm().run(handler.getNodeByName(arguments[0]), handler.getNodeByName(arguments[1]));
                            printPath(list);
                            markPath(list);
                        });
                        handD.ifPresent(handler -> {
                            ArrayList<Node<Double>> list = handler.getAlgorithm().run(handler.getNodeByName(arguments[0]), handler.getNodeByName(arguments[1]));
                            printPath(list);
                            markPath(list);
                        });
                        handL.ifPresent(handler -> {
                            ArrayList<Node<Long>> list = handler.getAlgorithm().run(handler.getNodeByName(arguments[0]), handler.getNodeByName(arguments[1]));
                            printPath(list);
                            markPath(list);
                        });
                    } catch (NoSuchElementException e) {
                        printFailure("Node not found");
                    }
                }
            }
        } else if(input.length() > 3 && input.substring(0, 4).equals("help")) {
            printText(
                    "List of all commands:\n"
                    + "* with 'read file [filename]' you can read a pathy config-file or a self defined .json (for reference see GitHub repo)\n"
                    + "* with 'mark [node]' you can visually mark a node in the graph\n"
                    + "* with 'add edge [node1] [node2] [length]' you can add an edge. It will be rendered and written to a config file if possible\n"
                    + "* with 'stylesheet [filename]' you can add a custom stylesheet for the renderer. For reference see GitHub repo\n"
                    + "* with 'new [Type] [filename]' you can initiate a new project with the given type and filename\n"
                    + "* 'unsafe-new' works like 'new', but it overwrites existing files\n"
                    + "* with 'path [node1] [node2]' you can get the shortest path between two nodes\n"
                    + "* with 'help' you can print a list of all commands, but you figured that out already"

            );
        } else {
            printFailure("Command not found");
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
                    printSuccess("New project was created");
                } catch (IOException ex) {
                    printFailure("An error occurred creating the project");
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
                    printSuccess("New project was created");
                } catch (IOException ex) {
                    printFailure("An error occurred creating the project");
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
                    printSuccess("New project was created");
                } catch (IOException ex) {
                    printFailure("An error occurred creating the project");
                }
                ren.renderBlank();
                break;
            default:
                printFailure("Wrong data type supplied");
                break;
        }

    }

    static void printText(String output) {
        System.out.println( ansi().eraseScreen().render("@|blue > " + output + "|@") );
    }

    static void printSuccess(String output) {
        System.out.println( ansi().eraseScreen().render("@|green >> " + output + "|@") );
    }

    static void printFailure(String output) {
        System.out.println( ansi().eraseScreen().render("@|red >> " + output + "|@") );
    }

    private <T extends Number> void printPath(ArrayList<Node<T>> path) {
        if (path != null) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < path.size(); i++) {
                builder.append(path.get(i).getName());
                if (i < path.size() - 1) {
                    builder.append(" -> ");
                }
            }
            printSuccess(builder.toString());

            T distance = path.get(path.size() - 1).getDistance().get();
            printSuccess("Distance: " + distance);
        } else {
            printSuccess("No path found");
        }
    }

    private <T extends Number> void markPath(ArrayList<Node<T>> path) {
        if (path == null) {
            return;
        }

        for (int i = 0; i < path.size(); i++) {
            if(i > 0) {
                ren.markEdge(path.get(i).getName() + path.get(i - 1).getName());
                ren.markEdge(path.get(i - 1).getName() + path.get(i).getName());
            }
        }
    }

}
