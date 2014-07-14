/*
 * Copyright 2013 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kie.workbench.common.screens.javaeditor.client.editor;

import com.google.gwt.user.client.ui.Composite;
import org.kie.workbench.common.screens.javaeditor.client.widget.ViewJavaSourceWidget;
import org.kie.workbench.common.widgets.client.widget.BusyIndicatorView;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class JavaSourceViewImpl extends Composite
                                implements JavaSourceView {

    @Inject
    private BusyIndicatorView busyIndicatorView;

    @Inject
    private ViewJavaSourceWidget javaSourceViewer;

    @PostConstruct
    public void initialize() {
        initWidget( javaSourceViewer );
    }

    @Override
    public void setContent( final String content ) {
        javaSourceViewer.setContent( content );
    }

    @Override
    public void clear() {
        javaSourceViewer.clearContent();
    }

    @Override
    public void showBusyIndicator( final String message ) {
        busyIndicatorView.showBusyIndicator( message );
    }

    @Override
    public void hideBusyIndicator() {
        busyIndicatorView.hideBusyIndicator();
    }

}
