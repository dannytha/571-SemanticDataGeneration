package SemanticRDF;

import java.io.FileNotFoundException;

public class App 
{
    public static void main( String[] args ) throws FileNotFoundException
    {
       //SemanticConverter lax = new SemanticConverter("schema/LAX_PassengerCountByCarrierType_Schema.rdf",
    //"dataset/Los_Angeles_International_Airport_Passenger_Count_By_Carrier_Type.csv");
        
        //SemanticConverter covid = new SemanticConverter("schema/LACity_Covid_Schema.rdf","dataset/LA_County_COVID_Cases.csv");
        
        //covid.csv_to_rdf();
        //lax.csv_to_rdf();

        COVID_Converter new_covid = new COVID_Converter("dataset/LA_County_COVID_Cases.csv");
        LAX_Converter new_laxa = new LAX_Converter("dataset/Los_Angeles_International_Airport_Passenger_Count_By_Carrier_Type.csv");
        //new_laxa.csv_to_rdf();

        new_covid.csv_to_rdf();
        new_laxa.csv_to_rdf();
        
        
    }
}
