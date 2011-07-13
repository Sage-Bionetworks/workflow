package org.sagebionetworks.repo.model.jdo.persistence;

import java.util.Set;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.Element;
import javax.jdo.annotations.ForeignKey;
import javax.jdo.annotations.ForeignKeyAction;
import javax.jdo.annotations.NullValue;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.sagebionetworks.repo.model.query.jdo.SqlConstants;

@PersistenceCapable(detachable = "true", table=SqlConstants.TABLE_NODE)
public class JDONode {
		
	@PrimaryKey
	private Long id;
	
	@Persistent
	@Column(name=SqlConstants.COL_NODE_PARENT_ID)
    @ForeignKey(name="NODE_PARENT_FK", deleteAction=ForeignKeyAction.CASCADE)
	private JDONode parent;

	@Column(name=SqlConstants.COL_NODE_NAME)
	@Persistent(nullValue = NullValue.EXCEPTION) // cannot be null
	private String name;
	
	@Persistent (mappedBy = "parent")
	@Element(dependent = "true")
	private Set<JDONode> children;
		
    @Persistent(serialized="false", mappedBy = "owner")
	@Element(dependent = "true")
	private Set<JDOStringAnnotation> stringAnnotations;
	
    @Persistent(serialized="false", mappedBy = "owner")
	@Element(dependent = "true")
	private Set<JDOLongAnnotation> longAnnotations;
	
    @Persistent(serialized="false", mappedBy = "owner")
	@Element(dependent = "true")
	private Set<JDODoubleAnnotation> doubleAnnotations;
	
    @Persistent(serialized="false", mappedBy = "owner")
	@Element(dependent = "true")
	private Set<JDODateAnnotation> dateAnnotations;
	
    @Persistent(serialized="false", mappedBy = "owner")
	@Element(dependent = "true")
	private Set<JDOBlobAnnotation> blobAnnotations;
	
	
	@Persistent (mappedBy = "owner")
	@Element(dependent = "true")
	private Set<JDORevision> revisions;
	
	@Column(name=SqlConstants.COL_CURRENT_REV)
	private Long currentRevNumber;
		
	@Persistent
	@Column(name=SqlConstants.COL_NODE_DESCRIPTION ,jdbcType="VARCHAR", length=3000)
	private String description;

	@Column(name=SqlConstants.COL_NODE_ETAG)
	@Persistent (nullValue = NullValue.EXCEPTION) //cannot be null
	private Long eTag = null;
	
	@Column(name=SqlConstants.COL_NODE_CREATED_BY)
	@Persistent (nullValue = NullValue.EXCEPTION) //cannot be null
	private String createdBy;
	
	@Column(name=SqlConstants.COL_NODE_CREATED_ON)
	@Persistent (nullValue = NullValue.EXCEPTION) //cannot be null
	private Long createdOn;
	
	// The type is enforced by a Foreign key.
	@Column(name=SqlConstants.COL_NODE_TYPE)
	@Persistent (nullValue = NullValue.EXCEPTION) //cannot be null
	@ForeignKey(name="NODE_TYPE_FK", deleteAction=ForeignKeyAction.RESTRICT)
	private JDONodeType nodeType;	
	// Indicates the node that this node gets its permissions from.
	@Persistent
	@Column(name=SqlConstants.COL_NODE_BENEFACTOR_ID)
    @ForeignKey(name="NODE_BENEFACTOR_FK", deleteAction=ForeignKeyAction.CASCADE)
	private JDONode permissionsBenefactor;
	
	// These are the nodes that that benefit from this nodes permissions
	@Persistent (mappedBy = "permissionsBenefactor")
	@Element(dependent = "true")
	private Set<JDONode> permissionsBeneficiaries;
	
	
	public Long getCurrentRevNumber() {
		return currentRevNumber;
	}

	public void setCurrentRevNumber(Long currentRevNumber) {
		this.currentRevNumber = currentRevNumber;
	}

	public Set<JDORevision> getRevisions() {
		return revisions;
	}

	public void setRevisions(Set<JDORevision> revisions) {
		this.revisions = revisions;
	}

	public Set<JDONode> getChildren() {
		return children;
	}

	public void setChildren(Set<JDONode> children) {
		this.children = children;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<JDOStringAnnotation> getStringAnnotations() {
		return stringAnnotations;
	}

	public void setStringAnnotations(Set<JDOStringAnnotation> stringAnnotations) {
		this.stringAnnotations = stringAnnotations;
	}

	public Set<JDOLongAnnotation> getLongAnnotations() {
		return longAnnotations;
	}

	public void setLongAnnotations(Set<JDOLongAnnotation> longAnnotations) {
		this.longAnnotations = longAnnotations;
	}

	public Set<JDODoubleAnnotation> getDoubleAnnotations() {
		return doubleAnnotations;
	}

	public void setDoubleAnnotations(Set<JDODoubleAnnotation> doubleAnnotations) {
		this.doubleAnnotations = doubleAnnotations;
	}

	public Set<JDODateAnnotation> getDateAnnotations() {
		return dateAnnotations;
	}

	public void setDateAnnotations(Set<JDODateAnnotation> dateAnnotations) {
		this.dateAnnotations = dateAnnotations;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public JDONode getParent() {
		return parent;
	}

	public void setParent(JDONode parentId) {
		this.parent = parentId;
	}

	public Long geteTag() {
		return eTag;
	}

	public void seteTag(Long eTag) {
		this.eTag = eTag;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}

	public JDONodeType getNodeType() {
		return nodeType;
	}

	public Set<JDOBlobAnnotation> getBlobAnnotations() {
		return blobAnnotations;
	}

	public void setBlobAnnotations(Set<JDOBlobAnnotation> blobAnnotations) {
		this.blobAnnotations = blobAnnotations;
	}

	public void setNodeType(JDONodeType nodeType) {
		this.nodeType = nodeType;
	}

	public JDONode getPermissionsBenefactor() {
		return permissionsBenefactor;
	}

	public void setPermissionsBenefactor(JDONode permissionsBenefactor) {
		this.permissionsBenefactor = permissionsBenefactor;
	}

	public Set<JDONode> getPermissionsBeneficiaries() {
		return permissionsBeneficiaries;
	}

	public void setPermissionsBeneficiaries(Set<JDONode> permissionsBeneficiaries) {
		this.permissionsBeneficiaries = permissionsBeneficiaries;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JDONode other = (JDONode) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
