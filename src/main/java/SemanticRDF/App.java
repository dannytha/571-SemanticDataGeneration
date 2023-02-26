package SemanticRDF;

import java.io.FileNotFoundException;

public class App 
{
    public static void main( String[] args ) throws FileNotFoundException
    {
       SemanticConverter lax = new SemanticConverter("schema/LAX_PassengerCountByCarrierType_Schema.rdf",
        "dataset/Los_Angeles_International_Airport_Passenger_Count_By_Carrier_Type.csv");

        lax.csv_to_rdf();
    }
}
