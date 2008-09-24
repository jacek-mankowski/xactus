/*******************************************************************************
 * Copyright (c) 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.jst.jsp.ui.tests;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IMarker;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.IAnnotationModel;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jst.jsp.core.internal.java.IJSPProblem;
import org.eclipse.jst.jsp.core.internal.java.IJSPTranslation;
import org.eclipse.jst.jsp.core.internal.java.JSPTranslationAdapter;
import org.eclipse.jst.jsp.core.internal.java.JSPTranslationExtension;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.AnnotationTypeLookup;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wst.sse.core.internal.provisional.INodeAdapter;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMModel;
import org.eclipse.wst.xml.core.internal.provisional.document.IDOMNode;


/**
 * Opens the current JSP editor's current translated source in a Java editor
 * 
 * Invoke with M1+M2+9
 * 
 * @author nitin
 */
public class ShowTranslationHandler extends AbstractHandler {

	/**
	 * 
	 */
	public ShowTranslationHandler() {
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands
	 * .ExecutionEvent)
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// IDE.openEditor(event.getApplicationContext(), createEditorInput(),
		// JavaUI.ID_CU_EDITOR, true);
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if (selection instanceof IStructuredSelection) {
			List list = ((IStructuredSelection) selection).toList();
			if (!list.isEmpty()) {
				if (list.get(0) instanceof IDOMNode) {
					IDOMModel model = ((IDOMNode) list.get(0)).getModel();
					INodeAdapter adapter = model.getDocument().getAdapterFor(IJSPTranslation.class);
					if (adapter != null) {
						// create an IEditorInput for the Java editor
						IStorageEditorInput input = new JSPTranslationEditorInput(model);
						try {
							IEditorPart editor = IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), input, JavaUI.ID_CU_EDITOR, true);
							// Now add the problems we found
							if (editor instanceof ITextEditor) {
								IAnnotationModel annotationModel = ((ITextEditor) editor).getDocumentProvider().getAnnotationModel(input);
								JSPTranslationAdapter translationAdapter = (JSPTranslationAdapter) model.getDocument().getAdapterFor(IJSPTranslation.class);
								JSPTranslationExtension translation = translationAdapter.getJSPTranslation();
								translation.reconcileCompilationUnit();
								List problemsList = translation.getProblems();
								IProblem[] problems = (IProblem[]) problemsList.toArray(new IProblem[problemsList.size()]);
								AnnotationTypeLookup lookup = new AnnotationTypeLookup();
								for (int i = 0; i < problems.length; i++) {
									if (problems[i] instanceof IJSPProblem)
										continue;
									int length = problems[i].getSourceEnd() - problems[i].getSourceStart() + 1;
									Position position = new Position(problems[i].getSourceStart(), length);
									Annotation annotation = null;
									String type = lookup.getAnnotationType(IMarker.PROBLEM, IMarker.SEVERITY_INFO);
									if (problems[i].isError()) {
										type = lookup.getAnnotationType(IMarker.PROBLEM, IMarker.SEVERITY_ERROR);
									}
									else if (problems[i].isWarning()) {
										type = lookup.getAnnotationType(IMarker.PROBLEM, IMarker.SEVERITY_WARNING);
									}
									annotation = new Annotation(type, false, problems[i].getMessage());
									if (annotation != null) {
										annotationModel.addAnnotation(annotation, position);
									}
								}
							}
						}
						catch (PartInitException e) {
							e.printStackTrace();
							Display.getCurrent().beep();
						}
					}
				}
			}
		}
		return null;
	}
}
