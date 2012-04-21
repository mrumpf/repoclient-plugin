package org.jenkinsci.plugins.repoclient.client.artifactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.restlet.data.MediaType;
import org.restlet.engine.converter.ConverterHelper;
import org.restlet.engine.resource.VariantInfo;
import org.restlet.ext.jackson.JacksonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.Variant;
import org.restlet.resource.UniformResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class tells the RESTlet engine how to deserialize the model classess.
 *
 * @author mrumpf
 *
 */
public class ArtifactoryConverter extends ConverterHelper {

	private static final Logger logger = LoggerFactory.getLogger(ArtifactoryConverter.class);
	
	private static final String FOLDER_INFO_MIME_TYPE = "application/vnd.org.jfrog.artifactory.storage.FolderInfo+json";
	private static final VariantInfo FOLDER_INFO_VARIANT_INFO = new VariantInfo(new MediaType(FOLDER_INFO_MIME_TYPE));
	private static final List<VariantInfo> FOLDER_INFO_VARIANT_INFO_LIST = new ArrayList<VariantInfo>();

	private static final String FILE_INFO_MIME_TYPE = "application/vnd.org.jfrog.artifactory.storage.FileInfo+json";
	private static final VariantInfo FILE_INFO_VARIANT_INFO = new VariantInfo(
			new MediaType(FILE_INFO_MIME_TYPE));
	private static final List<VariantInfo> FILE_INFO_VARIANT_INFO_LIST = new ArrayList<VariantInfo>();

	static {
		FILE_INFO_VARIANT_INFO_LIST.add(FILE_INFO_VARIANT_INFO);
		FOLDER_INFO_VARIANT_INFO_LIST.add(FOLDER_INFO_VARIANT_INFO);
	}

	@Override
	public List<Class<?>> getObjectClasses(Variant source) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<VariantInfo> getVariants(Class<?> source) {
		List<VariantInfo> result = null;
		if (FolderInfo.class.equals(source)) {
			result = FOLDER_INFO_VARIANT_INFO_LIST;
		}
		else if (FileInfo.class.equals(source)) {
			result = FILE_INFO_VARIANT_INFO_LIST;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("getVariants -> " + source);
		}
		return result;
	}

	@Override
	public float score(Object source, Variant target, UniformResource resource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> float score(Representation source, Class<T> target,
			UniformResource resource) {
		float result = 0.0f;
		if (FolderInfo.class.equals(source)) {
			result = 1.0f;
		}
		else if (FileInfo.class.equals(source)) {
			result = 1.0f;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("score -> Representation source: " + source + ", Class target: " + target + ", resource: " + resource);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T toObject(Representation source, Class<T> target,
			UniformResource resource) throws IOException {
		T result = null;
		if (FolderInfo.class.equals(target)) {
			FolderInfo fi = (new JacksonRepresentation<FolderInfo>(source, FolderInfo.class)).getObject();
			result = (T) fi;
		}
		else if (FileInfo.class.equals(target)) {
			FileInfo fi = (new JacksonRepresentation<FileInfo>(source, FileInfo.class)).getObject();
			result = (T) fi;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("toObject -> Representation source: " + source + ", Class target: " + target + ", resource: " + resource);
		}
		return result;
	}

	@Override
	public Representation toRepresentation(Object source, Variant target,
			UniformResource resource) throws IOException {
		throw new UnsupportedOperationException();
	}
}
