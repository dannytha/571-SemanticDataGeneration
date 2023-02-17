package SemanticRDF;
import org.apache.jena.rdf.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class jenaTest {
    // some definitions
    static String personURI    = "http://somewhere/JohnSmith";
    static String fullName     = "John Smith";

    // create an empty Model
    Model model = ModelFactory.createDefaultModel();

    // create the resource
    Resource johnSmith = model.createResource(personURI);

    // add the property
    johnSmith.addProperty(VCARD.FN, fullName);

}
