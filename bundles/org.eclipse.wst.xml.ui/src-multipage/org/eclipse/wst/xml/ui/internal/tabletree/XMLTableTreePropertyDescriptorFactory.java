/*****************************************************************************
 * Copyright (c) 2004 IBM Corporation and others. All rights reserved. This
 * program and the accompanying materials are made available under the terms
 * of the Eclipse Public License v1.0 which accompanies this distribution, and
 * is available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: IBM Corporation - initial API and implementation
 ****************************************************************************/


package org.eclipse.wst.xml.ui.internal.tabletree;

import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.eclipse.wst.common.contentmodel.CMAttributeDeclaration;
import org.eclipse.wst.common.contentmodel.CMElementDeclaration;
import org.eclipse.wst.common.contentmodel.CMNode;
import org.eclipse.wst.common.contentmodel.modelquery.ModelQuery;
import org.eclipse.wst.sse.ui.views.properties.EnumeratedStringPropertyDescriptor;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;


public class XMLTableTreePropertyDescriptorFactory extends DOMPropertyDescriptorFactory {

	protected TreeContentHelper treeContentHelper = new TreeContentHelper();

	public XMLTableTreePropertyDescriptorFactory(ModelQuery modelQuery) {
		super(modelQuery);
	}

	protected IPropertyDescriptor createPropertyDescriptorHelper(String name, Element element, CMNode cmNode) {
		IPropertyDescriptor result = null;

		String[] valuesArray = getModelQuery().getPossibleDataTypeValues(element, cmNode);
		if (valuesArray != null && valuesArray.length > 0) {
			result = new EnumeratedStringPropertyDescriptor(name, name, valuesArray);
		}

		return result;
	}

	public IPropertyDescriptor createTextPropertyDescriptor(Text text) {
		IPropertyDescriptor result = null;
		Node parentNode = text.getParentNode();
		if (parentNode != null && parentNode.getNodeType() == Node.ELEMENT_NODE) {
			Element parentElement = (Element) parentNode;
			CMElementDeclaration ed = getModelQuery().getCMElementDeclaration(parentElement);
			if (ed != null) {
				result = createPropertyDescriptorHelper(HACK, parentElement, ed);
			}
		}

		if (result == null) {
			result = new TextPropertyDescriptor(HACK, HACK);
		}

		return result;
	}

	public IPropertyDescriptor createAttributePropertyDescriptor(Attr attr) {
		IPropertyDescriptor result = null;

		String attributeName = attr.getName();

		CMAttributeDeclaration ad = getModelQuery().getCMAttributeDeclaration(attr);
		if (ad != null) {
			result = createPropertyDescriptorHelper(attributeName, attr.getOwnerElement(), ad);
		}

		if (result == null) {
			result = new TextPropertyDescriptor(attributeName, attributeName);
		}

		return result;
	}

	public IPropertyDescriptor createElementPropertyDescriptor(Element element) {
		IPropertyDescriptor result = null;
		List list = treeContentHelper.getElementTextContent(element);
		if (list != null) {
			Text text = treeContentHelper.getEffectiveTextNodeForCombinedNodeList(list);
			if (text != null) {
				result = createTextPropertyDescriptor(text);
			}
		}

		if (result == null) {
			result = new TextPropertyDescriptor(HACK, HACK);
		}
		return result;
	}
}