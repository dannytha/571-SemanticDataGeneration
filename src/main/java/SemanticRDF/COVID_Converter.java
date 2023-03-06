package SemanticRDF;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.naming.spi.ResolveResult;

import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NsIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.XSD;

public class COVID_Converter {
    /*
    private String schema = "https://holder/";
    private String csv;
    private Model schema_model;
    private Model rdf_model;*/

    private Model rdf;
    private String csv;
    private String homepage = "https://COVIDReport.com/";

    //COVID constructor
    public COVID_Converter(String loc ){
        csv = loc;
    }


    public void csv_to_rdf() throws FileNotFoundException{
        Model rdf = ModelFactory.createDefaultModel();
        Model schema = ModelFactory.createDefaultModel();
        File f = new File(csv);

        rdf.setNsPrefix("cd", homepage);

        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String[] col = br.readLine().split(","); //column names
            String data = br.readLine(); //first row of data

            int i = 0;
            while(data != null && i<5){ 
                
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
        output_to_file(rdf);
    }
    public Model create_schema(){
        Model model = ModelFactory.createDefaultModel();


        return model;
    }

    public void create_resource(String[] row_data, Model model, int i){
        /*
         * initialization 
         */
        Resource root = model.createResource(homepage+"row-number/"+i);

        Resource covid_report = model.createResource(homepage+"CovidReport");
        Resource extraction_date = model.createResource(homepage+"ExtractionDate");
        Resource date_time = model.createResource(homepage+"DateTime");
        Resource county_count = model.createResource(homepage+"CountyCount");
        Resource state_count = model.createResource(homepage+"StateCount");
        Resource location = model.createResource(homepage+"Location");
        Resource state = model.createResource(homepage+"State");
        Resource county = model.createResource(homepage+"County");

        Property has_date = model.createProperty(homepage+"hasDate");
        Property reported_in = model.createProperty(homepage+"reportedIn");
        //Property isAReport = model.createProperty(homepage+"isAReport");
        Property is_in = model.createProperty(homepage+"isIn");
        
        Property county_case_count = model.createProperty(homepage+"CountyCaseCount");
        Property county_death_count = model.createProperty(homepage+"CountyDeathCount");
        Property new_county_case_count = model.createProperty(homepage+"NewCountyCaseCount");
        Property new_county_death_count = model.createProperty(homepage+"NewCountyDeathCount");

        Property state_case_count = model.createProperty(homepage+"StateCaseCount");
        Property state_death_count = model.createProperty(homepage+"StateDeathCount");
        Property new_state_case_count = model.createProperty(homepage+"NewStateCaseCount");
        Property new_state_death_count = model.createProperty(homepage+"NewStateDeathCount");

        Property lon = model.createProperty(homepage+"Lon");
        Property lat = model.createProperty(homepage+"Lat");
        Property fips = model.createProperty(homepage+"fips");

        Property has_county_count = model.createProperty(homepage+"hasCountyCount");
        Property has_state_count = model.createProperty(homepage+"hasStateCount");

        covid_report.addProperty(has_county_count, county_count);
        covid_report.addProperty(has_state_count, state_count);
        covid_report.addProperty(has_date, extraction_date);
        covid_report.addProperty(reported_in, location);

        //county_count.addProperty(county_case_count, row_data[6]);
        model.add(root, county_case_count, row_data[6]);
        county_count.addProperty(county_death_count, row_data[7]);
        county_count.addProperty(new_county_case_count, row_data[11]);
        county_count.addProperty(new_county_death_count, row_data[12]);

        state_count.addProperty(state_case_count, row_data[9]);
        state_count.addProperty(state_death_count, row_data[10]);
        state_count.addProperty(new_state_case_count, row_data[13]);
        state_count.addProperty(new_state_death_count, row_data[14]);

        extraction_date.addProperty(RDFS.subClassOf, date_time);

        county.addProperty(is_in, state);
        state.addProperty(is_in, location);
        
        county.addProperty(fips, row_data[2]);
        county.addProperty(lat, row_data[4]);
        county.addProperty(lon, row_data[5]);

        // Add extraction date literal
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
}