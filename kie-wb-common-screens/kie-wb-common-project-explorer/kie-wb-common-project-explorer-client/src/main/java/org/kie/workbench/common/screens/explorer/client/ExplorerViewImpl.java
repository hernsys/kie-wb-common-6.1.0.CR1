/*
 * Copyright 2013 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.kie.workbench.common.screens.explorer.client;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

import org.kie.workbench.common.screens.explorer.client.widgets.business.BusinessViewWidget;
import org.kie.workbench.common.screens.explorer.client.widgets.technical.TechnicalViewWidget;
import org.uberfire.client.common.BusyPopup;
import org.uberfire.lifecycle.OnOpen;

/**
 * The Explorer's view implementation
 */
@ApplicationScoped
public class ExplorerViewImpl extends Composite implements ExplorerView {

    interface ExplorerViewImplBinder
            extends
            UiBinder<Widget, ExplorerViewImpl> {

    }

    private static ExplorerViewImplBinder uiBinder = GWT.create( ExplorerViewImplBinder.class );



    private ExplorerPresenter presenter;

    @PostConstruct
    public void postConstruct() {
    	//Window..alert("***postConstruct ExplorerViewImpl");
    	//Cannot create and bind UI until after injection points have been initialized
        initWidget( uiBinder.createAndBindUi( this ) );

        getElement().getStyle().setPropertyPx( "minWidth", 370 );
    }
    
    @OnOpen
    public void onOpen(){
    	//Window..alert("***onOpen ExplorerViewImpl");
    }

    @Override
    public void init( final ExplorerPresenterImpl presenter ) {
    	//Window..alert("***init ExplorerViewImpl");
        this.presenter = presenter;
    }


    @Override
    public void showBusyIndicator( final String message ) {
        BusyPopup.showMessage( message );
    }

    @Override
    public void hideBusyIndicator() {
        BusyPopup.close();
    }

}
