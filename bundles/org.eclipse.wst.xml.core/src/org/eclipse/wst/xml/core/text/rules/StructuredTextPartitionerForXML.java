/*******************************************************************************
 * Copyright (c) 2001, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Jens Lukowski/Innoopract - initial renaming/restructuring
 *     
 *******************************************************************************/
package org.eclipse.wst.xml.core.text.rules;

import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.wst.sse.core.internal.parser.ForeignRegion;
import org.eclipse.wst.sse.core.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.text.IStructuredTextPartitioner;
import org.eclipse.wst.sse.core.text.ITextRegion;
import org.eclipse.wst.sse.core.text.rules.StructuredTextPartitioner;
import org.eclipse.wst.xml.core.internal.parser.regions.BlockTextRegion;
import org.eclipse.wst.xml.core.parser.XMLRegionContext;


public class StructuredTextPartitionerForXML extends StructuredTextPartitioner implements IStructuredTextPartitioner {
	public final static String ST_DEFAULT_XML = "org.eclipse.wst.xml.DEFAULT_XML"; //$NON-NLS-1$
	public final static String ST_XML_CDATA = "org.eclipse.wst.xml.XML_CDATA"; //$NON-NLS-1$
	public final static String ST_XML_PI = "org.eclipse.wst.xml.XML_PI"; //$NON-NLS-1$
	public final static String ST_XML_DECLARATION = "org.eclipse.wst.xml.XML_DECL"; //$NON-NLS-1$
	public final static String ST_XML_COMMENT = "org.eclipse.wst.xml.XML_COMMENT"; //$NON-NLS-1$

	/**
	 * Should match
	 * org.eclipse.wst.sse.core.dtd.partitioning.StructuredTextPartitionerForDTD.ST_DTD_SUBSET
	 */
	public static final String ST_DTD_SUBSET = "org.eclipse.wst.xml.dtd.internal_subset"; //$NON-NLS-1$

	private final static String[] configuredContentTypes = new String[]{ST_DEFAULT_XML, ST_XML_CDATA, ST_XML_PI, ST_XML_DECLARATION, ST_XML_COMMENT, ST_DTD_SUBSET};

	/**
	 * Constructor for JSPDocumentPartioner.
	 */
	public StructuredTextPartitionerForXML() {
		super();
	}

	protected void setInternalPartition(int offset, int length, String type) {
		super.setInternalPartition(offset, length, type);
	}

	protected void initLegalContentTypes() {
		fSupportedTypes = configuredContentTypes;
	}

	/**
	 * @see com.ibm.sed.model.StructuredTextPartitioner#getPartitionType(com.ibm.sed.structuredDocument.ITextRegion)
	 */
	public String getPartitionType(ITextRegion region, int offset) {
		String result = null;
		if (region.getType() == XMLRegionContext.XML_COMMENT_TEXT)
			result = ST_XML_COMMENT;
		else if (region.getType() == XMLRegionContext.XML_CDATA_TEXT)
			result = ST_XML_CDATA;
		else if (region.getType() == XMLRegionContext.XML_PI_OPEN)
			result = ST_XML_PI;
		else if (region.getType() == XMLRegionContext.XML_DOCTYPE_DECLARATION)
			result = ST_XML_DECLARATION;
		else if (region.getType() == XMLRegionContext.XML_DOCTYPE_INTERNAL_SUBSET)
			result = ST_DTD_SUBSET;
		else
			result = super.getPartitionType(region, offset);
		return result;
	}

	protected String getPartitionType(ForeignRegion region, int offset) {
		// temp added just to dis-ambiguate call from subclass
		return super.getPartitionType(region, offset);
	}

	/**
	 * @see com.ibm.sed.structuredDocument.partition.IStructuredTextPartitioner#getPartitionTypeBetween(com.ibm.sed.structuredDocument.ITextRegion,
	 *      com.ibm.sed.structuredDocument.ITextRegion)
	 */
	public String getPartitionTypeBetween(IStructuredDocumentRegion previousNode, ITextRegion previousStartTagNameRegion, IStructuredDocumentRegion nextNode, ITextRegion nextEndTagNameRegion) {
		return super.getPartitionTypeBetween(previousNode, previousStartTagNameRegion, nextNode, nextEndTagNameRegion);
	}

	public String getDefault() {
		return ST_DEFAULT_XML;
	}

	public IDocumentPartitioner newInstance() {
		StructuredTextPartitionerForXML instance = new StructuredTextPartitionerForXML();
		return instance;
	}

	/**
	 * @return
	 */
	public static String[] getConfiguredContentTypes() {
		return configuredContentTypes;
	}

	protected String getPartitionFromBlockedText(ITextRegion region, int offset, String result) {
		// was moved to subclass for quick transition
		String newResult = result;
		// nsd_TODO: David and I need to discuss, design, and implement this
		// for all block tags and comments
		// and make sure is part of "extensible" design of block tags
		if (region.getType() == XMLRegionContext.BLOCK_TEXT) {
			// for code safety, we'll always check instanceof, but I think
			// always true.
			if (region instanceof BlockTextRegion) {
				// super is used below so won't be ambiguous
				newResult = getPartitionType((BlockTextRegion) region, offset);
			} else if (region instanceof ForeignRegion) {
				newResult = getPartitionType((ForeignRegion) region, offset);
			} else {
				newResult = getUnknown();
			}
		}
		return newResult;
	}

	protected boolean doParserSpecificCheck(int offset, boolean partitionFound, IStructuredDocumentRegion sdRegion, IStructuredDocumentRegion previousStructuredDocumentRegion, ITextRegion next, ITextRegion previousStart) {
		// this was moved down to subclass of StructuredTextPartioner
		// for quick fix to transition problems. Heirarchy needs lots of
		// cleanup.
		if (previousStart != null && previousStart.getType() == XMLRegionContext.XML_TAG_OPEN && next.getType() == XMLRegionContext.XML_END_TAG_OPEN) {
			ITextRegion previousName = previousStructuredDocumentRegion.getRegionAtCharacterOffset(previousStructuredDocumentRegion.getEndOffset(previousStart));
			ITextRegion nextName = sdRegion.getRegionAtCharacterOffset(sdRegion.getEndOffset(next));
			if (previousName != null && nextName != null && previousName.getType() == XMLRegionContext.XML_TAG_NAME && nextName.getType() == XMLRegionContext.XML_TAG_NAME) {
				setInternalPartition(offset, 0, getPartitionTypeBetween(previousStructuredDocumentRegion, previousName, sdRegion, nextName));
				partitionFound = true;
			}
		}
		return partitionFound;
	}
}
