package SemanticRDF;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.apache.jena.graph.Graph;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NsIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.RDFDataMgr;

/*
Steps to convert CSV files to an RDF based on a provided ontology:
    [^] 1. Read and load a schema (RDF/OWL/TTL) into a JENA model    
    []  2. Read and parse CSV dataset
            a. Retrieve URI of resource (row key data)
            b. Retrieve URI of the RDF property pertaining to that column
            c. Create literal value
    []  4. Write contents back out into an RDF model

Inputs:
    1. RDF SCHEMA
    2. CSV data file

Outputs:
    1. RDF 
 */

public class JenaExample {
    public static void main( String[] args ){
        
        //Graph vocab = RDFDataMgr.loadGraph("schema/LAX_PassengerCountByCarrierType_Schema1.rdf");
        //Ontology api
        //OntModel m = ModelFactory.createOntologyModel();
        //m.read("schema/LAX_PassengerCountByCarrierType_Schema.rdf");
        //Model base = m.getBaseModel();

        //Create a new default model to hold schema information
        Model new_model = ModelFactory.createDefaultModel();
        //2 ways to load schema into a model
        new_model.read("schema/LAX_PassengerCountByCarrierType_Schema.rdf");
        Model schema  = RDFDataMgr.loadModel("schema/LAX_PassengerCountByCarrierType_Schema.rdf");

        //Create a model to hold RDF information
        Model rdf = ModelFactory.createDefaultModel();

        // Read and parse CSV dataset
        File f = new File("dataset/Los_Angeles_International_Airport_Passenger_Count_By_Carrier_Type.csv");
        System.out.println(f.canRead());
        
        // Create special prefix namespaces
        //NOTE: this is where i  had to call the URI (URL of the csv) manually 
        //String ds = "https://data.lacity.org/resource/d3a2-7j6v/";
        String ds = "https://data.lacity.org/Transportation/Los-Angeles-International-Airport-Passenger-Count-/d3a2-7j6v/";

        //attempting to just read from schema
        ArrayList<String> ns_uri = new ArrayList<>();
        NsIterator iter = schema.listNameSpaces();
        while (iter.hasNext()) {
            String next = iter.next();
            ns_uri.add(next.toString());
        }

        rdf.setNsPrefix("ds", ns_uri.get(1));

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));

            //reading the column names to array to reference later
            String[] col = br.readLine().split(",");
            System.out.println(Arrays.toString(col));

            //testing with first 3 rows
            int i = 0;
            String[] row_data = br.readLine().split(","); 
            while(row_data != null && i < 3){ 
                System.out.println(Arrays.toString(row_data));
                //System.out.println(Arrays.toString(row_data));
                
                //Holder
                //String resource_uri = "http://somewhere/JohnSmith";

                //Create a resource (this pertains to a single row in the csv)
                //Resource current = rdf.createResource(ds+"/row-"+String.valueOf(i));
                Resource root = rdf.createResource(ds+"row-"+String.valueOf(i+1));

                //Loop through each element of row_data and col
                //create properties for each 'col' element (reference schema?)
                //its literal value is the corresponding 'row_data' element
                for(int j = 0; j < row_data.length; j++){
                    Property p = rdf.createProperty(ds+col[j]);
                    Literal o = rdf.createLiteral(row_data[j]);
                    //current.addProperty(p, o);
                    rdf.add(root, p, o);

                }

                row_data = br.readLine().split(","); 
                i++;
            }


            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        rdf.write(System.out);
    }
}
