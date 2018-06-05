import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.Scanner;

class Interactive {

    static Optional<NodeHandler> handlerFromFile(){
        System.out.println("Type your filename");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        try {
            return Optional.of(Json.readJson(input));
        } catch(ClassCastException ex) {
            System.out.println("Error: Wrong data type supplied");
            return Optional.empty();
        } catch(NumberFormatException ex) {
            System.out.println("Error: Number too large");
            return Optional.empty();
        } catch(FileNotFoundException ex) {
            System.out.println("file not found");
            return Optional.empty();
        } catch (Exception ex) {
            System.out.println("Exception in json parser: " + ex.getMessage() + "; " + ex.toString());
            return Optional.empty();
        }
    }

}
