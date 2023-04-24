package SemanticRDF;

import java.io.*;
import java.nio.file.*;


public class App 
{
    public static void main( String[] args ) throws IOException
    {
        //SemanticConverter lax = new SemanticConverter("schema/LAX_PassengerCountByCarrierType_Schema.rdf",
        //"dataset/Los_Angeles_International_Airport_Passenger_Count_By_Carrier_Type.csv");
        
        //SemanticConverter covid = new SemanticConverter("schema/LACity_Covid_Schema.rdf","dataset/LA_County_COVID_Cases.csv");
        
        //covid.csv_to_rdf();
        //lax.csv_to_rdf();

        
        //COVID_Converter new_covid = new COVID_Converter("dataset/LA_County_COVID_Cases.csv");
        
        //LAX_Converter new_laxa = new LAX_Converter("dataset/Los_Angeles_International_Airport_Passenger_Count_By_Carrier_Type.csv");
        //new_laxa.csv_to_rdf();

        //new_covid.csv_to_rdf();
        //snew_laxa.csv_to_rdf();
        

        //Testing Query System
        String complexity = args[0];

        SPARQL_Query q_sys = new SPARQL_Query();

        if(complexity.equals("s")){
            String ont_1 = args[1];
            String query_path = args[2];
        /*
         * Query Ontology
         */
        Path filePath = Path.of(query_path);
        String content = Files.readString(filePath);
        String queryString = content;
        q_sys.new_query(ont_1, queryString);

        }
        else if(args[0].equals("c")){
            String ont_1 = args[1];
            String ont_2 = args[2];
            String query_path = args[3];
        /*
         * Complex Query Merged Ontology
         */
        Path cm = Path.of(query_path);
        String cm_content = Files.readString(cm);
        String cm_string = cm_content;
        q_sys.new_query(ont_1, ont_2, cm_string);
        }
    }
}
