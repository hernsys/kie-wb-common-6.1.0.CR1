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
package org.kie.workbench.common.screens.explorer.client.widgets.business;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.guvnor.common.services.project.model.Project;
import org.kie.workbench.common.screens.explorer.client.resources.i18n.ProjectExplorerConstants;
import org.kie.workbench.common.screens.explorer.client.utils.Classifier;
import org.kie.workbench.common.screens.explorer.client.widgets.View;
import org.kie.workbench.common.screens.explorer.client.widgets.ViewPresenter;
import org.kie.workbench.common.screens.explorer.client.widgets.navigator.Explorer;
import org.kie.workbench.common.screens.explorer.model.FolderItem;
import org.kie.workbench.common.screens.explorer.model.FolderItemType;
import org.kie.workbench.common.screens.explorer.model.FolderListing;
import org.kie.workbench.common.screens.explorer.service.Option;
import org.kie.workbench.common.screens.explorer.utils.Sorters;
import org.uberfire.backend.organizationalunit.OrganizationalUnit;
import org.uberfire.backend.repositories.Repository;
import org.uberfire.backend.vfs.FileSystem;
import org.uberfire.backend.vfs.Path;
import org.uberfire.backend.vfs.PathFactory;
import org.uberfire.client.common.BusyPopup;
import org.uberfire.client.workbench.type.ClientResourceType;

import com.github.gwtbootstrap.client.ui.WellNavList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Business View implementation
 */
@ApplicationScoped
public class BusinessViewWidget extends Composite implements View {
	
	private static final String PATH_RESOURCE = "pathResource";
	private static final String DEFAULT_MASTER = "default://master@";
	private static final String PARAM_READONLY = "readOnly";
	private static final String PARAM_REPOSITORY = "repository";
	private static final String RESOURCE_NOT_FOUND = "Resource not found";
	private static FileSystem fs;
	private static Path path = null;

	interface BusinessViewImplBinder
            extends
            UiBinder<Widget, BusinessViewWidget> {

    }

    private static BusinessViewImplBinder uiBinder = GWT.create( BusinessViewImplBinder.class );

    @UiField
    Explorer explorer;

//    @UiField
//    WellNavList itemsContainer;

    @Inject
    Classifier classifier;

    //TreeSet sorts members upon insertion
    private final Set<FolderItem> sortedFolderItems = new TreeSet<FolderItem>( Sorters.ITEM_SORTER );

    private ViewPresenter presenter;

    @PostConstruct
    public void init() {
    	//Window.alert("***init in BusinessViewWidget");
        //Cannot create and bind UI until after injection points have been initialized
        initWidget( uiBinder.createAndBindUi( this ) );
    }

    @Override
    public void init( final ViewPresenter presenter ) {
        this.presenter = presenter;
    }

    @Override
    public void setContent( final Set<OrganizationalUnit> organizationalUnits,
                            final OrganizationalUnit organizationalUnit,
                            final Set<Repository> repositories,
                            final Repository repository,
                            final Set<Project> projects,
                            final Project project,
                            final FolderListing folderListing,
                            final Map<FolderItem, List<FolderItem>> siblings ) {
    	//Window.alert("***setContent BusinessViewWidget");
    	if(getNameRepo() != null){
        	setItems( folderListing );
        }else{
        	Window.alert(RESOURCE_NOT_FOUND);
        }
    }
    
    @Override
    public void setItems( final FolderListing folderListing ) {
    	
        sortedFolderItems.clear();
        for ( final FolderItem content : folderListing.getContent() ) {
            if ( !content.getType().equals( FolderItemType.FOLDER ) ) {
                sortedFolderItems.add( content );
            }
        }

        if ( !sortedFolderItems.isEmpty() ) {
            final Map<ClientResourceType, Collection<FolderItem>> resourceTypeGroups = classifier.group( sortedFolderItems );
            final TreeMap<ClientResourceType, Collection<FolderItem>> sortedResourceTypeGroups = new TreeMap<ClientResourceType, Collection<FolderItem>>( Sorters.RESOURCE_TYPE_GROUP_SORTER );
            sortedResourceTypeGroups.putAll( resourceTypeGroups );

            final Iterator<Map.Entry<ClientResourceType, Collection<FolderItem>>> itr = sortedResourceTypeGroups.entrySet().iterator();
            while ( itr.hasNext() ) {
            	readFolderItem(itr.next());
                if(fs != null) break;
            }
            loadResource();
        } else {
        	Window.alert(ProjectExplorerConstants.INSTANCE.noItemsExist() );
        }
    }
    
    private void readFolderItem(Map.Entry<ClientResourceType, Collection<FolderItem>> e){
    	for ( FolderItem folderItem : e.getValue() ) {
        	if(folderItem.getItem() != null && folderItem.getType().equals( FolderItemType.FILE ) && folderItem.getItem() instanceof Path){
        		setFsByFile(folderItem);
        		if(fs != null) break;
        	}
        }
    }
    
    private void setFsByFile(FolderItem folderItem){
    	PathFactory.PathImpl pathImpl = (PathFactory.PathImpl) folderItem.getItem();
   		fs = pathImpl.getFileSystem();
    }

    @Override
    public void setOptions( final Set<Option> options ) {
    }

    @Override
    public Explorer getExplorer() {
        return explorer;
    }

    private void loadResource(){
    	String nameRepo = getNameRepo();
        if(nameRepo != null){
        	path = PathFactory.newPath(fs, getFileName(Window.Location.getHref()), nameRepo);
        	if(path != null){
        		presenter.loadItemSelected( path, editorReadOnly() );
        	}
        }else{
        	Window.alert(RESOURCE_NOT_FOUND);
        }
    }

    @Override
    public void showBusyIndicator( final String message ) {
        BusyPopup.showMessage( message );
    }

    @Override
    public void hideBusyIndicator() {
        BusyPopup.close();
    }
    
    private String getNameRepo(){
    	String repo = (Window.Location.getParameter(PARAM_REPOSITORY) == null) ? null : Window.Location.getParameter(PARAM_REPOSITORY);  
    	return DEFAULT_MASTER + repo + getPathResource();  
    }
    
    private String getPathResource(){
    	String pathResource = (Window.Location.getParameter(PATH_RESOURCE) == null) ? null : Window.Location.getParameter(PATH_RESOURCE);  
    	return pathResource;
    }
    
    private String editorReadOnly(){
    	return (Window.Location.getParameter(PARAM_READONLY) != null && Window.Location.getParameter(PARAM_READONLY).equals("true")) 
    			? "true" : null;
    }
    
    
    private String getFileName(String hrefValue){
    	String nameRepo = getNameRepo();
        return nameRepo.split("/")[nameRepo.split("/").length - 1];
    }

}
