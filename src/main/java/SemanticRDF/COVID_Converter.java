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
        OntModel rdf = ModelFactory.createOntologyModel();
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

    public void create_resource(String[] row_data, OntModel model, int i){
        /*
         * initialization 
         */
        Resource covid_report = model.createResource(homepage+"row-number/"+i);

        Resource report = model.createResource(homepage+"Report");
        Resource extraction_date = model.createResource(homepage+"ExtractionDate");
        Resource date_time = model.createResource(homepage+"DateTime");
        Resource county_count = model.createResource(homepage+"CountyCount");
        Resource state_count = model.createResource(homepage+"StateCount");
        Resource location = model.createResource(homepage+"Location");
        Resource state = model.createResource(homepage+"State");
        Resource county = model.createResource(homepage+"County");

        report.addProperty(RDF.type, RDFS.Class);
        county_count.addProperty(RDF.type, RDFS.Class);
        state_count.addProperty(RDF.type, RDFS.Class);
        location.addProperty(RDF.type, RDFS.Class);
        state.addProperty(RDF.type, RDFS.Class);
        county.addProperty(RDF.type, RDFS.Class);

        covid_report.addProperty(RDF.type, RDFS.Class);
        covid_report.addProperty(RDF.type, report);

        ObjectProperty has_date = model.createObjectProperty(homepage+"hasDate");
        ObjectProperty reported_in = model.createObjectProperty(homepage+"reportedIn");
        //Property isAReport = model.createProperty(homepage+"isAReport");
        ObjectProperty is_in = model.createObjectProperty(homepage+"isIn");
        
        DatatypeProperty county_case_count = model.createDatatypeProperty(homepage+"CountyCaseCount");
        DatatypeProperty county_death_count = model.createDatatypeProperty(homepage+"CountyDeathCount");
        DatatypeProperty new_county_case_count = model.createDatatypeProperty(homepage+"NewCountyCaseCount");
        DatatypeProperty new_county_death_count = model.createDatatypeProperty(homepage+"NewCountyDeathCount");

        DatatypeProperty state_case_count = model.createDatatypeProperty(homepage+"StateCaseCount");
        DatatypeProperty state_death_count = model.createDatatypeProperty(homepage+"StateDeathCount");
        DatatypeProperty new_state_case_count = model.createDatatypeProperty(homepage+"NewStateCaseCount");
        DatatypeProperty new_state_death_count = model.createDatatypeProperty(homepage+"NewStateDeathCount");

        DatatypeProperty lon = model.createDatatypeProperty(homepage+"Lon");
        DatatypeProperty lat = model.createDatatypeProperty(homepage+"Lat");
        DatatypeProperty fips = model.createDatatypeProperty(homepage+"fips");

        ObjectProperty has_county_count = model.createObjectProperty(homepage+"hasCountyCount");
        ObjectProperty has_state_count = model.createObjectProperty(homepage+"hasStateCount");

        covid_report.addProperty(has_county_count, county_count);
        covid_report.addProperty(has_state_count, state_count);
        covid_report.addProperty(has_date, extraction_date);
        covid_report.addProperty(reported_in, location);

        county_count.addProperty(county_case_count, row_data[6]);
        //model.add(root, county_case_count, row_data[6]);
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

        model.add(covid_report,has_county_count, county_count);
        //model.add()
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