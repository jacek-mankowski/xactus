/*******************************************************************************
 * Copyright (c) 2004, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     
 *******************************************************************************/

package org.eclipse.wst.xml.core.internal.provisional.document;

import org.w3c.dom.Entity;

public interface IDOMEntity extends Entity {

	/**
	 * NOT IMPLEMENTED. Is defined here in preparation of DOM 3.
	 */
	public String getInputEncoding();

	/**
	 * NOT IMPLEMENTED. Is defined here in preparation of DOM 3.
	 */
	public String getXmlEncoding();

	/**
	 * NOT IMPLEMENTED. Is defined here in preparation of DOM 3.
	 */
	public String getXmlVersion();
}
