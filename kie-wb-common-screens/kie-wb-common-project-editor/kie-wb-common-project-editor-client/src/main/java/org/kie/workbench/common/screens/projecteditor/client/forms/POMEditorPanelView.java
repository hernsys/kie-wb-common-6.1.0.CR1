/*
 * Copyright 2012 JBoss Inc
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

package org.kie.workbench.common.screens.projecteditor.client.forms;

import com.google.gwt.user.client.ui.IsWidget;
import org.guvnor.common.services.project.model.GAV;
import org.kie.workbench.common.widgets.client.widget.HasBusyIndicator;

public interface POMEditorPanelView extends HasBusyIndicator,
                                            IsWidget {

    interface Presenter {

        void addNameChangeHandler( NameChangeHandler changeHandler );

        void addGroupIdChangeHandler( GroupIdChangeHandler changeHandler );

        void addArtifactIdChangeHandler( ArtifactIdChangeHandler changeHandler );

        void addVersionChangeHandler( VersionChangeHandler changeHandler );

        void onNameChange( String name );

        void onDescriptionChange( String description );

    }

    void setPresenter( Presenter presenter );

    String getTitleWidget();

    void setTitleText( String titleText );

    void showSaveSuccessful( String fileName );

    void setName( String projectName );

    void setDescription( String projectDescription );

    void setGAV( GAV gav );

    void addGroupIdChangeHandler( GroupIdChangeHandler changeHandler );

    void addArtifactIdChangeHandler( ArtifactIdChangeHandler changeHandler );

    void addVersionChangeHandler( VersionChangeHandler changeHandler );

    void setReadOnly();
}
