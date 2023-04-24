using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.IO;
using IronXL;


using System.Net;
using System.Data;

namespace ConsoleApp2
{
    class Program
    {
        // sort the data in the original data files manually by date first
        static String covidPath = "..\\covid1.csv";
        static String airportPath = "..\\airport1.csv";
        static String outputPath = "..\\pearson.xlsx";

        static void Main(string[] args)
        {


            Console.WriteLine("hello");

            // load both data sets
            WorkBook covidBook = WorkBook.LoadCSV(covidPath, 
                fileFormat : ExcelFileFormat.XLSX, 
                ListDelimiter: ",");
            WorkSheet covidSheet = covidBook.DefaultWorkSheet;
            DataTable covidTable = covidSheet.ToDataTable(true);

            WorkBook airportBook = WorkBook.LoadCSV(airportPath,
                fileFormat: ExcelFileFormat.XLSX,
                ListDelimiter: ",");
            WorkSheet airportSheet = airportBook.DefaultWorkSheet;
            DataTable airportTable = airportSheet.ToDataTable(true);

            

            // output file automatically gets overwritten
            WorkBook Pearson = WorkBook.Create();
            WorkSheet sheet = Pearson.CreateWorkSheet("Sheet1");

            // setup columns
            sheet["A1"].Value = "Date";
            sheet["B1"].Value = "Total Arrivals / Departure";
            sheet["C1"].Value = "Total Cases";


            // VERSION 1
            /*
            int totalTraffic = 0;
            //int totalCases = 0;
            int pearsonRow = 2;
            //bool firstData = true;

            //String dt = DateTime.Now.ToString("MM-dd-yyyy HH:mm");



            
            // loop through both datasets to generate compiled data
            for(int a = 1; a < covidTable.Rows.Count; a++)
            {
                // get sum of all covid deaths on a particular day

                string currRow = covidTable.Rows[a][3].ToString();
                string prevRow = covidTable.Rows[a - 1][3].ToString();

                if (!currRow.Equals(prevRow))
                {
                    

                    for (int b = 1; b < airportTable.Rows.Count; b++)
                    {
                        // get sum of all departure / arrival traffic for particular day
                        if (currRow.Equals(airportTable.Rows[b][1].ToString()))
                        {
                            totalTraffic = Int32.Parse(airportTable.Rows[b][5].ToString()) + totalTraffic;
                        }
                    }

                    //write date
                    sheet["A" + pearsonRow.ToString()].Value = currRow;
                    sheet["B" + pearsonRow.ToString()].Value = totalTraffic;
                    sheet["C" + pearsonRow.ToString()].Value = covidTable.Rows[a][6].ToString();

                    // set values for next iteration
                    totalTraffic = 0;
                    pearsonRow++;

                }

            }
            */

            int totalTraffic = 0;
            int totalCases = 0;
            int pearsonRow = 2;

            string covidMonth = covidTable.Rows[1][3].ToString().Split('/')[0];
            string covidYear = covidTable.Rows[1][3].ToString().Split('/')[2];


            for (int a = 0; a < covidTable.Rows.Count; a++)
            {
                string currMonth = covidTable.Rows[a][3].ToString().Split('/')[0];
                if(currMonth.Equals(covidMonth))
                {
                    int val = Int32.Parse(covidTable.Rows[a][6].ToString());
                    totalCases = Int32.Parse(covidTable.Rows[a][6].ToString()) + totalCases;
                }
                else
                {
                    for (int b = 0; b < airportTable.Rows.Count; b++)
                    {
                        string airportMonth = airportTable.Rows[b][1].ToString().Split('/')[0];
                        string airportYear = airportTable.Rows[b][1].ToString().Split('/')[2];
                        
                        
                        if (airportMonth.Equals(covidMonth) && airportYear.Equals(covidYear))
                        {
                            totalTraffic = Int32.Parse(airportTable.Rows[b][5].ToString()) + totalTraffic;
                        }
                        
                    }

                    //write date
                    sheet["A" + pearsonRow.ToString()].Value = covidMonth + "/ 1 / " + covidYear;
                    sheet["B" + pearsonRow.ToString()].Value = totalTraffic;
                    sheet["C" + pearsonRow.ToString()].Value = covidTable.Rows[a][6].ToString();

                    covidMonth = covidTable.Rows[a][3].ToString().Split('/')[0];
                    covidYear = covidTable.Rows[a][3].ToString().Split('/')[2];
                    totalCases = 0;
                    totalTraffic = 0;

                    pearsonRow++;
                }




            }






            Pearson.SaveAs(outputPath);

            Console.WriteLine("done");
            Console.ReadLine();
            
        }
    }
}
