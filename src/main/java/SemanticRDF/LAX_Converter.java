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
    public LAX_Converter(String loc ){
        csv = loc;
    }


    public void csv_to_rdf() throws FileNotFoundException{
        OntModel rdf = ModelFactory.createOntologyModel();
        Model schema = ModelFactory.createDefaultModel();
        File f = new File(csv);

        rdf.setNsPrefix("fr", homepage);

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

        Resource country = model.createResource(homepage+"Country");
        country.addProperty(RDF.type,RDFS.Class);
        Resource airport = model.createResource(homepage+"Airport");
        airport.addProperty(RDF.type,RDFS.Class);
        Property located_in = model.createProperty(homepage+"locatedIn");
        located_in.addProperty(RDFS.domain, airport);
        located_in.addProperty(RDFS.range, country);
        Property reported_from = model.createProperty(homepage+"reportedFrom");
        

        Resource flights = model.createResource(homepage+"Flights");
        flights.addProperty(RDF.type,RDFS.Class);
        Resource flight_reports = model.createResource(homepage+"FlightReport");
        flight_reports.addProperty(RDF.type,RDFS.Class);
        Property reports_on = model.createProperty(homepage+"reportsOn");

        reported_from.addProperty(RDFS.domain, airport);
        reported_from.addProperty(RDFS.range, flight_reports);
        reports_on.addProperty(RDFS.domain, flights);
        reports_on.addProperty(RDFS.range, reports_on);
        

        Resource date_time = model.createResource(homepage+"DateTime");
        date_time.addProperty(RDF.type,RDFS.Class);
        //date_time.addProperty(RDFS.range, XSD:datetime)
        Resource extract = model.createResource(homepage+"ExtractDate");
        extract.addProperty(RDF.type,RDFS.Class);
        extract.addProperty(RDFS.subClassOf, date_time);
        Resource report_period = model.createResource(homepage+"ReportPeriod");
        report_period.addProperty(RDF.type,RDFS.Class);
        report_period.addProperty(RDFS.subClassOf, date_time);
        Property extracted_date = model.createProperty(homepage+"extractedDate");
        Property reported_date = model.createProperty(homepage+"reportedDate");
        //subClassof

        Resource direction = model.createResource(homepage+"Direction");
        direction.addProperty(RDF.type,RDFS.Class);
        Resource arrival = model.createResource(homepage+"Arrival");
        arrival.addProperty(RDF.type,RDFS.Class);
        arrival.addProperty(RDFS.subClassOf, direction);
        Resource departure = model.createResource(homepage+"Departure");
        departure.addProperty(RDF.type,RDFS.Class);
        departure.addProperty(RDFS.subClassOf, direction);
        Property has_direction = model.createProperty(homepage+"hasDirection");

        Resource travel_type = model.createResource(homepage+"TravelType");
        travel_type.addProperty(RDF.type,RDFS.Class);
        Resource domestic = model.createResource(homepage+"Domestic");
        domestic.addProperty(RDF.type,RDFS.Class);
        domestic.addProperty(RDFS.subClassOf, travel_type);
        Resource international = model.createProperty(homepage+"International");
        international.addProperty(RDF.type,RDFS.Class);
        international.addProperty(RDFS.subClassOf, travel_type);

        Property has_travel_type = model.createProperty(homepage+"hasTravelType");



        Resource flight_type = model.createResource(homepage+"FlightType");
        flight_type.addProperty(RDF.type,RDFS.Class);

        Resource charter = model.createResource(homepage+"Charter");
        charter.addProperty(RDF.type,RDFS.Class);
        charter.addProperty(RDFS.subClassOf, flight_type);

        Resource scheduled_carrier = model.createResource(homepage+"ScheduledCarrier");
        scheduled_carrier.addProperty(RDF.type,RDFS.Class);
        scheduled_carrier.addProperty(RDFS.subClassOf, flight_type);

        Resource commuter = model.createResource(homepage+"Commuter");
        commuter.addProperty(RDF.type,RDFS.Class);
        commuter.addProperty(RDFS.subClassOf, flight_type);

        Property isof_FlightType = model.createProperty(homepage+"isOfFlightType");


        return model;
    }

    public void create_resource(String[] row_data, OntModel model, int i){
        /*
         * initialization 
         */
        Resource root = model.createResource("https://row-number/"+i); //root = flightsreport
        Resource flight_reports = model.createResource();
        //flight_reports.addProperty(RDF.type, RDFS.Class);
        //root.addProperty(RDF.type, flight_reports);
        Resource flights = model.createResource();

        Resource country = model.createResource();
        Resource airport = model.createResource();
        ObjectProperty located_in = model.createObjectProperty(homepage+"locatedIn");
        ObjectProperty reported_from = model.createObjectProperty(homepage+"reportedFrom");

        airport.addProperty(located_in, country);
        //root.addProperty(reported_from, airport);
        model.add(root, reported_from, airport);

        /*
         * extract date
         */
        Resource date_time = model.createResource(homepage+"DateTime");
        date_time.addProperty(RDFS.range, XSD.dateTime);


        Resource extract = model.createResource(homepage+"ExtractDate");
        ObjectProperty extracted_date = model.createObjectProperty(homepage+"extractedDate");
        extract.addProperty(RDFS.subClassOf, date_time);
        extracted_date.addProperty(RDFS.subClassOf, date_time);
        //extracted_date.addProperty(RDFS.domain, root);
        model.add(root, extracted_date, row_data[0]);
        
        /*
         * report period
         */
        Resource report_period = model.createResource(homepage+"ReportPeriod");
        ObjectProperty reported_date = model.createObjectProperty(homepage+"reportedDate");
        report_period.addProperty(RDFS.subClassOf, date_time);

        /*
         * arrival/departure
         */
        Resource direction = model.createResource(homepage+"Direction");
        Resource arrival = model.createResource(homepage+"Arrival");
        Resource departure = model.createResource(homepage+"Departure");
        ObjectProperty has_direction = model.createObjectProperty(homepage+"hasDirection");
        //has_direction.addProperty(RDFS.domain, flights);
        //has_direction.addProperty(RDFS.range,RDFS.);
        arrival.addProperty(RDFS.subClassOf, direction);
        departure.addProperty(RDFS.subClassOf, direction);
        if(row_data[2].equals("Arrival")){
            flights.addProperty(has_direction, arrival);
        }else{
            flights.addProperty(has_direction, departure);
        }

        /*
         * domestic/international
         */
        Resource travel_type = model.createResource(homepage+"TravelType");
        ObjectProperty has_travel_type = model.createObjectProperty(homepage+"hasTravelType");
        Resource domestic = model.createResource(homepage+"Domestic");
        Resource international = model.createProperty(homepage+"International");
        domestic.addProperty(RDFS.subClassOf, travel_type);
        international.addProperty(RDFS.subClassOf, travel_type);
        if(row_data[3].equals("Domestic")){
            flights.addProperty(has_travel_type, domestic);
        }else{
            flights.addProperty(has_travel_type, international);
        }

        ObjectProperty reports_on = model.createObjectProperty(homepage+"reportsOn");

        /*
         * flight type
         */
        Resource flight_type = model.createResource(homepage+"FlightType");
        ObjectProperty isof_FlightType = model.createObjectProperty(homepage+"isOfFlightType");
        Resource charter = model.createResource(homepage+"Charter");
        Resource scheduled_carrier = model.createResource(homepage+"ScheduledCarrier");
        Resource commuter = model.createResource(homepage+"Commuter");
        charter.addProperty(RDFS.subClassOf, flight_type);
        scheduled_carrier.addProperty(RDFS.subClassOf, flight_type);
        commuter.addProperty(RDFS.subClassOf, flight_type);

        if(row_data[4].equals("Charter")){
            flights.addProperty(isof_FlightType, charter);
        }else if(row_data[4].equals("Commuter")){
            flights.addProperty(isof_FlightType, commuter);
        }else{
            flights.addProperty(isof_FlightType, scheduled_carrier);
        }
        
        /*
         * passenger counter
         */
        Literal pass_count = model.createTypedLiteral(Integer.valueOf(row_data[5]));
        DatatypeProperty counted_pass = model.createDatatypeProperty(homepage+"countedPassenger");
        //root.addLiteral(counted_pass, pass_count);
        model.add(root, counted_pass, pass_count);
        
        //root.addProperty( reports_on, flights );
        model.add(root, reports_on, flights);
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

    /*
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
    */
}
