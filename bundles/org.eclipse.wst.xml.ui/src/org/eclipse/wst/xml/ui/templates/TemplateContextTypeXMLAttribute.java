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
package org.eclipse.wst.xml.ui.templates;

import org.eclipse.wst.xml.ui.nls.ResourceHandler;

/**
 * Templates of this context type apply to any attributes within XML content
 * type.
 */
public class TemplateContextTypeXMLAttribute extends TemplateContextTypeXML {

	public TemplateContextTypeXMLAttribute() {
		super(generateContextTypeId(TemplateContextTypeIds.ATTRIBUTE), ResourceHandler.getString("TemplateContextTypeXMLAttribute.0")); //$NON-NLS-1$
	}
}
