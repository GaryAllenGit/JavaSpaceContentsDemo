import net.jini.space.JavaSpace05;
import net.jini.space.MatchSet;

import java.util.ArrayList;
import java.util.Collection;

public class ContentsDemo {

    /* A demo to show the JavaSpace05 "contents" and "take" operations.
     "contents" reads a collection of objects from the space all at the
     same time, while "take" takes (removes and returns) a similar collection.
     Each method is passed a collection of templates, and returns
     all objects that match any of these templates, but note the different
     return types of the two operations. */

    private final static int FIVE_SECONDS = 1000 * 5; // that's 5000 Milliseconds
    private final static int NUMBER_OF_OBJECTS_TO_RETURN = 100;

    public static void main(String[] args) {

        // Get a reference to the space, and cast it to a JavaSpace05
        JavaSpace05 space = (JavaSpace05) SpaceUtils.getSpace();
        if (space == null) {
            System.err.println("JavaSpace not found.");
            System.exit(1);
        }

        // write some objects to the space
        // for this demo we write 10 "SObj" objects
        for (int i = 0; i < 10; i++) {
            Sobj s = new Sobj("Object " + i);
            try {
                space.write(s, null, FIVE_SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Now get ready to read them all back at once

        // Both "contents" and "take" require a list of templates
        // For this demo we will set up the Collection of templates, but add
        //     just one template to that list
        Collection<Sobj> templates = new ArrayList<>();

        Sobj template = new Sobj();
        templates.add(template);


        // Now use "contents" to read the matching objects.
        // The parameters to the "contents" command are:
        // the collection of templates, transaction, timeout, maximum number
        //     of objects to retrieve
        // The method returns a "MatchSet" of Entry objects
        try {
            MatchSet results = space.contents(templates, null, FIVE_SECONDS, NUMBER_OF_OBJECTS_TO_RETURN);

            // take the objects from the MatchSet one by one and print them
            System.out.println("Printing the results of the 'contents' method call");

            Sobj result = (Sobj)results.next();
            while (result != null){
                System.out.println("\t" + result.contents);
                result = (Sobj)results.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Now use the "take" command to take the matching objects
        // The parameters to the take command are:
        // the collection of templates, transaction, timeout, maximum number
        //     of objects to retrieve
        // The method returns a Collection of objects
        try {
            Collection<?> results = space.take(templates, null, FIVE_SECONDS, NUMBER_OF_OBJECTS_TO_RETURN);

            // print out the results
            System.out.println("\n\nPrinting the results of the 'take' method call");

            for (Object result : results) {
                Sobj s = (Sobj) result;
                System.out.println("\t" + s.contents);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // That's it, so close down
        System.exit(0);
    }
}
