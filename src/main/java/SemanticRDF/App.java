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
        String ont_1 = "output/Los_Angeles_International_Airport_Passenger_Count_By_Carrier_Type.rdf";
        String ont_2 = "output/LA_County_COVID_Cases.rdf";
        SPARQL_Query q_sys = new SPARQL_Query();

        /*
         * Query Ontology 1
         */
        Path filePath = Path.of("queries/BasicFlightQuery.txt");
        String content = Files.readString(filePath);
        String queryString = content;
        //q_sys.new_query(ont_1, queryString);

        /*
         * Query Ontology 2
         */
        Path c_path = Path.of("queries/SimpleCovidQuery.txt");
        String c_content = Files.readString(c_path);
        String c_queryString = c_content;
        //q_sys.new_query(ont_2, c_queryString);
        
        /*
         * Query Merged Ontology
         */
        /*
         * 
        Path m = Path.of("queries/MergedQuery.txt");
        String m_content = Files.readString(m);
        String m_string = m_content;
        q_sys.new_query(ont_1, ont_2, m_string);
         */

        /*
         * Complex Query Merged Ontology
         */
        Path cm = Path.of("queries/ComplexMergedQuery.txt");
        String cm_content = Files.readString(cm);
        String cm_string = cm_content;
        //q_sys.new_query(ont_1, ont_2, cm_string);

        /*
         * Complex Query 2 Merged Ontology
         */
        Path cm2 = Path.of("queries/ComplexQuery2.txt");
        String cm2_content = Files.readString(cm2);
        String cm2_string = cm2_content;
        q_sys.new_query(ont_1, ont_2, cm2_string);

    }
}
