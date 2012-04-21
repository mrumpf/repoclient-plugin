package org.jenkinsci.plugins.repoclient.client.nexus;

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

public class NexusConverter extends ConverterHelper {

	private static final Logger logger = LoggerFactory
			.getLogger(NexusConverter.class);

	private static final String MIME_TYPE = "application/json";
	private static final VariantInfo FOLDER_INFO_VARIANT_INFO = new VariantInfo(
			new MediaType(MIME_TYPE));
	private static final VariantInfo FILE_INFO_VARIANT_INFO = new VariantInfo(
			new MediaType(MIME_TYPE));
	private static final List<VariantInfo> VARIANT_INFO_LIST = new ArrayList<VariantInfo>();

	static {
		VARIANT_INFO_LIST.add(FILE_INFO_VARIANT_INFO);
		VARIANT_INFO_LIST.add(FOLDER_INFO_VARIANT_INFO);
	}

	@Override
	public List<Class<?>> getObjectClasses(Variant source) {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<VariantInfo> getVariants(Class<?> source) {
		if (logger.isDebugEnabled()) {
			logger.debug("getVariants -> " + source);
		}
		return VARIANT_INFO_LIST;
	}

	@Override
	public float score(Object source, Variant target, UniformResource resource) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> float score(Representation source, Class<T> target,
			UniformResource resource) {
		float result = 0.0f;
		if (ContentItem.class.equals(target)) {
			result = 1.0f;
		} else if (DataContainer.class.equals(target)) {
			result = 1.0f;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("score -> Representation source: " + source
					+ ", Class target: " + target + ", resource: " + resource);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T toObject(Representation source, Class<T> target,
			UniformResource resource) throws IOException {
		T result = null;
		if (DataContainer.class.equals(target)) {
			DataContainer dc = (new JacksonRepresentation<DataContainer>(
					source, DataContainer.class)).getObject();
			result = (T) dc;
		} else if (ContentItem.class.equals(target)) {
			ContentItem fi = (new JacksonRepresentation<ContentItem>(source,
					ContentItem.class)).getObject();
			result = (T) fi;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("toObject -> Representation source: " + source
					+ ", Class target: " + target + ", resource: " + resource);
		}
		return result;
	}

	@Override
	public Representation toRepresentation(Object source, Variant target,
			UniformResource resource) throws IOException {
		throw new UnsupportedOperationException();
	}
}
