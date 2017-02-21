package cn.edu.ruc.Mexplore.domain;


public class FeatureKey {
	private int relationId;
	private int targetId;
	
	public FeatureKey(int relationId, int targetId){
		setRelationId(relationId);
		setTargetId(targetId);
	}
	
	@Override
	public boolean equals(Object object){
		if(!(object instanceof FeatureKey))
			return false;
		if(object == this)
			return true;
		return (relationId == ((FeatureKey) object).getRelationId()) && (targetId == ((FeatureKey) object).getTargetId());
	}
	
	public int hashCode(){
		return new Integer(relationId + targetId).hashCode();
	}
	
	public int getRelationId() {
		return relationId;
	}
	
	public void setRelationId(int relationId) {
		this.relationId = relationId;
	}
	
	public int getTargetId() {
		return targetId;
	}
	
	public void setTargetId(int targetId) {
		this.targetId = targetId;
	}
}
