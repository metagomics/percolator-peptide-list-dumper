package org.uwpr.metagomics.peptide_dumper.main;

public class ReporterFactory {
	public static IReporter getReporter( String xsdVersion ) throws Exception {
		return (IReporter)Class.forName( "org.uwpr.metagomics.peptide_dumper.main.ReporterXSD" + xsdVersion ).newInstance();
	}
}
