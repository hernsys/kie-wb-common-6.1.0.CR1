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

import com.github.gwtbootstrap.client.ui.Collapse;
import com.github.gwtbootstrap.client.ui.CollapseTrigger;
import com.github.gwtbootstrap.client.ui.Divider;
import com.github.gwtbootstrap.client.ui.Label;
import com.github.gwtbootstrap.client.ui.NavLink;
import com.github.gwtbootstrap.client.ui.NavList;
import com.github.gwtbootstrap.client.ui.WellNavList;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import org.guvnor.common.services.project.model.Project;
import org.kie.workbench.common.screens.explorer.client.resources.i18n.ProjectExplorerConstants;
import org.kie.workbench.common.screens.explorer.client.utils.Classifier;
import org.kie.workbench.common.screens.explorer.client.utils.Utils;
import org.kie.workbench.common.screens.explorer.client.widgets.View;
import org.kie.workbench.common.screens.explorer.client.widgets.ViewPresenter;
import org.kie.workbench.common.screens.explorer.client.widgets.navigator.Explorer;
import org.kie.workbench.common.screens.explorer.client.widgets.navigator.NavigatorOptions;
import org.kie.workbench.common.screens.explorer.model.FolderItem;
import org.kie.workbench.common.screens.explorer.model.FolderItemType;
import org.kie.workbench.common.screens.explorer.model.FolderListing;
import org.kie.workbench.common.screens.explorer.service.Option;
import org.kie.workbench.common.screens.explorer.utils.Sorters;
import org.uberfire.backend.organizationalunit.OrganizationalUnit;
import org.uberfire.backend.repositories.Repository;
import org.uberfire.backend.vfs.Path;
import org.uberfire.client.common.BusyPopup;
import org.uberfire.client.workbench.type.AnyResourceType;
import org.uberfire.client.workbench.type.ClientResourceType;

/**
 * Business View implementation
 */
@ApplicationScoped
public class BusinessViewWidget extends Composite implements View {
	
	private static final String CURRENT_RESOURCE = "hiring.bpmn2";
	//private static final String CURRENT_RESOURCE = "Descuentos.drl";
	//private static final String CURRENT_RESOURCE = "empty.gdst";

	private static Object _item = null;

    interface BusinessViewImplBinder
            extends
            UiBinder<Widget, BusinessViewWidget> {

    }

    private static BusinessViewImplBinder uiBinder = GWT.create( BusinessViewImplBinder.class );

    private static final String ID_CLEANUP_PATTERN = "[^a-zA-Z0-9]";

    @UiField
    Explorer explorer;

    @UiField
    WellNavList itemsContainer;

    @Inject
    Classifier classifier;

    //TreeSet sorts members upon insertion
    private final Set<FolderItem> sortedFolderItems = new TreeSet<FolderItem>( Sorters.ITEM_SORTER );

    private final NavigatorOptions businessOptions = new NavigatorOptions() {{
        showFiles( false );
        showHiddenFiles( false );
        showDirectories( true );
        allowUpLink( false );
        showItemAge( false );
        showItemMessage( false );
        showItemLastUpdater( false );
    }};

    private ViewPresenter presenter;

    @PostConstruct
    public void init() {
        //Cannot create and bind UI until after injection points have been initialized
        initWidget( uiBinder.createAndBindUi( this ) );
    }

    @Override
    public void init( final ViewPresenter presenter ) {
        this.presenter = presenter;
        explorer.init( Explorer.Mode.COLLAPSED, businessOptions, Explorer.NavType.TREE, presenter );
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
        explorer.setupHeader( organizationalUnits, organizationalUnit,
                              repositories, repository,
                              projects, project );
        explorer.loadContent( folderListing, siblings );
        setItems( folderListing );
    }

    @Override
    public void setItems( final FolderListing folderListing ) {
        itemsContainer.clear();
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
                final Map.Entry<ClientResourceType, Collection<FolderItem>> e = itr.next();

                final CollapseTrigger collapseTrigger = makeTriggerWidget( e.getKey() );

                final Collapse collapse = new Collapse();
                collapse.setExistTrigger( true );
                collapse.setId( getCollapseId( e.getKey() ) );
                final NavList itemsNavList = new NavList();
                collapse.add( itemsNavList );
                for ( FolderItem folderItem : e.getValue() ) {
                	if(folderItem.getItem()!=null){
                		setResourceToLoad(folderItem );
                	}
                }
                collapse.setDefaultOpen( false );

                itemsContainer.add( collapseTrigger );
                itemsContainer.add( collapse );
                if ( itr.hasNext() ) {
                    itemsContainer.add( new Divider() );
                }
            }
            loadResourceSelected();
            
            
            
        } else {
            itemsContainer.add( new Label( ProjectExplorerConstants.INSTANCE.noItemsExist() ) );
        }
    }

    @Override
    public void setOptions( final Set<Option> options ) {
        if ( options.contains( Option.TREE_NAVIGATOR ) ) {
            explorer.setNavType( Explorer.NavType.TREE, businessOptions );
        } else {
            explorer.setNavType( Explorer.NavType.BREADCRUMB, businessOptions );
        }
    }

    @Override
    public Explorer getExplorer() {
        return explorer;
    }

    private CollapseTrigger makeTriggerWidget( final ClientResourceType resourceType ) {
        final CollapseTrigger collapseTrigger = new CollapseTrigger( "#" + getCollapseId( resourceType ) );
        final String description = getResourceTypeDescription( resourceType );
        final IsWidget icon = resourceType.getIcon();
        if ( icon == null ) {
            collapseTrigger.setWidget( new TriggerWidget( description ) );
        } else {
            collapseTrigger.setWidget( new TriggerWidget( icon,
                                                          description ) );

        }
        return collapseTrigger;
    }

    private String getResourceTypeDescription( final ClientResourceType resourceType ) {
        String description = resourceType.getDescription();
        description = ( description == null || description.isEmpty() ) ? ProjectExplorerConstants.INSTANCE.miscellaneous_files() : description;
        return description;
    }

    private IsWidget makeItemNavLink( final ClientResourceType resourceType,
                                      final FolderItem folderItem ) {
        String fileName = folderItem.getFileName();
        if ( !( resourceType instanceof AnyResourceType ) ) {
            fileName = Utils.getBaseFileName( fileName );
        }
        fileName = fileName.replaceAll( " ", "\u00a0" );
        final NavLink navLink = new NavLink( fileName );
        navLink.addClickHandler( new ClickHandler() {

            @Override
            public void onClick( ClickEvent event ) {
            	Window.alert("click en el boton del resurso");
                presenter.itemSelected( folderItem );
            }
        } );
        return navLink;
    }
    
    private void setResourceToLoad(final FolderItem folderItem){
        if ( folderItem.getType().equals( FolderItemType.FILE ) && folderItem.getItem() instanceof Path ) {
        	if(((Path) folderItem.getItem()).getFileName().indexOf(CURRENT_RESOURCE) > -1){	
        		_item = folderItem.getItem();
        	}
        } 
    }
    
    private void loadResourceSelected(){
    	if(_item != null){
    		presenter.loadItemSelected( _item );
    	}
    }
    
    

    private String getCollapseId( ClientResourceType resourceType ) {
        return resourceType != null ? resourceType.getShortName().replaceAll( ID_CLEANUP_PATTERN, "" ) : "";
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
