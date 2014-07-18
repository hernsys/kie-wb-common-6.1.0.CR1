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
package org.kie.workbench.common.screens.explorer.client.widgets;

import java.util.Set;

import com.google.gwt.user.client.ui.HasVisibility;

import org.guvnor.common.services.project.model.Package;
import org.guvnor.common.services.project.model.Project;
import org.kie.workbench.common.screens.explorer.model.FolderItem;
import org.kie.workbench.common.screens.explorer.model.FolderListing;
import org.kie.workbench.common.screens.explorer.service.Option;
import org.uberfire.backend.organizationalunit.OrganizationalUnit;
import org.uberfire.backend.repositories.Repository;

/**
 * Base for the different views
 */
public interface ViewPresenter extends HasVisibility {

    void update( final Set<Option> options );

    void initialiseViewForActiveContext( final OrganizationalUnit organizationalUnit );

    void initialiseViewForActiveContext( final OrganizationalUnit organizationalUnit,
                                         final Repository repository );

    void initialiseViewForActiveContext( final OrganizationalUnit organizationalUnit,
                                         final Repository repository,
                                         final Project project );

    void initialiseViewForActiveContext( final OrganizationalUnit organizationalUnit,
                                         final Repository repository,
                                         final Project project,
                                         final Package pkg );

    void organizationalUnitSelected( final OrganizationalUnit organizationalUnit );

    void repositorySelected( final Repository repository );

    void projectSelected( final Project project );

    void activeFolderItemSelected( final FolderItem folderItem );

    void itemSelected( final FolderItem folderItem );
    
    //TODO BORRAR
    public void loadItemSelected( Object _item );
    
    public void loadItemSelected( org.uberfire.backend.vfs.Path _item );

    void refresh();

    void loadContent( final FolderItem item,
                      final Set<Option> options );

    Set<Option> getActiveOptions();

    FolderListing getActiveContent();

    void deleteItem( FolderItem folderItem );

    void renameItem( FolderItem folderItem );

    void copyItem( FolderItem folderItem );
}
