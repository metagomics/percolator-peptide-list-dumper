package org.uwpr.metagomics.peptide_dumper.main;

import java.io.File;

import org.yeastrc.proteomics.percolator.out.PercolatorOutXMLUtils;

import jargs.gnu.CmdLineParser;
import jargs.gnu.CmdLineParser.IllegalOptionValueException;
import jargs.gnu.CmdLineParser.UnknownOptionException;

/**
 * Take a percolator output file and output a tab-delimited list of
 * distinct peptides and number of PSMs, where the peptides and PSMs
 * meet the supplied q-value cutoffs.
 * 
 * @author mriffle
 *
 */
public class MainProgram {

	public static void main( String[] args ) throws Exception {
		
		CmdLineParser cmdLineParser = new CmdLineParser();
        
		
		// peptide-level cutoff
		CmdLineParser.Option peptideOpt = cmdLineParser.addDoubleOption( 'p', "peptide" );	
		
		// psm-level cutoff
		CmdLineParser.Option psmOpt = cmdLineParser.addDoubleOption( 's', "psm" );	
        
		// percolator xml file
		CmdLineParser.Option percFileOpt = cmdLineParser.addStringOption( 'f', "file" );	
		
        // they want help
		CmdLineParser.Option helpOpt = cmdLineParser.addBooleanOption('h', "help"); 
		
        // parse command line options
        try { cmdLineParser.parse(args); }
        catch (IllegalOptionValueException e) {
            System.err.println(e.getMessage());
            printHelp();
            System.exit( 0 );
        }
        catch (UnknownOptionException e) {
            System.err.println(e.getMessage());
            printHelp();
            System.exit( 0 );
        }
        
        Boolean help = (Boolean) cmdLineParser.getOptionValue(helpOpt, Boolean.FALSE);
        if(help) {
        	printHelp();
        	System.exit( 0 );
        }
        
        Double psmCutoff = (Double)cmdLineParser.getOptionValue( psmOpt );
        Double peptideCutoff = (Double)cmdLineParser.getOptionValue( peptideOpt );
        String percFilename = (String)cmdLineParser.getOptionValue( percFileOpt );
        
        if( percFilename == null  ) {
        	System.err.println( "Must specify a percolator input file.\n" );
        	printHelp();
        	System.exit( 1 );
        }
        
        File percFile = new File( percFilename );
        if( !percFile.exists() ) {
        	System.err.println( "Specified percolator file does not exist.\n" );
        	printHelp();
        	System.exit( 1 );
        }
		
        if( psmCutoff == null )
        	psmCutoff = 0.01;
        
        if( peptideCutoff == null )
        	peptideCutoff = 0.01;
       
       
        String xsdVersion = PercolatorOutXMLUtils.getXSDVersion( percFile );
        ReporterFactory.getReporter( xsdVersion ).doReport( percFile, psmCutoff, peptideCutoff);		
	}
	
	private static void printHelp() {
		System.err.println( "Usage: java -jar peptideDumper.jar -p <peptide q-value cutoff> -s <psm q-value cutoff> -f <percolator output XML file" );
		System.err.println( "Example: java -jar peptideDumper.jar -p 0.01 -s 0.05 -f /data/percolator/percout.xml" );
	}
	
}
