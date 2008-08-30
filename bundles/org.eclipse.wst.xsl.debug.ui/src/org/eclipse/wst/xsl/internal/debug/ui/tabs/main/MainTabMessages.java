/*******************************************************************************
 * Copyright (c) 2007 Chase Technology Ltd - http://www.chasetechnology.co.uk
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Doug Satchwell (Chase Technology Ltd) - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xsl.internal.debug.ui.tabs.main;

import org.eclipse.osgi.util.NLS;

public final class MainTabMessages extends NLS
{
	private static final String BUNDLE_NAME = "org.eclipse.wst.xsl.internal.debug.ui.tabs.main.MainTabMessages"; //$NON-NLS-1$

	public static String StylesheetEntryLabelProvider_Invalid_path;

	public static String TransformsBlock_0;

	public static String TransformsBlock_Name;

	public static String InputFileBlock_DIRECTORY_NOT_SPECIFIED;

	public static String InputFileBlock_DIRECTORY_DOES_NOT_EXIST;

	public static String InputFileBlock_GROUP_NAME;

	public static String InputFileBlock_DEFAULT_RADIO;

	public static String InputFileBlock_OTHER_RADIO;

	public static String InputFileBlock_DIALOG_MESSAGE;

	public static String InputFileBlock_WORKSPACE_DIALOG_MESSAGE;

	public static String InputFileBlock_VARIABLES_BUTTON;

	public static String InputFileBlock_FILE_SYSTEM_BUTTON;

	public static String InputFileBlock_WORKSPACE_BUTTON;

	public static String InputFileBlock_Name;

	public static String InputFileBlock_Exception_occurred_reading_configuration;

	public static String InputFileBlock_WORKSPACE_DIALOG_TITLE;

	public static String XSLMainTab_TabName;

	public static String TransformsBlock_ParametersLabel;

	public static String TransformsBlock_StylesheetsLabel;

	static
	{
		// load message values from bundle file
		NLS.initializeMessages(BUNDLE_NAME, MainTabMessages.class);
	}

}
