/*******************************************************************************
 * Copyright (c) 2009, 2017 Standards for Technology in Automotive Retail and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     David Carver (STAR) - initial API and implementation 
 *     Mukul Gandhi - bug 280798 - PsychoPath support for JDK 1.4
 *******************************************************************************/
package org.eclipse.wst.xml.xpath2.processor.testsuite.userdefined;


import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSSimpleTypeDefinition;
import org.eclipse.wst.xml.xpath2.processor.DynamicError;
import org.eclipse.wst.xml.xpath2.processor.ResultSequence;
import org.eclipse.wst.xml.xpath2.processor.ResultSequenceFactory;
import org.eclipse.wst.xml.xpath2.processor.internal.types.AnyType;
import org.eclipse.wst.xml.xpath2.processor.internal.types.XSFloat;


public class XercesFloatUserDefined extends XSFloat {

	private XSObject typeInfo;

	public XercesFloatUserDefined(XSObject typeInfo) {
		this.typeInfo = typeInfo;
	}
	
	public XercesFloatUserDefined(float x) {
		super(x);
	}
	
	public ResultSequence constructor(ResultSequence arg) throws DynamicError {
			ResultSequence rs = ResultSequenceFactory.create_new();

			if (arg.empty())
				return rs;


			//AnyAtomicType aat = (AnyAtomicType) arg.first();
			AnyType aat = arg.first();
			
			XSSimpleTypeDefinition simpletype = (XSSimpleTypeDefinition) typeInfo;
			if (simpletype != null) {
				if (simpletype.isDefinedFacet(XSSimpleTypeDefinition.FACET_MININCLUSIVE)) {
					String minValue = simpletype.getLexicalFacetValue(XSSimpleTypeDefinition.FACET_MININCLUSIVE);
					float iminValue = Float.parseFloat(minValue);
					float actualValue = Float.parseFloat(aat.getStringValue());
	
					if (actualValue < iminValue) {
						throw DynamicError.invalidForCastConstructor();
					}
				}
	
				if (simpletype.isDefinedFacet(XSSimpleTypeDefinition.FACET_MAXINCLUSIVE)) {
					String maxValue = simpletype.getLexicalFacetValue(XSSimpleTypeDefinition.FACET_MAXINCLUSIVE);
					float imaxValue = Float.parseFloat(maxValue);
					float actualValue = Float.parseFloat(aat.getStringValue());
					if (actualValue > imaxValue) {
						throw DynamicError.invalidForCastConstructor();
					}
				}
			}
			
			rs.add(new XercesFloatUserDefined(Float.parseFloat(aat.getStringValue())));

			return rs;
	}	

	public String type_name() {
		return typeInfo.getName();
	}
	
}
