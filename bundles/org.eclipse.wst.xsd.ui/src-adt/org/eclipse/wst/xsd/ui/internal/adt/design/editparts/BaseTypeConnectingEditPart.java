/*******************************************************************************
 * Copyright (c) 2001, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.wst.xsd.ui.internal.adt.design.editparts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.DirectEditRequest;
import org.eclipse.wst.xsd.ui.internal.adt.design.editparts.model.IFeedbackHandler;
import org.eclipse.wst.xsd.ui.internal.adt.design.editpolicies.IADTUpdateCommand;
import org.eclipse.wst.xsd.ui.internal.adt.editor.Messages;
import org.eclipse.wst.xsd.ui.internal.adt.facade.IType;

/**
 * This class provides some base function to enable drawing connections to a referenced type
 *
 */
public abstract class BaseTypeConnectingEditPart extends BaseEditPart implements IFeedbackHandler, IConnectionContainer
{
  private TypeReferenceConnection connectionFigure;  
  
  public void activate()
  {
    super.activate();         
  }
  
  public void deactivate()
  {
    deactivateConnection();
    super.deactivate();
  }
  
  public void refreshConnections()
  {
    deactivateConnection();     
    activateConnection();
  }

  protected void activateConnection()
  {    
    // If appropriate, create our connectionFigure and add it to the appropriate layer
    if (connectionFigure == null && shouldDrawConnection())
    {
      //System.out.println("activateeConnection()-pre:" + getClass().getName());           
      connectionFigure = createConnectionFigure();
      if (connectionFigure != null)
      {  
        // Add our editpolicy as a listener on the connection, so it can stay in synch
        //connectionFigure.addPropertyChangeListener((AttributeSelectionFeedbackPolicy) getEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE));
        //connectionFigure.addMouseListener(this);
        getLayer(LayerConstants.CONNECTION_LAYER).add(connectionFigure);
      }  
    }
  }
  
  protected void deactivateConnection()
  {
    // if we have a connection, remove it
    if (connectionFigure != null)
    {
      getLayer(LayerConstants.CONNECTION_LAYER).remove(connectionFigure);
      // Remove our editpolicy listener(s)
      //connectionFigure.removePropertyChangeListener((AttributeSelectionFeedbackPolicy) getEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE));
      //connectionFigure.removeMouseListener(this);
      connectionFigure = null;
    }
  }  
  
  protected boolean shouldDrawConnection()
  {
    return true;
  }
  
  public abstract TypeReferenceConnection createConnectionFigure();
  
  public void addFeedback()
  {
    if (connectionFigure != null)
    {
      connectionFigure.setHighlight(true);
    }
  }
  
  public void removeFeedback()
  {
    if (connectionFigure != null)
    {
      connectionFigure.setHighlight(false);
    }
  }
  
  protected class NameUpdateCommandWrapper extends Command implements IADTUpdateCommand
  {
    Command command;
    protected DirectEditRequest request;
    
    public NameUpdateCommandWrapper()
    {
      super(Messages._UI_ACTION_UPDATE_NAME);
    }

    public void setRequest(DirectEditRequest request)
    {
      this.request = request;
    }
    
    public void execute()
    {
      IType iType = (IType)getModel();
      Object newValue = request.getCellEditor().getValue();
      if (newValue instanceof String)
      {
        command = iType.getUpdateNameCommand((String)newValue);
      }
      if (command != null)
        command.execute();
    }
  }

  public boolean hitTest(IFigure target, Point location)
  {
    Rectangle b = target.getBounds().getCopy();
    target.translateToAbsolute(b);  
    return b.contains(location);
  }
}
