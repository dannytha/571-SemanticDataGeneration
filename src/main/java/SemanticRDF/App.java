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

        
        COVID_Converter new_covid = new COVID_Converter("dataset/LA_County_COVID_Cases.csv");
        
        //LAX_Converter new_laxa = new LAX_Converter("dataset/Los_Angeles_International_Airport_Passenger_Count_By_Carrier_Type.csv");
        //new_laxa.csv_to_rdf();

        //new_covid.csv_to_rdf();
        //snew_laxa.csv_to_rdf();
        

        //Testing Query System
        String ont_1 = "output/Los_Angeles_International_Airport_Passenger_Count_By_Carrier_Type.rdf";
        String ont_2 = "output/LA_County_COVID_Cases.rdf";

        SPARQL_Query q_sys = new SPARQL_Query();
        Path filePath = Path.of("queries/BasicFlightQuery.txt");
        String content = Files.readString(filePath);
        String queryString = content;

        //q_sys.new_query(ont_1, queryString);

        Path c_path = Path.of("queries/SimpleCovidQuery.txt");
        String c_content = Files.readString(c_path);
        String c_queryString = c_content;
        
        q_sys.new_query(ont_2, c_queryString);


        
    }
}
