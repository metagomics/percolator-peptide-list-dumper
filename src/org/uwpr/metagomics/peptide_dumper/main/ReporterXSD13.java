package org.uwpr.metagomics.peptide_dumper.main;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.per_colator.percolator_out._13.Peptide;
import com.per_colator.percolator_out._13.PercolatorOutput;
import com.per_colator.percolator_out._13.Psm;

public class ReporterXSD13 implements IReporter {

	private static final String poutPackage = "com.per_colator.percolator_out._13";

	
	public void doReport( File percFile, double psmCutoff, double peptideCutoff ) throws Exception {
		
        // a map of psm ids to xml psm objects
        Collection<String> psmSet = new HashSet<>();

        // a map of peptide sequences to a collection of corresponding xml psm objects
        Map<String, Collection<String>> peptideMap = new HashMap<>();
        
		Reader percReader = null;
		try {
			
			percReader = new FileReader( percFile );
			
			// unmarshal our XML and get to work
			JAXBContext jaxbContext = JAXBContext.newInstance( poutPackage );
			Unmarshaller u = jaxbContext.createUnmarshaller();
			PercolatorOutput po = (PercolatorOutput)u.unmarshal( percReader );
		    
		    // loop through PSMs gather PSMs that meet the cutoff
		    System.err.println( "Reading PSMs..." );
		    for( Psm xpsm : po.getPsms().getPsm() ) {
		    	String psmId = xpsm.getPsmId();
		    	
		    	if( psmSet.contains( psmId ) )
		    		throw new Exception( "Found PSM ID " + psmId + " twice... Aborting." );
		    	
		    	if( Double.valueOf(xpsm.getQValue()) <= psmCutoff )
		    		psmSet.add( psmId );
		    }
		    
		    // loop through peptides
		    System.err.println( "Reading peptides..." );
		    for( Peptide xpeptide : po.getPeptides().getPeptide() ) {
		    	String seq = xpeptide.getPeptideId();
		    	seq = cleanPeptideSequence( seq );
		    	
		    	
		    	if( Double.valueOf( xpeptide.getQValue() ) > peptideCutoff )
		    		continue;
		    	
		    	
		    	if( !peptideMap.containsKey( seq ) )
		    		peptideMap.put( seq, new HashSet<>() );
		    	
		    	
		    	for( String psmid : xpeptide.getPsmIds().getPsmId() ) {
		    		if( psmSet.contains( psmid ) )
		    			peptideMap.get( seq ).add( psmid );
		    	}
		    	
		    }
		    
		} finally {
			if( percReader != null ) {
				try { percReader.close(); percReader = null; }
				catch( Exception e ) { ; }
			}
		}
		

		for( String sequence : peptideMap.keySet() ) {
			
			int size = peptideMap.get( sequence ).size();
			
			if( size > 0 )
				System.out.println( sequence + "\t" + size );
			
		}
		
	}
	
	/**
	 * Remove all non alphabetical characters from string and return it
	 * @param sequence
	 * @return
	 */
	private String cleanPeptideSequence( String sequence ) {
		return sequence.replaceAll("[^a-zA-Z]", "");
	}
}


