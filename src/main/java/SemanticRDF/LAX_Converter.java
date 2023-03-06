package SemanticRDF;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NsIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;

public class LAX_Converter {
    /*
    private String schema = "https://holder/";
    private String csv;
    private Model schema_model;
    private Model rdf_model;*/

    private Model rdf;
    private String csv;
    private String homepage = "https://FlighReport.com/";

    //lax constructor
    public void main( String[] args ) throws FileNotFoundException{
        csv = "dataset/Los_Angeles_International_Airport_Passenger_Count_By_Carrier_Type.csv";
        csv_to_rdf();

    }


    public void csv_to_rdf() throws FileNotFoundException{
        rdf = ModelFactory.createDefaultModel();
        File f = new File(csv);

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String[] col = br.readLine().split(","); //column names
            String data = br.readLine(); //first row of data

            int i = 0;
            while(data != null){ 
                
                String[] row_data = data.split(","); 
                create_resource(row_data, rdf, i);

                /*
                Resource root = rdf.createResource(ds +"row-"+ String.valueOf(i+1));

                for(int j = 0; j < row_data.length; j++){
                    Property p = rdf.createProperty(ds+col[j]);
                    Literal o = rdf.createLiteral(row_data[j]);
                    rdf.add(root, p, o);
                }
                */
                //get next line
                data = br.readLine();
                i++;
            }

            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        rdf.write(System.out);
        //rdf_model = rdf;
        //output_to_file(rdf_model);
    }
    public void create_resource(String[] row_data, Model model, int i){
        //initialization for row
        Resource root = model.createResource("https://row-number/"+i);

        //-----data extract date
        Resource extract = model.createResource(homepage+"extract");
        Property extracted_date = model.createProperty(homepage+"extractedDate");

        //report period
        Resource report_period = model.createResource(homepage+"reportperiod");
        Property reported_date = model.createProperty(homepage+"reportedDate");

        //arrival/departure
        Resource direction = model.createResource(homepage+"direction");
        Property has_direction = model.createProperty(homepage+"hasDirection");

        //domestic/int
        Resource travel_type = model.createResource(homepage+"travel_type");
        Property has_travel_type = model.createProperty(homepage+"hasTravelType");

        //flight type
        Resource flight_type = model.createResource(homepage+"flight_type");
        Property isof_FlightType = model.createProperty(homepage+"isOfFlightType");

        //passenger count
        Literal pass_count = model.createTypedLiteral(Integer.valueOf(row_data[5]));
        Property counted_pass = model.createProperty(homepage+"countedPassenger");
        root.addLiteral(counted_pass, pass_count);

        //model.add(root);
    }


    private void output_to_file(Model rdf) throws FileNotFoundException{
        String name = csv.replaceFirst(".*/(\\w+).*","$1");
        String path = "output/" + name + ".rdf";
        File file = new File(path);

        if(file.exists()){
            file.delete();
        }
        FileOutputStream fs = new FileOutputStream(file);
        rdf.write(fs);
    }

    private ArrayList<String> get_ns_array(){
        ArrayList<String> ns_uri = new ArrayList<>();
        NsIterator iter = schema_model.listNameSpaces();
        while (iter.hasNext()) {
            String next = iter.next();
            ns_uri.add(next.toString());
        }
        return ns_uri;
    }

    public Model get_RDF_model(){
        return rdf_model;
    }
}
