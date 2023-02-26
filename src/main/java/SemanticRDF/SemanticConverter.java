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

public class SemanticConverter {
    private String schema;
    private String csv;
    private Model schema_model;
    private Model rdf_model;

    public SemanticConverter(String schema_loc, String csv_loc){
        schema = schema_loc;
        csv = csv_loc;
        schema_model  = RDFDataMgr.loadModel(schema);
    }

    public void csv_to_rdf() throws FileNotFoundException{
        Model rdf = ModelFactory.createDefaultModel();
        File f = new File(csv);
        ArrayList<String> ns_uri = get_ns_array();

        String ds = ns_uri.get(1);
        rdf.setNsPrefix("ds", ds);

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String[] col = br.readLine().split(",");
            String data = br.readLine(); 

            int i = 0;
            while(data != null){ 
                String[] row_data = data.split(","); 
                Resource root = rdf.createResource(ds + "row-"+String.valueOf(i+1));

                for(int j = 0; j < row_data.length; j++){
                    Property p = rdf.createProperty(ds+col[j]);
                    Literal o = rdf.createLiteral(row_data[j]);
                    rdf.add(root, p, o);
                }
                data = br.readLine();
                i++;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //rdf.write(System.out);
        rdf_model = rdf;
        output_to_file(rdf_model);
    }

    private void output_to_file(Model rdf) throws FileNotFoundException{
        String name = csv.replaceFirst(".*/(\\w+).*","$1");;
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
