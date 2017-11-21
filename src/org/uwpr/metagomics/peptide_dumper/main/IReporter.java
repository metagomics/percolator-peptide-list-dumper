package org.uwpr.metagomics.peptide_dumper.main;

import java.io.File;

public interface IReporter {

	public void doReport( File percFile, double psmCutoff, double peptideCutoff ) throws Exception;
	
}
