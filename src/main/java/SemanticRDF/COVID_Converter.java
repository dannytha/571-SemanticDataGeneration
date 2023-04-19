package SemanticRDF;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.naming.ldap.HasControls;
import javax.naming.spi.ResolveResult;

import org.apache.jena.datatypes.xsd.XSDDateTime;
import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
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

        //rdf.write(System.out);
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
        Resource covid_report = model.createResource(homepage+"CovidReport");

        Resource report = model.createResource(homepage+"Report");
        Resource extraction_date = model.createResource(homepage+"ExtractionDate");
        Resource date_time = model.createResource(homepage+"DateTime");
        Resource county_count = model.createResource(homepage+"CountyCount");
        Resource state_count = model.createResource(homepage+"StateCount");
        Resource location = model.createResource(homepage+"Location");
        Resource state = model.createResource(homepage+"State");
        Resource county = model.createResource(homepage+"County");

        //extraction_date.addProperty(RDFS.subPropertyOf, covid_report);
        //county_count.addProperty(RDFS.subPropertyOf, covid_report);
        //state_count.addProperty(RDFS.subPropertyOf, covid_report);
        Individual cc_individual = model.createIndividual(homepage+"CovidReport/row-"+i+"/", county_count);
        Individual sc_individual = model.createIndividual(homepage+"CovidReport/row-"+i+"/", state_count);
        Individual ed_individual = model.createIndividual(homepage+"CovidReport/row-"+i+"/", extraction_date);
        Individual cr_individual = model.createIndividual(homepage+"CovidReport/row-"+i+"/", covid_report);

        report.addProperty(RDF.type, RDFS.Class);
        county_count.addProperty(RDF.type, RDFS.Class);
        state_count.addProperty(RDF.type, RDFS.Class);
        location.addProperty(RDF.type, RDFS.Class);
        state.addProperty(RDF.type, RDFS.Class);
        county.addProperty(RDF.type, RDFS.Class);

        //covid_report.addProperty(RDF.type, RDFS.Class);
        //covid_report.addProperty(RDF.type, report);

        ObjectProperty has_date = model.createObjectProperty(homepage+"hasDate");
        ObjectProperty reported_in = model.createObjectProperty(homepage+"reportedIn");
        //ObjectProperty isAReport = model.createObjectProperty(homepage+"isAReport");
        ObjectProperty has_a = model.createObjectProperty(homepage+"hasA");

        //covid_report.addProperty(isAReport, report);

        has_date.addProperty(RDFS.domain, report);
        reported_in.addProperty(RDFS.domain, report);
        has_a.addProperty(RDFS.domain, location);
        has_a.addProperty(RDFS.domain, state);
        has_date.addProperty(RDFS.range, extraction_date);
        reported_in.addProperty(RDFS.range, location);
        has_a.addProperty(RDFS.range, state);
        has_a.addProperty(RDFS.range, county);
        
        DatatypeProperty county_case_count = model.createDatatypeProperty(homepage+"CountyCaseCount");
        DatatypeProperty county_death_count = model.createDatatypeProperty(homepage+"CountyDeathCount");
        DatatypeProperty new_county_case_count = model.createDatatypeProperty(homepage+"NewCountyCaseCount");
        DatatypeProperty new_county_death_count = model.createDatatypeProperty(homepage+"NewCountyDeathCount");

        county_case_count.addProperty(RDFS.domain, county_count);
        county_death_count.addProperty(RDFS.domain, county_count);
        new_county_case_count.addProperty(RDFS.domain, county_count);
        new_county_death_count.addProperty(RDFS.domain, county_count);
        county_case_count.addProperty(RDFS.range, XSD.integer);
        county_death_count.addProperty(RDFS.range, XSD.integer);
        new_county_case_count.addProperty(RDFS.range, XSD.integer);
        new_county_death_count.addProperty(RDFS.range, XSD.integer);

        DatatypeProperty state_case_count = model.createDatatypeProperty(homepage+"StateCaseCount");
        DatatypeProperty state_death_count = model.createDatatypeProperty(homepage+"StateDeathCount");
        DatatypeProperty new_state_case_count = model.createDatatypeProperty(homepage+"NewStateCaseCount");
        DatatypeProperty new_state_death_count = model.createDatatypeProperty(homepage+"NewStateDeathCount");

        state_case_count.addProperty(RDFS.domain, state_count);
        state_death_count.addProperty(RDFS.domain, state_count);
        new_state_case_count.addProperty(RDFS.domain, state_count);
        new_state_death_count.addProperty(RDFS.domain, state_count);
        state_case_count.addProperty(RDFS.range, XSD.integer);
        state_death_count.addProperty(RDFS.range, XSD.integer);
        new_state_case_count.addProperty(RDFS.range, XSD.integer);
        new_state_death_count.addProperty(RDFS.range, XSD.integer);

        DatatypeProperty lon = model.createDatatypeProperty(homepage+"Lon");
        DatatypeProperty lat = model.createDatatypeProperty(homepage+"Lat");
        DatatypeProperty fips = model.createDatatypeProperty(homepage+"fips");
        DatatypeProperty state_name = model.createDatatypeProperty(homepage+"StateName");
        DatatypeProperty county_name = model.createDatatypeProperty(homepage+"CountyName");
        DatatypeProperty date = model.createDatatypeProperty(homepage+"Date");

        lon.addProperty(RDFS.domain, county);
        lat.addProperty(RDFS.domain, county);
        fips.addProperty(RDFS.domain, county);
        state_name.addProperty(RDFS.domain, county);
        county_name.addProperty(RDFS.domain, county);
        date.addProperty(RDFS.domain, extraction_date);
        lon.addProperty(RDFS.range, XSD.xstring);
        lat.addProperty(RDFS.range, XSD.xstring);
        fips.addProperty(RDFS.range, XSD.integer);
        state_name.addProperty(RDFS.range, XSD.xstring);
        county_name.addProperty(RDFS.range, XSD.xstring);
        date.addProperty(RDFS.range, XSD.dateTime);


        ObjectProperty has_county_count = model.createObjectProperty(homepage+"hasCountyCount");
        ObjectProperty has_state_count = model.createObjectProperty(homepage+"hasStateCount");

        has_county_count.addProperty(RDFS.domain, report);
        has_state_count.addProperty(RDFS.domain, report);
        has_county_count.addProperty(RDFS.range, county_count);
        has_state_count.addProperty(RDFS.range, state_count);

        covid_report.addProperty(RDFS.subClassOf, report);

        //county info -> county_count
        cr_individual.addProperty(has_county_count, cc_individual);
        cr_individual.addProperty(has_state_count, sc_individual);
        cr_individual.addProperty(has_date, ed_individual);
        cr_individual.addProperty(reported_in, location);

        cc_individual.addLiteral(county_case_count, Integer.valueOf(row_data[6]));
        //model.add(root, county_case_count, row_data[6]);
        cc_individual.addLiteral(county_death_count, Integer.valueOf(row_data[7]));
        cc_individual.addLiteral(new_county_case_count, Integer.valueOf(row_data[11]));
        cc_individual.addLiteral(new_county_death_count, Integer.valueOf(row_data[12]));

        sc_individual.addLiteral(state_case_count, Integer.valueOf(row_data[9]));
        sc_individual.addLiteral(state_death_count, Integer.valueOf(row_data[10]));
        sc_individual.addLiteral(new_state_case_count, Integer.valueOf(row_data[13]));
        sc_individual.addLiteral(new_state_death_count, Integer.valueOf(row_data[14]));


        extraction_date.addProperty(RDFS.subClassOf, date_time);
        String dateTimeCSV = row_data[3].replace("/", "-");
        DateFormat df = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss a");
        try {
            Date dated = df.parse(dateTimeCSV);
            Calendar cal = new GregorianCalendar();
            cal.setTime(dated);
            XSDDateTime dateTimeXSD = new XSDDateTime(cal);
            Literal extractedLiteral = model.createTypedLiteral(dateTimeXSD);
            ed_individual.addLiteral(date, extractedLiteral);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        location.addProperty(has_a, state);
        state.addProperty(has_a, county);
        state.addProperty(state_name, row_data[1]);
        
        county.addLiteral(fips, Integer.valueOf(row_data[2]));
        county.addLiteral(lat, row_data[4]);
        county.addLiteral(lon, row_data[5]);
        county.addProperty(county_name, row_data[0]);

        //county -> LA

        //model.add(covid_report,has_county_count, county_count);
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