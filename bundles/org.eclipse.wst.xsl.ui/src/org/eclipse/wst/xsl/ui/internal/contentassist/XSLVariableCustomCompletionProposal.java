/*******************************************************************************
 * Copyright (c) 2008 Standards for Technology in Automotive Retail and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     
 *******************************************************************************/
package org.eclipse.wst.xsl.ui.internal.contentassist;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocument;
import org.eclipse.wst.sse.ui.internal.contentassist.CustomCompletionProposal;

/**
 * @author dcarver
 *
 */
public class XSLVariableCustomCompletionProposal extends CustomCompletionProposal {

	/**
	 * @param replacementString
	 * @param replacementOffset
	 * @param replacementLength
	 * @param cursorPosition
	 * @param image
	 * @param displayString
	 * @param contextInformation
	 * @param additionalProposalInfo
	 * @param relevance
	 * @param updateReplacementLengthOnValidate
	 */
	public XSLVariableCustomCompletionProposal(String replacementString,
			int replacementOffset, int replacementLength, int cursorPosition,
			Image image, String displayString,
			IContextInformation contextInformation,
			String additionalProposalInfo, int relevance,
			boolean updateReplacementLengthOnValidate) {
		super(replacementString, replacementOffset, replacementLength,
				cursorPosition, image, displayString, contextInformation,
				additionalProposalInfo, relevance,
				updateReplacementLengthOnValidate);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param replacementString
	 * @param replacementOffset
	 * @param replacementLength
	 * @param cursorPosition
	 * @param image
	 * @param displayString
	 * @param contextInformation
	 * @param additionalProposalInfo
	 * @param relevance
	 */
	public XSLVariableCustomCompletionProposal(String replacementString,
			int replacementOffset, int replacementLength, int cursorPosition,
			Image image, String displayString,
			IContextInformation contextInformation,
			String additionalProposalInfo, int relevance) {
		super(replacementString, replacementOffset, replacementLength,
				cursorPosition, image, displayString, contextInformation,
				additionalProposalInfo, relevance);
		// TODO Auto-generated constructor stub
	}
	
	/** 
	 * Create a positional based Proposal and replace the text at that position.
	 * @see org.eclipse.wst.sse.ui.internal.contentassist.CustomCompletionProposal#apply(org.eclipse.jface.text.ITextViewer, char, int, int)
	 */
	@Override
	public void apply(ITextViewer viewer, char trigger, int stateMask,
			int offset) {
		// TODO Auto-generated method stub
//		super.apply(viewer, trigger, stateMask, offset);
		IStructuredDocument document = (IStructuredDocument)viewer.getDocument();
		Position position = new Position(offset);
	    int currentPosition =  getCursorPosition();
	    int startOffset = document.getRegionAtCharacterOffset(offset).getStart();
	    int existingLength = offset - startOffset;
	    
		PositionBasedCompletionProposal proposal = 
			new PositionBasedCompletionProposal(getReplacementString(), position, existingLength + getReplacementString().length());
		proposal.apply(document);
	}
}
