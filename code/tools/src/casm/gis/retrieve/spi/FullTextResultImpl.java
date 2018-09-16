package casm.gis.retrieve.spi;

import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.response.FacetField;

import casm.gis.retrieve.api.FullTextResult;

/*
 * Implementation class of FullTextResult interface
 * 2017-04-30 15:34:17
 */
public class FullTextResultImpl implements FullTextResult{
	
	public List resultList = new ArrayList();
	
	public List<FacetField> facetList = new ArrayList<FacetField>();
	
	public long numFound;

	@Override
	public List getResultList() {
		return resultList;
	}

	@Override
	public void setResultList(List list) {
		this.resultList = list;
	}

	@Override
	public List getFacetList() {
		return facetList;
	}

	@Override
	public void setFacetList(List list) {
		this.facetList = list;
	}

	@Override
	public long getNumFound() {
		return numFound;
	}

	@Override
	public void setNumFound(long numFound) {
		this.numFound = numFound;
	}

}