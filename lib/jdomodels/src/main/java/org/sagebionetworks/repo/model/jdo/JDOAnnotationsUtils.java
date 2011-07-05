package org.sagebionetworks.repo.model.jdo;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.sagebionetworks.repo.model.Annotations;
import org.sagebionetworks.repo.model.jdo.persistence.JDOBlobAnnotation;
import org.sagebionetworks.repo.model.jdo.persistence.JDODateAnnotation;
import org.sagebionetworks.repo.model.jdo.persistence.JDODoubleAnnotation;
import org.sagebionetworks.repo.model.jdo.persistence.JDOLongAnnotation;
import org.sagebionetworks.repo.model.jdo.persistence.JDONode;
import org.sagebionetworks.repo.model.jdo.persistence.JDORevision;
import org.sagebionetworks.repo.model.jdo.persistence.JDOStringAnnotation;

import com.thoughtworks.xstream.XStream;

/**
 * Helper utilities for converting between JDOAnnotations and Annotations (DTO).
 * 
 * @author jmhill
 *
 */
public class JDOAnnotationsUtils {
	

	/**
	 * Update the JDO from the DTO
	 * @param dto
	 * @param jdo
	 * @throws IOException 
	 */
	@SuppressWarnings("unchecked")
	public static void updateFromJdoFromDto(Annotations dto, JDONode jdo, JDORevision rev) throws IOException {
		if(dto.getId() != null){
			jdo.setId(Long.valueOf(dto.getId()));
		}
		jdo.setStringAnnotations((Set<JDOStringAnnotation>)createFromMap(jdo, dto.getStringAnnotations()));
		jdo.setDateAnnotations((Set<JDODateAnnotation>)createFromMap(jdo, dto.getDateAnnotations()));
		jdo.setLongAnnotations((Set<JDOLongAnnotation>)createFromMap(jdo, dto.getLongAnnotations()));
		jdo.setDoubleAnnotations((Set<JDODoubleAnnotation>)createFromMap(jdo, dto.getDoubleAnnotations()));
		jdo.setBlobAnnotations((Set<JDOBlobAnnotation>)createFromMap(jdo, dto.getBlobAnnotations()));
		// Compress the annotations and save them in a blob
		rev.setAnnotations(compressAnnotations(dto));
	}
	
	/**
	 * Convert the passed annotations to a compressed (zip) byte array
	 * @param dto
	 * @return
	 * @throws IOException 
	 */
	public static byte[] compressAnnotations(Annotations dto) throws IOException{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BufferedOutputStream buff = new BufferedOutputStream(out);
		GZIPOutputStream zipper = new GZIPOutputStream(buff);
		try{
			XStream xstream = new XStream();
			xstream.toXML(dto, zipper);
			zipper.flush();
			zipper.close();
			return out.toByteArray();
		}finally{
			zipper.flush();
			zipper.close();
		}

	}
	/**
	 * Read the compressed (zip) byte array into the Annotations.
	 * @param zippedByes
	 * @return
	 * @throws IOException 
	 */
	public static Annotations decompressedAnnotations(byte[] zippedByes) throws IOException{
		Annotations results =  Annotations.createInitialized();
		if(zippedByes != null){
			ByteArrayInputStream in = new ByteArrayInputStream(zippedByes);
			GZIPInputStream unZipper = new GZIPInputStream(in);
			try{
				// Now read using XStream
				XStream xstream = new XStream();
				if(zippedByes != null){
					xstream.fromXML(unZipper, results);
				}

			}finally{
				unZipper.close();
			}			
		}
		return results;
	}

	
	/**
	 * Create a new Annotations object from the JDO.
	 * @param jdo
	 * @return
	 * @throws IOException 
	 */
	public static Annotations createFromJDO(JDORevision rev) throws IOException{
		if(rev == null) throw new IllegalArgumentException("JDOAnnotations cannot be null");
		return decompressedAnnotations(rev.getAnnotations());
	}
	
	/**
	 * Build up the map from the set.
	 * @param <A>
	 * @param set
	 * @return
	 */
	public static <A> Map<String, Collection<A>> createFromSet(Set<? extends JDOAnnotation<A>> set){
		Map<String, Collection<A>> map = new HashMap<String, Collection<A>>();
		if(set != null){
			Iterator<? extends JDOAnnotation<A>> it = set.iterator();
			while(it.hasNext()){
				JDOAnnotation<A> jdoAno = it.next();
				String key = jdoAno.getAttribute();
				A value = jdoAno.getValue();
				Collection<A> collection = map.get(key);
				if(collection == null){
					collection = new ArrayList<A>();
					map.put(key, collection);
				}
				collection.add(value);
			}
		}
		return map;
	}
	
	/**
	 * Create a set of JDOAnnoations from a map
	 * @param <T>
	 * @param owner
	 * @param annotation
	 * @return
	 */
	public static <T> Set<? extends JDOAnnotation<T>> createFromMap(JDONode owner, Map<String, Collection<T>> annotation){
		Set<JDOAnnotation<T>> set = new HashSet<JDOAnnotation<T>>();
		if(annotation != null){
			Iterator<String> keyIt = annotation.keySet().iterator();
			while(keyIt.hasNext()){
				String key = keyIt.next();
				Collection<T> valueColection = annotation.get(key);
				Iterator<T> valueIt = valueColection.iterator();
				while(valueIt.hasNext()){
					T value = valueIt.next();
					JDOAnnotation<T> jdo = createAnnotaion(owner, key, value);
					set.add(jdo);
				}
			}
		}
		return set;
	}
	
	/**
	 * Create a single JDOAnnotation.
	 * @param <T>
	 * @param owner
	 * @param key
	 * @param value
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> JDOAnnotation<T> createAnnotaion(JDONode owner, String key, T value){
		if(key == null) throw new IllegalArgumentException("Key cannot be null");
		if(value == null) throw new IllegalArgumentException("Value cannot be null");
		JDOAnnotation<T>  jdo = null;
		if(value instanceof String){
			JDOStringAnnotation temp =  new JDOStringAnnotation();
			temp.setOwner(owner);
			jdo = (JDOAnnotation<T>) temp;
		}else if(value instanceof Date){
			JDODateAnnotation temp =  new JDODateAnnotation();
			temp.setOwner(owner);
			jdo = (JDOAnnotation<T>) temp;
		}else if(value instanceof Long){
			JDOLongAnnotation temp =  new JDOLongAnnotation();
			temp.setOwner(owner);
			jdo = (JDOAnnotation<T>) temp;
		}else if(value instanceof Double){
			JDODoubleAnnotation temp =  new JDODoubleAnnotation();
			temp.setOwner(owner);
			jdo = (JDOAnnotation<T>) temp;
		}else if(value instanceof byte[]){
			JDOBlobAnnotation temp =  new JDOBlobAnnotation();
			temp.setOwner(owner);
			jdo = (JDOAnnotation<T>) temp;
		}else{
			throw new IllegalArgumentException("Unknown annoation type: "+value.getClass());
		}
		jdo.setAttribute(key);
		jdo.setValue(value);
		return jdo;
	}

}
