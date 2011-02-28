package org.sagebionetworks.repo.model.gaejdo;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;

/**
 * We follow this approach to persist when other classes inherit from this one
 * http
 * ://www.wetfeetblog.com/google-app-engine-java-jdo-inheritance-one-to-many-
 * relationships/242
 * 
 * 
 * @author bhoff
 * 
 */

@PersistenceCapable(detachable = "false")
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
abstract public class GAEJDODatasetLayer<T extends GAEJDODatasetLayer<T>>
		implements GAEJDORevisable<T>, GAEJDOBase, GAEJDOAnnotatable {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key id;

	@Persistent
	private String name;

	@Persistent
	private Date creationDate;

	@Persistent
	private Date publicationDate;

	@Persistent
	private Text description;

	@Persistent
	private Text releaseNotes;

	// this is a reference to the version info object
	@Persistent(dependent = "true")
	private GAEJDORevision<T> revision;

	@Persistent(dependent = "true")
	private GAEJDOAnnotations annotations;

	@Persistent(dependent = "true")
	private GAEJDOLayerLocations locations;

	@Persistent
	private Text preview;

	/**
	 * Default constructor
	 * <p>
	 * 
	 * Set the values of all Text fields to the empty string because it can't
	 * handle nulls
	 */
	public GAEJDODatasetLayer() {
		setDescription(new Text(""));
		setReleaseNotes(new Text(""));
		setPreview(new Text(""));
	}

	public GAEJDORevision<T> getRevision() {
		return revision;
	}

	public void setRevision(GAEJDORevision<T> revision) {
		this.revision = revision;
	}

	public GAEJDOAnnotations getAnnotations() {
		return annotations;
	}

	public void setAnnotations(GAEJDOAnnotations annotations) {
		this.annotations = annotations;
	}

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public Text getDescription() {
		return description;
	}

	public void setDescription(Text description) {
		if (description.getValue() == null)
			throw new NullPointerException("Null Text not allowed.");
		this.description = description;
	}

	public Text getReleaseNotes() {
		return releaseNotes;
	}

	public void setReleaseNotes(Text releaseNotes) {
		if (description.getValue() == null)
			throw new NullPointerException("Null Text not allowed.");
		this.releaseNotes = releaseNotes;
	}

	/**
	 * @param preview
	 *            the preview to set
	 */
	public void setPreview(Text preview) {
		this.preview = preview;
	}

	/**
	 * @return the preview
	 */
	public Text getPreview() {
		return preview;
	}

	/**
	 * @param locations
	 *            the locations to set
	 */
	public void setLocations(GAEJDOLayerLocations locations) {
		this.locations = locations;
	}

	/**
	 * @return the locations
	 */
	public GAEJDOLayerLocations getLocations() {
		return locations;
	}

	public static Collection<String> getPrimaryFields() {
		return Arrays.asList(new String[] { "name", "creationDate",
				"publicationDate", "releaseNotes" });
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
		if (!(obj instanceof GAEJDODatasetLayer))
			return false;
		GAEJDODatasetLayer other = (GAEJDODatasetLayer) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
