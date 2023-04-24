package SemanticRDF;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;

public class SPARQL_Query {
    private String ontology;
    private Model model;

    public SPARQL_Query(){
    }

    public ResultSet new_query(String ont, String query_string){
        ontology = ont;
        model = RDFDataMgr.loadModel(ontology);

        Query query = QueryFactory.create(query_string);
        //pass in query or query_string
        QueryExecution queryResults = QueryExecutionFactory.create(query, model);

        ResultSet results = queryResults.execSelect();

        ResultSetFormatter.out(System.out, results, query);
        return results;
    }

    public ResultSet new_query(String ont, String ont2, String query_string){
        model = RDFDataMgr.loadModel(ont);
        System.out.println(ont);
        RDFDataMgr.read(model, ont2);

        Query query = QueryFactory.create(query_string);
        //pass in query or query_string
        QueryExecution queryResults = QueryExecutionFactory.create(query, model);
        ResultSet results = queryResults.execSelect();
        ResultSetFormatter.out(System.out, results, query);

        return results;
    }

    public ResultSet multi_query(ArrayList<String> onts, String query_string) {
        String outputFile = "queryResults.txt";
        model = RDFDataMgr.loadModel(onts.get(0));
        for (int i = 1; i <= onts.size() -1; i++) {
            RDFDataMgr.read(model, onts.get(i));
        }
        System.out.println("===== YOUR QUERY =====");
        System.out.println(query_string);
        Query query = QueryFactory.create(query_string);
        // pass in query or query_string
        QueryExecution queryResults = QueryExecutionFactory.create(query, model);
        ResultSet results = queryResults.execSelect();
        System.out.println("===== RESULTS =====");
        String saveResultString = ResultSetFormatter.asText(results, query);
        System.out.println(saveResultString);
        try {
            if(!Files.exists(Paths.get(outputFile))) {
                Files.createFile(Paths.get(outputFile));
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
            writer.write(saveResultString);
            writer.close();
            System.out.println("Result set written to file: " + outputFile);
        } catch (Exception e){
            e.printStackTrace();
        }

        return results;
    }
}
