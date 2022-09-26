package msk.dsmn.utils;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public class Interaction {
	private String key;
	private String source;
	private String target;
	private String rhea;
	private Set<String> pathways;
	private Integer occurence = 0;
	private Set<String> intTypes;
	private Set<String> intIds;
	private Set<String> pathwayName;
	private Set<String> sourceChebi;
	private Set<String> targetChebi;
	private Set<String> sourceHMDB;
	private Set<String> targetHMDB;
	private Set<String> proteinName;
	private Set<String> proteinID;
	private Set<String> interactionID;
	private Set<String> rheaID;
	private Set<String> pathwayOnt;
	private Set<String> diseaseOnt;
	private Set<String> curationStatus;
	private Set<String> interactionRef;
	private Set<String> pathwayRef;
	private Set<String> sourceRef;
	private Set<String> targetRef;
	
	public Interaction(String key) {
		this.key = key;
		pathways = new LinkedHashSet<String>();
		intTypes = new HashSet<String>();
		intIds = new HashSet<String>();
		pathwayName = new LinkedHashSet<String>();
		sourceChebi = new HashSet<String>();
		targetChebi = new HashSet<String>();
		sourceHMDB = new HashSet<String>();
		targetHMDB = new HashSet<String>();
		proteinName = new LinkedHashSet<String>();
		proteinID = new LinkedHashSet<String>();
		interactionID = new LinkedHashSet<String>();
		rheaID = new LinkedHashSet<String>();
		pathwayOnt = new LinkedHashSet<String>();
		diseaseOnt = new LinkedHashSet<String>();
		curationStatus = new LinkedHashSet<String>();
		interactionRef = new LinkedHashSet<String>();
		pathwayRef = new LinkedHashSet<String>();
		sourceRef = new LinkedHashSet<String>();
		targetRef = new LinkedHashSet<String>();
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}
	
	public String getRhea() {
		return rhea;
	}

	public void setRhea(String rhea) {
		this.rhea = rhea;
	}	

	public Integer getOccurence() {
		return occurence;
	}

	public void setOccurence(Integer occurence) {
		this.occurence = occurence;
	}

	public Set<String> getPathways() {
		return pathways;
	}

	public Set<String> getIntTypes() {
		return intTypes;
	}

	public Set<String> getIntIds() {
		return intIds;
	}
	
	public Set<String> getPathwayName() {
		return pathwayName;
	}
	
	public Set<String> getSourceChebi() {
		return sourceChebi;
	}
	
	public Set<String> getTargetChebi() {
		return targetChebi;
	}
	
	public Set<String> getSourceHMDB() {
		return sourceHMDB;
	}
	
	public Set<String> getTargetHMDB() {
		return targetHMDB;
	}
	
	public Set<String> getProteinName() {
		return proteinName;
	}
	
	public Set<String> getProteinID() {
		return proteinID;
	}
	
	public Set<String> getInteractionID() {
		return interactionID;
	}
	
	public Set<String> getRheaID() {
		return rheaID;
	}
	
	public Set<String> getPathwayOnt() {
		return pathwayOnt;
	}
	
	public Set<String> getDiseaseOnt() {
		return diseaseOnt;
	}
	
	public Set<String> getCurationStatus() {
		return curationStatus;
	}
	
	public Set<String> getInteractionRef() {
		return interactionRef;
	}
	
	public Set<String> getPathwayRef() {
		return pathwayRef;
	}
	
	public Set<String> getSourceRef() {
		return sourceRef;
	}
	
	public Set<String> getTargetRef() {
		return targetRef;
	}


}
