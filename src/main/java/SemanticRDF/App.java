package SemanticRDF;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

import javax.swing.JFileChooser;


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
        
        //FileSelect
        JFileChooser jfc_onts = new JFileChooser();
        File current_dir = new File(System.getProperty("user.dir"));
        jfc_onts.setDialogTitle("Choose the ontology files to open (RDF/OWL)");
        jfc_onts.setCurrentDirectory(current_dir);
        jfc_onts.setMultiSelectionEnabled(true);;
        int option = jfc_onts.showOpenDialog(null);
        ArrayList<String> onts = new ArrayList<>();
        if (option == JFileChooser.APPROVE_OPTION) {
            System.out.println("===== LOADED SEMANTIC FILES =====");
            File[] files = jfc_onts.getSelectedFiles();
            for (File file : files) {
               onts.add(file.getAbsolutePath()); 
               System.out.println("- " + file.getAbsolutePath());
            } 
        } else {
            System.exit(0);
        }
        jfc_onts.setMultiSelectionEnabled(false);
        jfc_onts.setDialogTitle("Choose the SPARQL query you wish to open (.txt)");
        int option2 = jfc_onts.showOpenDialog(null);
        String sparql_query = null;
        if (option2 == JFileChooser.APPROVE_OPTION) {
            sparql_query = jfc_onts.getSelectedFile().getAbsolutePath();
        } else {
            System.exit(0);
        }
        
        // Begin Query
        SPARQL_Query q_sys = new SPARQL_Query();

        Path cm = Path.of(sparql_query);
        String cm_content = Files.readString(cm);
        String cm_string = cm_content;
        q_sys.multi_query(onts, cm_string);
        
    }
}
