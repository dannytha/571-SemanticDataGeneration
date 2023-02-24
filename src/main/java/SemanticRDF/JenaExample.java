package SemanticRDF;
import java.util.Iterator;
import org.apache.jena.graph.Graph;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;

/*
Steps to convert CSV files to an RDF based on a provided ontology:
    [^] 1. Read and load a schema (RDF/OWL/TTL) into a JENA model    
    []  2. Read and parse CSV dataset
            a. Retrieve URI of resource (row key data)
            b. Retrieve URI of the RDF property pertaining to that column
            c. Create literal value
    []  4. Write contents back out into an RDF model
 */

public class JenaExample {
    public static void main( String[] args ){
        //Graph vocab = RDFDataMgr.loadGraph("schema/LAX_PassengerCountByCarrierType_Schema1.rdf");
        //Ontology api
        OntModel m = ModelFactory.createOntologyModel();
        m.read("schema/LAX_PassengerCountByCarrierType_Schema2.rdf");
        Model base = m.getBaseModel();

        //Create a new default model to hold information
        Model new_model = ModelFactory.createDefaultModel();

        //2 ways to load schema into a model
        new_model.read("schema/LAX_PassengerCountByCarrierType_Schema2.rdf");
        Model schema  = RDFDataMgr.loadModel("schema/LAX_PassengerCountByCarrierType_Schema2.rdf");

       

    }
}
