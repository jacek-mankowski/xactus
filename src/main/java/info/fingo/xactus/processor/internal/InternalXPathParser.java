/*******************************************************************************
 * Copyright (c) 2005, 2013 Andrea Bittau, University College London, and others
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Andrea Bittau - initial API and implementation from the PsychoPath XPath 2.0
 *     Bug 338494    - prohibiting xpath expressions starting with / or // to be parsed.
 *******************************************************************************/

package info.fingo.xactus.processor.internal;

import java.io.StringReader;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

import java_cup.runtime.Symbol;

import info.fingo.xactus.processor.StaticError;
import info.fingo.xactus.processor.XPathParserException;
import info.fingo.xactus.processor.ast.XPath;
import info.fingo.xactus.processor.internal.ast.XPathExpr;

/**
 * JFlexCupParser parses the xpath expression
 */
@SuppressWarnings("deprecation")
public class InternalXPathParser {
	private static boolean containsInvalidSurrogatePairs(String toTest) {
		CharacterIterator iterator = new StringCharacterIterator(toTest);

		for (char ch = iterator.first(); ch != CharacterIterator.DONE; ch = iterator.next()) {
			if (Character.isLowSurrogate(ch)) {
				return true;
			}

			if (Character.isHighSurrogate(ch) && !Character.isLowSurrogate(iterator.next())) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Tries to parse the xpath expression
	 *
	 * @param xpath
	 *            is the xpath string.
	 * @param isRootlessAccess
	 *            if 'true' then PsychoPath engine can't parse xpath expressions starting with / or //.
	 * @throws XPathParserException
	 *            when parsing exception occurs.
	 * @return the xpath value.
	 * @since 2.0
	 */
	public XPath parse(String xpath, boolean isRootlessAccess) throws XPathParserException {
		if (containsInvalidSurrogatePairs(xpath)) {
			throw new XPathParserException("Invalid surrogate pairs detected in XPath expression.");
		}

		XPathFlex lexer = new XPathFlex(new StringReader(xpath));

		try {
			XPathCup p = new XPathCup(lexer);
			Symbol res = p.parse();
			XPath xPath2 = (XPath) res.value;
			if (isRootlessAccess) {
				xPath2.accept(new DefaultVisitor() {
					@Override
					public Object visit(XPathExpr e) {
						if (e.slashes() > 0) {
							throw new XPathParserException("Access to root node is not allowed (set by caller)");
						}
						do {
							e.expr().accept(this); // check the single step (may have filter with root access)
							e = e.next(); // follow singly linked list of the path, it's all relative past the first one
						} while (e != null);
						return null;
					}
				});
			}
			return xPath2;
		} catch (JFlexError e) {
			throw new XPathParserException("JFlex lexer error: " + e.reason());
		} catch (CupError e) {
			throw new XPathParserException("CUP parser error: " + e.reason());
		} catch (StaticError e) {
			throw  new XPathParserException(e.code(), e.getMessage());
		} catch (Exception e) {
			throw new XPathParserException(e.getMessage());
		}
	}
}
