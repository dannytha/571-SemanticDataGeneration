package SemanticRDF;

import java.io.*;
import java.nio.file.*;

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
        RDFDataMgr.read(model, ont2);

        Query query = QueryFactory.create(query_string);
        //pass in query or query_string
        QueryExecution queryResults = QueryExecutionFactory.create(query, model);
        ResultSet results = queryResults.execSelect();
        ResultSetFormatter.out(System.out, results, query);

        return results;
    }
}
