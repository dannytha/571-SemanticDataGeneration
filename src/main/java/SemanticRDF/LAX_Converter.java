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
import org.apache.jena.ontology.OntModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.NsIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFWriterI;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.RDFWriter;
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
        //Model rdf = ModelFactory.createDefaultModel();
        Model schema = schemaCreate(); 
        //File f = new File(csv);

        //rdf.setNsPrefix("fr", homepage);
/* 
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            String[] col = br.readLine().split(","); //column names
            String data = br.readLine(); //first row of data

            int i = 0;
            while(data != null && i<5){ 
                
                String[] row_data = data.split(","); 
                create_resource(row_data, rdf, i);

                
                Resource root = rdf.createResource(ds +"row-"+ String.valueOf(i+1));

                for(int j = 0; j < row_data.length; j++){
                    Property p = rdf.createProperty(ds+col[j]);
                    Literal o = rdf.createLiteral(row_data[j]);
                    rdf.add(root, p, o);
                }
                
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
*/
        //rdf.write(System.out);
        //rdf_model = rdf;
        output_to_file(schema);
    }

    public Model schemaCreate(){
        Model schema = ModelFactory.createDefaultModel();

        Resource flightReport = schema.createResource(homepage+"FlightReport");
        flightReport.addProperty(RDF.type,RDFS.Class);
        Resource airport = schema.createResource(homepage+"Airport");
        airport.addProperty(RDF.type,RDFS.Class);
        Resource country = schema.createResource(homepage+"Country");
        country.addProperty(RDF.type,RDFS.Class);
        Resource flights = schema.createResource(homepage+"Flights");
        flights.addProperty(RDF.type,RDFS.Class);
        Resource flightType = schema.createResource(homepage+"FlightType");
        flightType.addProperty(RDF.type,RDFS.Class);
        Resource charter = schema.createResource(homepage+"Charter");
        charter.addProperty(RDFS.subClassOf,flightType);
        Resource scheduledCarrier = schema.createResource(homepage+"ScheduledCarrier");
        scheduledCarrier.addProperty(RDFS.subClassOf,flightType);
        Resource commuter = schema.createResource(homepage+"Commuter");
        commuter.addProperty(RDFS.subClassOf,flightType);
        Resource direction = schema.createResource(homepage+"Direction");
        direction.addProperty(RDF.type,RDFS.Class);
        Resource departure = schema.createResource(homepage+"Departure");
        departure.addProperty(RDFS.subClassOf,direction);
        Resource arrival = schema.createResource(homepage+"Arrival");
        arrival.addProperty(RDFS.subClassOf,direction);
        Resource travelType = schema.createResource(homepage+"TravelType");
        travelType.addProperty(RDF.type,RDFS.Class);
        Resource international = schema.createResource(homepage+"International");
        international.addProperty(RDFS.subClassOf,travelType);
        Resource domestic = schema.createResource(homepage+"Domestic");
        domestic.addProperty(RDFS.subClassOf,travelType);
        Resource dateTime = schema.createResource(homepage+"DateTime");
        dateTime.addProperty(RDF.type,RDFS.Class);
        Resource extractionDate = schema.createResource(homepage+"ExtractionDate");
        extractionDate.addProperty(RDFS.subClassOf,dateTime);
        Resource reportPeriod = schema.createResource(homepage+"ReportPeriod");
        reportPeriod.addProperty(RDFS.subClassOf,dateTime);

        Property locatedIn = schema.createProperty(homepage+"locatedIn");
        locatedIn.addProperty(RDFS.domain, airport);
        locatedIn.addProperty(RDFS.range, country);
        Property reportedFrom = schema.createProperty(homepage+"reportedFrom");
        reportedFrom.addProperty(RDFS.domain, flightReport);
        reportedFrom.addProperty(RDFS.range, airport);
        Property countedPassengers = schema.createProperty(homepage+"countedPassengers");
        countedPassengers.addProperty(RDFS.domain, flightReport);
        //countedPassengers.addProperty(RDFS.range, );
        Property reportsOn = schema.createProperty(homepage+"reportsOn");
        reportsOn.addProperty(RDFS.domain, flightReport);
        reportsOn.addProperty(RDFS.range, flights);
        Property extractedDate = schema.createProperty(homepage+"extractedDate");
        extractedDate.addProperty(RDFS.domain, flightReport);
        extractedDate.addProperty(RDFS.range, extractionDate);
        Property reportedDate = schema.createProperty(homepage+"reportedDate");
        reportedDate.addProperty(RDFS.domain, flightReport);
        reportedDate.addProperty(RDFS.range, reportedDate);
        Property isOfFlightType = schema.createProperty(homepage+"isOfFlightType");
        isOfFlightType.addProperty(RDFS.domain, flights);
        isOfFlightType.addProperty(RDFS.range, flightType);
        Property hasTravelType = schema.createProperty(homepage+"hasTravelType");
        hasTravelType.addProperty(RDFS.domain, flights);
        hasTravelType.addProperty(RDFS.range, travelType);
        Property hasDirection = schema.createProperty(homepage+"hasDirection");
        hasDirection.addProperty(RDFS.domain, flights);
        hasDirection.addProperty(RDFS.range, direction);
        Property hasReportID = schema.createProperty(homepage+"hasReportID");
        hasReportID.addProperty(RDFS.domain, flightReport);
        //hasReportID.addProperty(RDFS.range, country);

        // Associate Classes with Properties

        return schema;
    }

    
    public void create_resource(String[] row_data, Model model, int i){
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
        Property located_in = model.createProperty(homepage+"locatedIn");
        Property reported_from = model.createProperty(homepage+"reportedFrom");

        airport.addProperty(located_in, country);
        //root.addProperty(reported_from, airport);
        model.add(root, reported_from, airport);

        /*
         * extract date
         */
        Resource date_time = model.createResource(homepage+"DateTime");
        Resource extract = model.createResource(homepage+"ExtractDate");
        Property extracted_date = model.createProperty(homepage+"extractedDate");
        extract.addProperty(RDFS.subClassOf, date_time);
        
        /*
         * report period
         */
        Resource report_period = model.createResource(homepage+"ReportPeriod");
        Property reported_date = model.createProperty(homepage+"reportedDate");
        report_period.addProperty(RDFS.subClassOf, date_time);

        /*
         * arrival/departure
         */
        Resource direction = model.createResource(homepage+"Direction");
        Resource arrival = model.createResource(homepage+"Arrival");
        Resource departure = model.createResource(homepage+"Departure");
        Property has_direction = model.createProperty(homepage+"hasDirection");
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
        Property has_travel_type = model.createProperty(homepage+"hasTravelType");
        Resource domestic = model.createResource(homepage+"Domestic");
        Resource international = model.createProperty(homepage+"International");
        domestic.addProperty(RDFS.subClassOf, travel_type);
        international.addProperty(RDFS.subClassOf, travel_type);
        if(row_data[3].equals("Domestic")){
            flights.addProperty(has_travel_type, domestic);
        }else{
            flights.addProperty(has_travel_type, international);
        }

        Property reports_on = model.createProperty(homepage+"reportsOn");

        /*
         * flight type
         */
        Resource flight_type = model.createResource(homepage+"FlightType");
        Property isof_FlightType = model.createProperty(homepage+"isOfFlightType");
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
        Property counted_pass = model.createProperty(homepage+"countedPassenger");
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
