import java.io.FileNotFoundException;
import java.util.Scanner;

class Interactive {

    static void start(){
        System.out.println("Type your filename");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        try {
            NodeHandler handler = Json.readJson(input);
            System.out.println(handler.toString());
        } catch(ClassCastException ex) {
            System.out.println("Error: Wrong data type supplied");
        } catch(NumberFormatException ex) {
            System.out.println("Error: Number too large");
        } catch(FileNotFoundException ex) {
            System.out.println("file not found");
        } catch (Exception ex) {
            System.out.println("Exception in json parser: " + ex.getMessage() + "; " + ex.toString());
        }
    }

}
